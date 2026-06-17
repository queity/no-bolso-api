package com.nobolso.api.controller;

import com.nobolso.api.dto.response.EnumsResponseDTO;
import com.nobolso.api.dto.response.EnumsResponseDTO.EnumOptionDTO;
import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/enums")
@Tag(name = "Enums", description = "Valores possíveis para os campos enumerados")
public class EnumsController {

    @GetMapping
    @Operation(summary = "Listar todos os enums", description = "Retorna os valores possíveis de tipo, direção e categoria de transação")
    public ResponseEntity<EnumsResponseDTO> listar() {
        List<EnumOptionDTO> tipos = Arrays.stream(TipoTransacao.values())
                .map(e -> new EnumOptionDTO(e.getCodigo(), e.getDescricao()))
                .toList();

        List<EnumOptionDTO> direcoes = Arrays.stream(DirecaoTransacao.values())
                .map(e -> new EnumOptionDTO(e.getCodigo(), e.getDescricao()))
                .toList();

        List<EnumOptionDTO> categorias = Arrays.stream(CategoriaTransacao.values())
                .map(e -> new EnumOptionDTO(e.getCodigo(), e.getDescricao()))
                .toList();

        return ResponseEntity.ok(new EnumsResponseDTO(tipos, direcoes, categorias));
    }
}
