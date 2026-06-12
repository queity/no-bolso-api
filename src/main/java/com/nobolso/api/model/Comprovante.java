package com.nobolso.api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "comprovantes")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comprovante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Column(nullable = false)
    private byte[] bytes;

    @Column(nullable = false, length = 255)
    private String nome;

    @Column(nullable = false, length = 100)
    private String contentType;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime dataCadastro;
    
}
