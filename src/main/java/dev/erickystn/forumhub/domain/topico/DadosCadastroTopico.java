package dev.erickystn.forumhub.domain.topico;

import dev.erickystn.forumhub.domain.usuario.Usuario;
import jakarta.validation.constraints.NotBlank;

public record DadosCadastroTopico(
        @NotBlank
        String titulo,
        @NotBlank
        String mensagem,
        @NotBlank
        String curso,

        Usuario autor
) {
}
