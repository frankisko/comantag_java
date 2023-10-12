package com.frankisko.comantag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class GalleryMedia {

    private Integer idMedia;

    private Integer idLocation;

    private String location;

    private String name;

    private Long size;

    private String humanSize;

    private Integer pages;

    private Integer rating;

    private Integer scrapped;

    private Long createdAt;

    private Long lastViewed;

    private Integer viewCount;

    private Integer idCollection;

    private String collectionLocation;

    private Integer artistsCount;

    private Integer charactersCount;

    private Integer groupsCount;

    private Integer languagesCount;

    private Integer seriesCount;

    private Integer tagsCount;

    private Integer typesCount;

}
