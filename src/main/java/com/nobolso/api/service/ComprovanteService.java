package com.nobolso.api.service;

import com.nobolso.api.model.Comprovante;
import com.nobolso.api.repository.ComprovanteRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
public class ComprovanteService {

    private final ComprovanteRepository repository;

    public ComprovanteService(ComprovanteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Comprovante salvar(MultipartFile arquivo) {
        log.info("Salvando comprovante: {}", arquivo.getOriginalFilename());
        try {
            return repository.salvar(arquivo.getBytes(), arquivo.getOriginalFilename(), arquivo.getContentType());
        } catch (IOException e) {
            throw new IllegalArgumentException("Erro ao processar o arquivo: " + e.getMessage());
        }
    }

    @Transactional(readOnly = true)
    public Comprovante buscarPorId(Long id) {
        return repository.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Comprovante não encontrado: " + id));
    }
}
