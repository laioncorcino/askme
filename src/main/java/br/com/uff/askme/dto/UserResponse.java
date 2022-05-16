package br.com.uff.askme.dto;

import br.com.uff.askme.model.User;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class UserResponse {

    private final Long userId;
    private final String name;
    private final String email;
    private final List<ListTopicResponse> topics = new ArrayList<>();
    private final List<AnswerResponse> answersMade = new ArrayList<>();
    private final List<ProfileResponse> profiles = new ArrayList<>();

    public UserResponse(User user) {
        this.userId = user.getUserId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.topics.addAll(convertTopicList(user));
        this.answersMade.addAll(convertAnswerList(user));
        this.profiles.addAll(convertRoles(user));
    }

    private Collection<? extends ListTopicResponse> convertTopicList(User user) {
        return user.getTopics()
                    .stream()
                    .map(ListTopicResponse::new)
                    .collect(Collectors.toList())
        ;
    }

    private Collection<? extends AnswerResponse> convertAnswerList(User user) {
        return user.getAnswersMade()
                    .stream()
                    .map(AnswerResponse::new)
                    .collect(Collectors.toList())
        ;
    }

    private Collection<? extends ProfileResponse> convertRoles(User user) {
        return user.getProfiles()
                .stream()
                .map(ProfileResponse::new)
                .collect(Collectors.toList())
        ;
    }

}
