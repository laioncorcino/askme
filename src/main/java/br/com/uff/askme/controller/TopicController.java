package br.com.uff.askme.controller;

import br.com.uff.askme.dto.ListTopicResponse;
import br.com.uff.askme.dto.TopicRequest;
import br.com.uff.askme.dto.TopicResponse;
import br.com.uff.askme.dto.UpdateTopicRequest;
import br.com.uff.askme.model.Topic;
import br.com.uff.askme.security.TokenService;
import br.com.uff.askme.service.TopicService;
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
@RequestMapping("/topic")
@AllArgsConstructor
public class TopicController {

    private TopicService topicService;
    private TokenService tokenService;

    @GetMapping
    public Page<ListTopicResponse> list(@RequestParam(required = false) String courseName,
                                        @PageableDefault(sort = "topicId", direction = ASC) Pageable pageable) {
        return topicService.listTopics(courseName, pageable);
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid TopicRequest topicRequest, UriComponentsBuilder uriBuilder,
                                         HttpServletRequest httpRequest) throws Exception {
        Long userId = recuperateUserIdFromToken(httpRequest);
        Topic topic = topicService.createTopic(topicRequest, userId);
        URI uri = uriBuilder.path("/topic/{id}").buildAndExpand(topic.getTopicId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{topicId}")
    public ResponseEntity<TopicResponse> detail(@PathVariable Long topicId) {
        Topic topic = topicService.getTopicById(topicId);
        return ResponseEntity.ok().body(new TopicResponse(topic));
    }

    @PutMapping("/{topicId}")
    public ResponseEntity<TopicResponse> update(@RequestBody @Valid UpdateTopicRequest updateTopicRequest,
                                                @PathVariable Long topicId, HttpServletRequest httpRequest) throws Exception {

        Long userId = recuperateUserIdFromToken(httpRequest);
        Topic topic = topicService.updateTopic(topicId, updateTopicRequest, userId);
        return ResponseEntity.ok().body(new TopicResponse(topic));
    }

    @DeleteMapping("/{topicId}")
    public ResponseEntity<Integer> delete(@PathVariable Long topicId) {
        topicService.deleteTopic(topicId);
        return ResponseEntity.ok().build();
    }

    private Long recuperateUserIdFromToken(HttpServletRequest httpRequest) {
        String token = tokenService.recuperateToken(httpRequest);
        return tokenService.getUserId(token);
    }

}
