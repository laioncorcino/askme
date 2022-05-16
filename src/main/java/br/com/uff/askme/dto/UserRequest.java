package br.com.uff.askme.dto;

import br.com.uff.askme.model.User;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class UserRequest {

    @NotNull @NotEmpty
    private String name;

    @NotNull @NotEmpty
    private String email;

    @NotNull @NotEmpty
    private String password;

    @Pattern(regexp = "ROLE_ADMINISTRATOR|ROLE_USER", message = "Invalid role")
    private String role;

    public User convert() {
        return new User(name, email, password);
    }
}
