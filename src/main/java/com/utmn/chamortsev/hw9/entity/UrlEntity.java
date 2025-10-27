package com.utmn.chamortsev.hw9.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "urls")
@Schema(description = "Введенный URL для парсинга контактов")
public class UrlEntity {

    //@OneToMany(mappedBy = "urlEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    @OneToMany(mappedBy = "urlEntity", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UrlResultEntity> results = new ArrayList<>();


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Уникальный идентификатор для URL", example = "1")
    private Long id;

    @Column(nullable = false, unique = true)
    @Schema(description = "Полный URL сайта", example = "https://example.com")
    private String url;

    @Schema(description = "Отображаемое имя сайта", example = "Тестовый сайт")
    private String name;

    @Schema(description = "Описание сайта", example = "Пример тестового сайта")
    private String description;

    @Schema(description = "Время создания записи")
    private LocalDateTime createdAt;

    @Schema(description = "Доступность сайта к обработке", example = "true")
    private boolean active = true;
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public UrlEntity() {}

    public UrlEntity(String url, String name) {
        this.url = url;
        this.name = name;
    }

    public UrlEntity(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }
}