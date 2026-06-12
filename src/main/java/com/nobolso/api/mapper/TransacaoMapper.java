package com.nobolso.api.mapper;

import com.nobolso.api.dto.response.TransacaoResponseDTO;
import com.nobolso.api.dto.response.TransacaoResponseDTO.EnumDTO;
import com.nobolso.api.model.Transacao;
import org.springframework.stereotype.Component;

@Component
public class TransacaoMapper {

    public TransacaoResponseDTO toResponse(Transacao t) {
        TransacaoResponseDTO.ComprovanteInfo comprovanteInfo = t.getComprovante() != null
                ? new TransacaoResponseDTO.ComprovanteInfo(
                        t.getComprovante().getNome(),
                        t.getComprovante().getContentType(),
                        "/transacoes/" + t.getId() + "/comprovante")
                : null;

        return new TransacaoResponseDTO(
                t.getId(),
                t.getValor(),
                new EnumDTO(t.getTipo().getCodigo(), t.getTipo().getDescricao()),
                new EnumDTO(t.getDirecao().getCodigo(), t.getDirecao().getDescricao()),
                t.getCategoria() != null
                        ? new EnumDTO(t.getCategoria().getCodigo(), t.getCategoria().getDescricao())
                        : null,
                t.getDescricao(),
                t.getDataTransacao(),
                t.getDataCadastro(),
                t.getDataAtualizacao(),
                comprovanteInfo
        );
    }
}
