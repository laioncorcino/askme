package br.com.uff.askme.unit.security;

import br.com.uff.askme.error.exception.BadRequestException;
import br.com.uff.askme.security.AuthenticationController;
import br.com.uff.askme.security.AuthenticationService;
import br.com.uff.askme.security.TokenService;
import br.com.uff.askme.service.UserService;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = AuthenticationController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthenticationService authService;

    @Test
    @DisplayName("deve_retornar_bad_request_caso_dados_da_autenticacao_estejam_errados")
    public void deve_retornar_bad_request_caso_dados_da_autenticacao_estejam_errados() throws Exception {
        Mockito.when(authService.authenticateUser(any())).thenThrow(new BadRequestException("Usuário ou senha incorretos"));

        JSONObject json = new JSONObject();
        json.put("email", "invalido@email.com");
        json.put("password", "123456");

        mockMvc.perform(post("/login")
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));

    }

    @Test
    @DisplayName("deve_autenticar_usuario_com_sucesso")
    public void deve_autenticar_usuario_com_sucesso() throws Exception {
        Mockito.when(authService.authenticateUser(any())).thenReturn("validToken");

        JSONObject json = new JSONObject();
        json.put("email", "invalido@email.com");
        json.put("password", "123456");

        mockMvc.perform(post("/login")
                        .content(json.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(HttpStatus.OK.value()));

    }

}