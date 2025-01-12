package dev.erickystn.forumhub.controller;


import dev.erickystn.forumhub.domain.topico.*;
import dev.erickystn.forumhub.domain.usuario.UsuarioRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    TopicoRepository topicoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping
    @Transactional
    public ResponseEntity cadastrar(@RequestBody @Valid DadosCadastroTopico dados, UriComponentsBuilder uriBuilder) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println(authentication.getName());
        var usuario = usuarioRepository.findByEmail(authentication.getName());

        if(topicoRepository.existsByTituloOrMensagem(dados.titulo().trim(), dados.mensagem().trim())){
            return ResponseEntity.badRequest().body("Já existe um tópico com esse titulo ou conteudo");
        }


        var topico = topicoRepository.save(new Topico(dados, usuario));

        URI uri = uriBuilder
                .path("/pacientes/{id}")
                .buildAndExpand(topico.getId())
                .toUri();
        return ResponseEntity.created(uri).body(new DadosDetalhamentoTopico(topico));

    }

    @GetMapping
    public ResponseEntity<Page<DadosListagemTopico>> lista(
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.ASC) Pageable paginacao) {
        var topicos = topicoRepository.findAll(paginacao)
                .map(DadosListagemTopico::new);

        return ResponseEntity.ok(topicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<DadosDetalhamentoTopico> detalha(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        if (Optional.ofNullable(topico).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DadosDetalhamentoTopico> atualiza(@RequestBody @Valid DadosCadastroTopico dados, @PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        topico.atualizarInformacoes(dados);

        return ResponseEntity.ok(new DadosDetalhamentoTopico(topico));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity exclui(@PathVariable Long id) {
        var topico = topicoRepository.getReferenceById(id);
        if (Optional.ofNullable(topico).isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        topicoRepository.delete(topico);
        return ResponseEntity.noContent().build();
    }

}
