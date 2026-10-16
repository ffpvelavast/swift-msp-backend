package com.rex.commodis.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "translations_client", schema = "rexbase")
@Getter
@Setter
public class TranslationClient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "platform_id", nullable = false)
    private Long platformId;

    @Column(nullable = false)
    private String component;

    @Column(nullable = false)
    private String key;

    @Column(columnDefinition = "jsonb", nullable = false)
    private String translations;

    @Column(name = "samecontent_id")
    private Long samecontentId;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
