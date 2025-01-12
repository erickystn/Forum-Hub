package dev.erickystn.forumhub.domain.resposta;

import java.time.LocalDateTime;

public record DadosDetalhamentoResposta(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        boolean solucao
) {
    public DadosDetalhamentoResposta(Resposta resposta) {
        this(resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.isSolucao());
    }
}
