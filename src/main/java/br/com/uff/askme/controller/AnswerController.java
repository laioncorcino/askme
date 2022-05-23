package br.com.uff.askme.controller;

import br.com.uff.askme.dto.AnswerRequest;
import br.com.uff.askme.dto.AnswerResponse;
import br.com.uff.askme.dto.UpdateAnswerRequest;
import br.com.uff.askme.model.Answer;
import br.com.uff.askme.security.TokenService;
import br.com.uff.askme.service.AnswerService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;
import java.nio.file.AccessDeniedException;

import static org.springframework.data.domain.Sort.Direction.ASC;

@RestController
@AllArgsConstructor
@RequestMapping("/topic/{topicId}/answer")
public class AnswerController {

    private AnswerService answerService;
    private TokenService tokenService;

    @GetMapping
    @Cacheable(value = "answerList")
    public Page<AnswerResponse> list(@PathVariable Long topicId, @PageableDefault(sort = "id", direction = ASC) Pageable pageable) {
        return answerService.listAnswer(pageable, topicId);
    }

    @PostMapping
    @CacheEvict(value = "answerList", allEntries = true)
    public ResponseEntity<String> create(@PathVariable Long topicId, @RequestBody @Valid AnswerRequest answerRequest,
                                         UriComponentsBuilder uriBuilder, HttpServletRequest httpRequest) {

        Long userId = recuperateUserIdFromToken(httpRequest);
        Answer answer = answerService.createAnswer(answerRequest, topicId, userId);
        URI uri = uriBuilder.path("/topic/{topicId}/answer/{answerId}").buildAndExpand(topicId, answer.getAnswerId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerResponse> detail(@PathVariable Long topicId, @PathVariable Long answerId) {
        Answer answer = answerService.getAnswerById(topicId, answerId);
        return ResponseEntity.ok().body(new AnswerResponse(answer));
    }

    @PutMapping("/{answerId}")
    @CacheEvict(value = "answerList", allEntries = true)
    public ResponseEntity<AnswerResponse> update(@PathVariable Long topicId, @PathVariable Long answerId,
                                                 @RequestBody @Valid UpdateAnswerRequest updateAnswerRequest, HttpServletRequest httpRequest) throws AccessDeniedException {
        Long userId = recuperateUserIdFromToken(httpRequest);
        Answer answer = answerService.updateAnswer(topicId, answerId, updateAnswerRequest, userId);
        return ResponseEntity.ok().body(new AnswerResponse(answer));
    }

    @DeleteMapping("/{answerId}")
    @CacheEvict(value = "answerList", allEntries = true)
    public ResponseEntity<Integer> delete(@PathVariable Long topicId, @PathVariable Long answerId) {
        answerService.deleteAnswer(topicId, answerId);
        return ResponseEntity.ok().build();
    }

    private Long recuperateUserIdFromToken(HttpServletRequest httpRequest) {
        String token = tokenService.recuperateToken(httpRequest);
        return tokenService.getUserId(token);
    }

}

