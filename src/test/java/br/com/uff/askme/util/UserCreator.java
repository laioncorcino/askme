package br.com.uff.askme.util;

import br.com.uff.askme.model.User;

public class UserCreator {

    public static User userLaion() {
        return User.builder()
                .userId(1L)
                .name("Laion Corcino")
                .email("laion@email.com")
                .build();
    }

    public static User userPaula() {
        return User.builder()
                .userId(1L)
                .name("Paula Corcino")
                .email("paula@email.com")
                .build();
    }

    public static User userPaulaSouza() {
        return User.builder()
                .userId(1L)
                .name("Paula de Souza")
                .email("paula@email.com")
                .build();
    }

    public static User userEdna() {
        return User.builder()
                .userId(1L)
                .name("Edna Corcino")
                .email("edna@email.com")
                .build();
    }

    public static User userNilson() {
        return User.builder()
                .userId(1L)
                .name("Nilson Faria")
                .email("nilson@email.com")
                .build();
    }

}
