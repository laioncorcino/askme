package br.com.uff.askme.security;

import br.com.uff.askme.dto.LoginRequest;
import br.com.uff.askme.dto.TokenResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/login")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping
    public ResponseEntity<TokenResponse> autenticate(@RequestBody @Valid LoginRequest loginRequest) {
        String token = authenticationService.authenticateUser(loginRequest);
        return ResponseEntity.ok(new TokenResponse("Bearer", token));
    }

}
