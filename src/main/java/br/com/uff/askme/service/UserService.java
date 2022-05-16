package br.com.uff.askme.service;

import br.com.uff.askme.dto.ListUserResponse;
import br.com.uff.askme.dto.UpdateProfileRequest;
import br.com.uff.askme.dto.UpdateUserRequest;
import br.com.uff.askme.dto.UserRequest;
import br.com.uff.askme.error.exception.BadRequestException;
import br.com.uff.askme.error.exception.UnauthorizedException;
import br.com.uff.askme.model.Profile;
import br.com.uff.askme.model.User;
import br.com.uff.askme.repository.ProfileRepository;
import br.com.uff.askme.repository.UserRepository;
import br.com.uff.askme.security.TokenService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static br.com.uff.askme.model.Role.ROLE_ADMINISTRATOR;
import static br.com.uff.askme.model.Role.ROLE_USER;

@Service
@Slf4j
@AllArgsConstructor
public class UserService {
    
    private UserRepository userRepository;
    private TokenService tokenService;
    private ProfileRepository profileRepository;

    public Page<ListUserResponse> listUsers(String name, Pageable pageable) {
        Page<User> users;

        if (StringUtils.isBlank(name)) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByName(name, pageable);
        }
        return ListUserResponse.convertList(users);
    }
    
    public User createUser(UserRequest userRequest) throws Exception {
        verifyAlreadyRegisteredUser(userRequest.getEmail());
        User user = userRequest.convert();

        if (StringUtils.isBlank(userRequest.getRole())) {
            user.addProfile(profileRepository.findByRole(ROLE_USER));
        }
        else {
            user.addProfile(buildAuthorities(userRequest.getRole()));
        }

        user.setPassword(cryptPassword(user.getPassword()));
        return saveUser(user);
    }

    private Profile buildAuthorities(String profile) {
        if (profile.equals(ROLE_USER.toString())) {
            return profileRepository.findByRole(ROLE_USER);
        }
        return profileRepository.findByRole(ROLE_ADMINISTRATOR);
    }

    public User getUserById(Long userId) {
        Optional<User> user = userRepository.findById(userId);
        return user.orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));
    }

    public User updateUser(Long userId, UpdateUserRequest updateUserRequest, HttpServletRequest httpRequest) throws Exception {
        Long userIdFromToken = recuperateUserIdFromToken(httpRequest);

        if (userId.equals(userIdFromToken)) {
            User user = getUserById(userId);

            if (StringUtils.isNotBlank(updateUserRequest.getName())) {
                user.setName(updateUserRequest.getName());
            }

            if (StringUtils.isNotBlank(updateUserRequest.getEmail())) {
                verifyAlreadyRegisteredUser(updateUserRequest.getEmail());
                user.setEmail(updateUserRequest.getEmail());
            }
            
            if ((oldPassIsNotBlank(updateUserRequest) && newPassIsNotBlank(updateUserRequest))) {
                if (passwordMatcher(updateUserRequest, user)) {
                    user.setPassword(cryptPassword(updateUserRequest.getNewPassword()));
                } else {
                    throw new BadRequestException("Senha incorreta");
                }
            }

            return saveUser(user);
        }
        else {
            throw new UnauthorizedException("Este usuário não pode atualizar outro usuário");
        }
    }

    public User updateProfileUser(Long userId, UpdateProfileRequest updateProfile) throws Exception {
        User user = getUserById(userId);
        Profile profile = buildAuthorities(updateProfile.getRole());

        if (userWillPassAUserProfile(user, profile)) {
            user.getProfiles().remove(profileRepository.findByRole(ROLE_ADMINISTRATOR));
        }
        else {
            if (user.getProfiles().contains(profile)) {
                throw new BadRequestException("Usuário ja pertence a esse perfil");
            } else {
                user.addProfile(buildAuthorities(updateProfile.getRole()));
            }
        }

        return saveUser(user);
    }

    private boolean userWillPassAUserProfile(User user, Profile profile) {
        return user.getProfiles().size() == 2 && profile.getRole().equals(ROLE_USER);
    }

    private User saveUser(User user) throws Exception {
        try {
            return userRepository.save(user);
        }
        catch (DataIntegrityViolationException e) {
            log.error("Usuario com nome ja existente");
            throw new BadRequestException("User name duplicated");
        }
        catch (Exception | Error e) {
            log.error("Erro ao salvar usuario", e.getCause());
            throw new Exception("Erro ao salvar usuario");
        }
    }

    public void deleteUser(Long userId, HttpServletRequest httpRequest) {
        Long userIdFromToken = recuperateUserIdFromToken(httpRequest);

        if (userId.equals(userIdFromToken)) {
            getUserById(userId);
            userRepository.deleteById(userId);
        }
        else {
            User user = getUserById(userIdFromToken);
            if (isAdministrator(user)) {
                userRepository.deleteById(userId);
            } else {
                throw new UnauthorizedException("Este usuário não pode deletar outro usuário");
            }
        }
    }

    private boolean isAdministrator(User user) {
        return user.getProfiles().stream().anyMatch(profile -> profile.getRole().equals(ROLE_ADMINISTRATOR));
    }

    private void verifyAlreadyRegisteredUser(String email) {
        Optional<User> registeredUser = userRepository.findByEmail(email);

        if (registeredUser.isPresent()) {
            throw new BadRequestException("Email já cadastrado");
        }
    }

    private Long recuperateUserIdFromToken(HttpServletRequest httpRequest) {
        String token = tokenService.recuperateToken(httpRequest);
        return tokenService.getUserId(token);
    }

    private String cryptPassword(String password) {
        return new BCryptPasswordEncoder().encode(password);
    }

    private boolean passwordMatcher(UpdateUserRequest updateUserRequest, User user) {
        String oldPass = cryptPassword(updateUserRequest.getOldPassword());
        return user.getPassword().equals(oldPass);
    }

    private boolean newPassIsNotBlank(UpdateUserRequest updateUserRequest) {
        return StringUtils.isNotBlank(updateUserRequest.getNewPassword());
    }

    private boolean oldPassIsNotBlank(UpdateUserRequest updateUserRequest) {
        return StringUtils.isNotBlank(updateUserRequest.getOldPassword());
    }

}
