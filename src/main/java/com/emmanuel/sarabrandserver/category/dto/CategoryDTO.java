package com.emmanuel.sarabrandserver.category.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.json.Json;
import javax.json.JsonObject;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CategoryDTO {

    @NotNull(message = "name cannot be null")
    @NotEmpty(message = "name cannot be empty")
    @Size(max = 50, message = "category name cannot exceed length of 50")
    private String name;

    @NotNull(message = "visible cannot be null")
    private Boolean visible;

    @NotNull(message = "parent cannot be null")
    private String parent;

    public JsonObject toJson() {
        return Json.createObjectBuilder()
                .add("name", getName())
                .add("visible", getVisible())
                .add("parent", getParent())
                .build();
    }
}
