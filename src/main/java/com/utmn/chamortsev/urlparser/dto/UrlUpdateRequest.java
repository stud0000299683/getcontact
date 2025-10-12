package com.utmn.chamortsev.urlparser.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "Запрос на обновление URL")
public class UrlUpdateRequest {

    @Schema(description = "Новое название сайта", example = "Обновленное название")
    private String name;

    @Schema(description = "Новое описание сайта", example = "Обновленное описание")
    private String description;

    @Schema(description = "Активен ли URL", example = "true")
    private Boolean active;

    public UrlUpdateRequest() {}

    public UrlUpdateRequest(String name, String description, Boolean active) {
        this.name = name;
        this.description = description;
        this.active = active;
    }

//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public Boolean getActive() { return active; }
//    public void setActive(Boolean active) { this.active = active; }
}