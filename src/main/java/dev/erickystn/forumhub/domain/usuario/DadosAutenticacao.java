package dev.erickystn.forumhub.domain.usuario;

import com.fasterxml.jackson.annotation.JsonAlias;
import jakarta.validation.constraints.NotBlank;

public record DadosAutenticacao(
        @NotBlank @JsonAlias("login") String email,
        @NotBlank String senha
) {
}
