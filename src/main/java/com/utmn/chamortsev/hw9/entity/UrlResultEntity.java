package com.utmn.chamortsev.hw9.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "url_results")
@Schema(description = "Result of URL parsing operation")
@Getter
@Setter
public class UrlResultEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the result", example = "1")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "url_id", nullable = false)
    @JsonIgnore
    @Schema(description = "Reference to the parsed URL")
    private UrlEntity urlEntity;

    @Schema(description = "HTTP status code", example = "200")
    private Integer statusCode;

    @Schema(description = "Response time in milliseconds", example = "350")
    private Long responseTime;

    @Schema(description = "Extracted address information", example = "ул. Примерная, 123")
    private String address;

    @Schema(description = "Extracted phone numbers", example = "+7 (123) 456-78-90")
    private String phone;

    @Schema(description = "Extracted email addresses", example = "contact@example.com")
    private String email;

    @Schema(description = "Extracted working hours", example = "пн-пт 9:00-18:00")
    private String workingHours;

    @Schema(description = "Error message if processing failed")
    private String errorMessage;

    @Schema(description = "Processing timestamp")
    private LocalDateTime processedAt;

    @Column(length = 1000)
    private String notes;

    @PrePersist
    protected void onCreate() {
        processedAt = LocalDateTime.now();
    }

    public UrlResultEntity() {}

    public UrlResultEntity(UrlEntity urlEntity, Integer statusCode, Long responseTime) {
        this.urlEntity = urlEntity;
        this.statusCode = statusCode;
        this.responseTime = responseTime;
    }
}
