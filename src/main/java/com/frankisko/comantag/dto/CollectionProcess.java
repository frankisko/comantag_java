package com.frankisko.comantag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class CollectionProcess {

    private Integer idCollection;

    private Float progress;

    private String name;

    private Integer left;

}