package br.com.uff.askme.controller;

import br.com.uff.askme.dto.*;
import br.com.uff.askme.model.User;
import br.com.uff.askme.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private UserService userService;

    @GetMapping
    public Page<ListUserResponse> list(@RequestParam(required = false) String name, @PageableDefault(sort = "userId", direction = ASC) Pageable pageable) {
        return userService.listUsers(name, pageable);
    }

    @PostMapping("/signup")
    public ResponseEntity<String> create(@Valid @RequestBody UserRequest userRequest, UriComponentsBuilder uriBuilder) throws Exception {
        User user = userService.createUser(userRequest);
        URI uri = uriBuilder.path("/user/{userId}").buildAndExpand(user.getUserId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> detail(@PathVariable Long userId) {
        User user = userService.getUserById(userId);
        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UpdateUserRequest updateUserRequest, @PathVariable Long userId,
                                               HttpServletRequest httpRequest) throws Exception {
        User user = userService.updateUser(userId, updateUserRequest, httpRequest);
        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @PutMapping("/profile/{userId}")
    public ResponseEntity<UserResponse> updateProfile(@RequestBody @Valid UpdateProfileRequest updateProfile, @PathVariable Long userId) throws Exception {
        User user = userService.updateProfileUser(userId, updateProfile);
        return ResponseEntity.ok().body(new UserResponse(user));
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Integer> delete(@PathVariable Long userId, HttpServletRequest httpRequest) {
        userService.deleteUser(userId, httpRequest);
        return ResponseEntity.ok().build();
    }

}
