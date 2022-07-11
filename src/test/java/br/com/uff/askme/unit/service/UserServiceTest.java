package br.com.uff.askme.unit.service;

import br.com.uff.askme.dto.ListUserResponse;
import br.com.uff.askme.dto.UserRequest;
import br.com.uff.askme.error.exception.BadRequestException;
import br.com.uff.askme.error.exception.NotFoundException;
import br.com.uff.askme.model.User;
import br.com.uff.askme.repository.UserRepository;
import br.com.uff.askme.service.UserService;
import br.com.uff.askme.util.UserCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("should_return_paginated_list_of_users")
    public void listUserPaginated() {
        List<User> userList = Arrays.asList(UserCreator.userLaion(), UserCreator.userEdna(), UserCreator.userPaula());
        PageImpl<User> userListPaginated = new PageImpl<>(userList, buildPageable(), 2);

        Mockito.when(userRepository.findAll(any(PageRequest.class))).thenReturn(userListPaginated);
        Page<ListUserResponse> listUserResponses = userService.listUsers(null, buildPageable());

        assertFalse(listUserResponses.isEmpty());
        assertThat(listUserResponses.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("should_return_user_list_find_by_name")
    public void listUserByName() {
        List<User> userList = Arrays.asList(UserCreator.userPaulaSouza(), UserCreator.userEdna(), UserCreator.userPaula());
        PageImpl<User> oneUserPage = new PageImpl<>(userList, buildPageable(), 2);

        Mockito.when(userRepository.findByName(eq("Paula"), any(PageRequest.class))).thenReturn(oneUserPage);
        Page<ListUserResponse> users = userService.listUsers("Paula", buildPageable());

        assertFalse(users.isEmpty());
        assertThat(users.getTotalElements()).isEqualTo(2);
    }

    @Test
    @DisplayName("should_return_empty_list_users_find_by_name_nonexistent")
    public void emptyUserListByAuthorNonexistent() {
        PageImpl<User> oneUserPage = new PageImpl<>(Collections.emptyList(), buildPageable(), 0);

        Mockito.when(userRepository.findByName(eq("xxxxx"), any(PageRequest.class))).thenReturn(oneUserPage);
        Page<ListUserResponse> users = userService.listUsers("xxxxx", buildPageable());

        assertTrue(users.isEmpty());
        assertThat(users.getTotalElements()).isEqualTo(0);
    }

    @Test
    @DisplayName("should_return_user_find_by_id")
    public void getUserById() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.ofNullable(UserCreator.userNilson()));
        User user = userService.getUserById(1L);

        assertThat(user).isNotNull();
        assertThat(user.getName()).isEqualTo("Nilson Faria");
    }

    @Test
    @DisplayName("should_return_not_found_search_users_by_id_nonexistent")
    public void getUserByIdNotFound() {
        Throwable exception = catchThrowable(() -> userService.getUserById(10L));

        assertThat(exception)
                .isInstanceOf(NotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    @DisplayName("should_save_user_successfully")
    public void createUser() throws Exception {
        Mockito.doNothing().when(userRepository).findByEmail(anyString());
        Mockito.when(userRepository.save(any(User.class))).thenReturn(new User("Sonia", "sonia@email.com", "khNBi56F44AS"));

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Sonia");
        userRequest.setEmail("sonia@email.com");
        userRequest.setPassword("khNBi56F44AS");

        User user = userService.createUser(userRequest);

        assertThat(user).isNotNull();
        assertThat(user.getUserId()).isNotNull();
        assertThat(user.getEmail()).isEqualTo("sonia@email.com");
    }

    @Test
    @DisplayName("should_not_update_user_with_email_duplicated")
    public void updateUserEmailDuplicated() {
        Mockito.doNothing().when(userRepository).findByEmail(anyString());
        Mockito.when(userRepository.save(any(User.class))).thenThrow(DataIntegrityViolationException.class);

        UserRequest userRequest = new UserRequest();
        userRequest.setName("Sonia");
        userRequest.setEmail("sonia@email.com");
        userRequest.setPassword("khNBi56F44AS");

        Throwable exception = catchThrowable(() -> userService.createUser(userRequest));

        assertThat(exception)
                .isInstanceOf(BadRequestException.class)
                .hasMessage("User name duplicated");
    }

    private Pageable buildPageable() {
        return PageRequest.of(0, 10, Sort.Direction.ASC, "userId");
    }

}
