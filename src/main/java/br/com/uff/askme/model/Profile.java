package br.com.uff.askme.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static br.com.uff.askme.model.Role.ROLE_USER;

@Entity
@Data
public class Profile implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;

    @Enumerated(EnumType.STRING)
    private Role role = ROLE_USER;

    @JsonIgnore
    @ManyToMany(mappedBy = "profiles")
    private final List<User> users = new ArrayList<>();

    @Override
    public String getAuthority() {
        return role.toString();
    }

    public void addUser(User user) {
        this.users.add(user);
    }
}
