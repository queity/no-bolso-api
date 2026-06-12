package com.nobolso.api.repository.mock;

import com.nobolso.api.model.Comprovante;
import com.nobolso.api.model.Transacao;
import com.nobolso.api.repository.ComprovanteRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Profile("dev")
@Repository
public class MockComprovanteRepository implements ComprovanteRepository {

    private final Map<Long, Comprovante> store = new ConcurrentHashMap<>();

    @Override
    public Comprovante salvar(Transacao transacao, byte[] bytes, String nome, String contentType) {
        Comprovante existente = store.get(transacao.getId());
        if (existente != null) {
            existente.setBytes(bytes);
            existente.setNome(nome);
            existente.setContentType(contentType);
            return existente;
        }
        Comprovante comprovante = Comprovante.builder()
                .id(transacao.getId())
                .transacao(transacao)
                .bytes(bytes)
                .nome(nome)
                .contentType(contentType)
                .dataCadastro(LocalDateTime.now())
                .build();
        store.put(transacao.getId(), comprovante);
        transacao.setComprovante(comprovante);
        return comprovante;
    }

    @Override
    public Optional<Comprovante> buscarPorTransacaoId(Long transacaoId) {
        return Optional.ofNullable(store.get(transacaoId));
    }


}
