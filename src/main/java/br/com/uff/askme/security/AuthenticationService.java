package br.com.uff.askme.security;

import br.com.uff.askme.dto.LoginRequest;
import br.com.uff.askme.error.exception.BadRequestException;
import br.com.uff.askme.model.User;
import br.com.uff.askme.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthenticationService implements UserDetailsService {

    @Autowired
    private AuthenticationManager authManager;
    private final UserRepository userRepository;
    private final TokenService tokenService;

    public AuthenticationService(UserRepository userRepository, TokenService tokenService) {
        this.userRepository = userRepository;
        this.tokenService = tokenService;
    }

    public String authenticateUser(LoginRequest loginRequest) {
        UsernamePasswordAuthenticationToken loginData = loginRequest.convert();

        try {
            Authentication authenticate = authManager.authenticate(loginData);
            return tokenService.generateToken(authenticate);
        }
        catch (AuthenticationException exception) {
            throw new BadRequestException("Usuário ou senha incorretos");
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        return user.orElseThrow(() -> new BadRequestException("Usuário ou senha incorretos"));
    }

}
