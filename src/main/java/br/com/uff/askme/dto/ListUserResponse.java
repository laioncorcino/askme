package br.com.uff.askme.dto;

import br.com.uff.askme.model.User;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
public class ListUserResponse {

    private final Long userId;
    private final String name;
    private final String email;

    public ListUserResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
    }

    public static Page<ListUserResponse> convertList(Page<User> users) {
        return users.map(ListUserResponse::new);
    }
}
