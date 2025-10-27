package com.utmn.chamortsev.hw9.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на добавление URL")
public class UrlRequest {

    @Schema(description = "URL адрес", example = "https://example.com", required = true)
    private String url;

    @Schema(description = "Название сайта", example = "Пример сайта")
    private String name;

    @Schema(description = "Описание сайта", example = "Пример сайта для тестирования")
    private String description;

    public UrlRequest() {
    }

    public UrlRequest(String url, String name, String description) {
        this.url = url;
        this.name = name;
        this.description = description;
    }

//    // Getters and Setters
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public void setDescription(String description) {
//        this.description = description;
//    }
}