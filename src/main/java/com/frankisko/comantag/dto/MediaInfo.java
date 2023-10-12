package com.frankisko.comantag.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class MediaInfo {

    private Integer idMedia;

    private String location;

    private String name;

    private Long size;

    private String humanSize;

    private Integer pages;

    private Integer scrapped;

    private Long createdAt;

    private Long lastViewed;

    private Integer viewCount;

    private Integer idCollection;

    private String collectionName;

    private String url;

    private List<Catalog> artistsMetadata;

    private List<Catalog> charactersMetadata;

    private List<Catalog> groupsMetadata;

    private List<Catalog> languagesMetadata;

    private List<Catalog> seriesMetadata;

    private List<Catalog> tagsMetadata;

    private List<Catalog> typesMetadata;

}
