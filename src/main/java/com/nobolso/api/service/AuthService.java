package com.nobolso.api.service;

import com.nobolso.api.dto.request.LoginRequestDTO;
import com.nobolso.api.dto.request.RegisterRequestDTO;
import com.nobolso.api.dto.response.AuthResponseDTO;
import com.nobolso.api.model.RefreshToken;
import com.nobolso.api.model.Usuario;
import com.nobolso.api.repository.RefreshTokenRepository;
import com.nobolso.api.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiration;

    @Value("${jwt.refresh-token-expiration}")
    private long refreshTokenExpiration;

    public AuthResponseDTO registrar(RegisterRequestDTO dto) {
        if (usuarioRepository.existePorEmail(dto.email())) {
            throw new IllegalArgumentException("E-mail já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(dto.nome())
                .email(dto.email())
                .senha(passwordEncoder.encode(dto.senha()))
                .build();

        usuario = usuarioRepository.salvar(usuario);
        log.info("Novo usuário registrado: {}", usuario.getEmail());

        return gerarAuth(usuario);
    }

    public AuthResponseDTO login(LoginRequestDTO dto) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.email(), dto.senha())
        );

        Usuario usuario = usuarioRepository.buscarPorEmail(dto.email())
                .orElseThrow(() -> new IllegalStateException("Usuário não encontrado"));

        refreshTokenRepository.revogarPorUsuario(usuario.getId());
        log.info("Login realizado: {}", usuario.getEmail());

        return gerarAuth(usuario);
    }

    public AuthResponseDTO refresh(String refreshTokenStr) {
        RefreshToken refreshToken = refreshTokenRepository.buscarPorToken(refreshTokenStr)
                .orElseThrow(() -> new IllegalArgumentException("Refresh token inválido"));

        if (refreshToken.isRevogado()) {
            throw new IllegalArgumentException("Refresh token revogado");
        }

        if (refreshToken.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refresh token expirado");
        }

        Usuario usuario = refreshToken.getUsuario();
        refreshTokenRepository.revogarPorUsuario(usuario.getId());

        return gerarAuth(usuario);
    }

    public void logout(String refreshTokenStr) {
        refreshTokenRepository.buscarPorToken(refreshTokenStr)
                .ifPresent(rt -> refreshTokenRepository.revogarPorUsuario(rt.getUsuario().getId()));
    }

    private AuthResponseDTO gerarAuth(Usuario usuario) {
        String accessToken = jwtService.gerarToken(usuario);
        String refreshTokenStr = UUID.randomUUID().toString();

        RefreshToken refreshToken = RefreshToken.builder()
                .token(refreshTokenStr)
                .usuario(usuario)
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenExpiration / 1000))
                .build();

        refreshTokenRepository.salvar(refreshToken);

        return new AuthResponseDTO(
                accessToken,
                refreshTokenStr,
                accessTokenExpiration / 1000,
                usuario.getNome(),
                usuario.getEmail()
        );
    }
}
