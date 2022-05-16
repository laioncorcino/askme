package br.com.uff.askme.dto;

import br.com.uff.askme.model.User;
import lombok.Getter;

@Getter
public class UpdateUserRequest {

    private String name;
    private String email;
    private String oldPassword;
    private String newPassword;

    public User convert() {
        return new User(name, email, newPassword);
    }
}
