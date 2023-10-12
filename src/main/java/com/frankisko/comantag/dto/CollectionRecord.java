package com.frankisko.comantag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.frankisko.comantag.validations.PathExists;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class CollectionRecord  {

    @NotBlank(message = "Name is required")
    private String name;

    @PathExists
    private String location;

}
