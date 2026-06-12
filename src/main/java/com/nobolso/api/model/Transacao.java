package com.nobolso.api.model;

import com.nobolso.api.model.enums.CategoriaTransacao;
import com.nobolso.api.model.enums.DirecaoTransacao;
import com.nobolso.api.model.enums.TipoTransacao;
import jakarta.persistence.*;
import lombok.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transacoes")
@EntityListeners(AuditingEntityListener.class)
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

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;

    @LastModifiedDate
    @Column
    private LocalDateTime dataAtualizacao;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comprovante_id")
    private Comprovante comprovante;
    
}
