package dev.erickystn.forumhub.infra.security;

import com.auth0.jwt.exceptions.JWTVerificationException;
import dev.erickystn.forumhub.domain.usuario.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class SecurityFilter extends OncePerRequestFilter {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private UsuarioRepository repository;


    private String obterToken(HttpServletRequest request) {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer", "").trim();
        }

        return null;

    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        try {

            var token = obterToken(request);

            // A partir daqui se o token for nulo, vai pular pra cadeia e vai dar 403 forbbiden

            if (token != null) {
                // aqui se der erro de validacao do token, cai no catch e preenchemos o response com os dados de erro.
                var subject = tokenService.obterSubject(token); // busca o subject que definimos no JWT
                var usuario = repository.findByEmail(subject);

                var autenticacao = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(autenticacao);
            }

            filterChain.doFilter(request, response);

        } catch (JWTVerificationException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("""
                    {
                    "erro": 401,
                    "message": "%s"
                    }
                    """.formatted(e.getMessage()));
        }
    }
}
