package br.com.uff.askme.dto;

import br.com.uff.askme.model.Profile;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ProfileResponse {

    private String role;

    public ProfileResponse(Profile profile) {
        this.role = profile.getRole().toString();
    }

}
