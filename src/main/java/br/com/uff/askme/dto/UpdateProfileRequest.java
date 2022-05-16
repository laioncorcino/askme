package br.com.uff.askme.dto;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
public class UpdateProfileRequest {

    @NotNull(message = "role is mandatory")
    @NotBlank(message = "role is mandatory")
    @Pattern(regexp = "ROLE_ADMINISTRATOR|ROLE_USER", message = "Invalid role")
    private String role;

}
