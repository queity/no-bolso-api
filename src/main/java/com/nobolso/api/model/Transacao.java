package com.nobolso.api.model;

import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal valor;

    @Column(nullable = false)
    private TipoTransacao tipo;

    @Column(nullable = false)
    private DirecaoTransacao direcao;

    @Column
    private CategoriaTransacao categoria;

    @Column(length = 255)
    private String descricao;

    @Column(nullable = false)
    private LocalDateTime dataTransacao;

    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @Column
    private LocalDateTime dataAtualizacao;
}
