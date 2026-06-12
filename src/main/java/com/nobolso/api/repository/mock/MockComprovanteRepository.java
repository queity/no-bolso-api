package com.nobolso.api.repository.mock;

import com.nobolso.api.model.Comprovante;
import com.nobolso.api.repository.ComprovanteRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Profile("dev")
@Repository
public class MockComprovanteRepository implements ComprovanteRepository {

    private final Map<Long, Comprovante> store = new ConcurrentHashMap<>();
    private final AtomicLong nextId = new AtomicLong(1);

    @Override
    public Comprovante salvar(byte[] bytes, String nome, String contentType) {
        Comprovante comprovante = Comprovante.builder()
                .id(nextId.getAndIncrement())
                .bytes(bytes)
                .nome(nome)
                .contentType(contentType)
                .dataCadastro(LocalDateTime.now())
                .build();
        store.put(comprovante.getId(), comprovante);
        return comprovante;
    }

    @Override
    public Optional<Comprovante> buscarPorId(Long id) {
        return Optional.ofNullable(store.get(id));
    }
}
