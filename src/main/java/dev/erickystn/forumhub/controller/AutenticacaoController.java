package dev.erickystn.forumhub.controller;


import dev.erickystn.forumhub.domain.usuario.DadosAutenticacao;
import dev.erickystn.forumhub.domain.usuario.Usuario;
import dev.erickystn.forumhub.infra.security.DadosTokenJWT;
import dev.erickystn.forumhub.infra.security.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
public class AutenticacaoController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity efetuarLogin(@RequestBody @Valid DadosAutenticacao dados) {
        var authenticationToken = new UsernamePasswordAuthenticationToken(dados.email(), dados.senha());//DTO do proprio Spring p user / senha
        var autenticacao = authenticationManager.authenticate(authenticationToken);

        var tokenJWT = tokenService.gerarToken((Usuario) autenticacao.getPrincipal());


        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT, "bearer"));
    }
}
