package dev.erickystn.forumhub.domain.topico;

import dev.erickystn.forumhub.domain.resposta.DadosDetalhamentoResposta;

import java.time.LocalDateTime;
import java.util.List;

public record DadosDetalhamentoTopico(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        String nomeAutor,
        List<DadosDetalhamentoResposta> respostas
) {
    public DadosDetalhamentoTopico(Topico topico) {
        this(topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getUsuario().getNome(),
                topico.getRespostas().stream().map(DadosDetalhamentoResposta::new).toList()
        );
    }
}
