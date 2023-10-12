package com.frankisko.comantag.entities;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class Media {

    private Integer idMedia;

    @NotBlank
    private String name;

    private Location location;

    private Long size;

    private Integer pages;

    private Integer scrapped;

    private Collection collection;

    private Long lastViewed;

    private Integer viewCount;

    private Long createdAt;

    private Long updatedAt;

    private Integer rating;

    private String url;

}
