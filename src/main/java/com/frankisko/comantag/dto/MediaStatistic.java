package com.frankisko.comantag.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class MediaStatistic  {

    private Integer idMedia;

    private String name;

    private Long size;

    private Long createdAt;

    private Long lastViewed;

    private Long viewCount;

    private Long rating;

    private Long artistsCount;

    private Long charactersCount;

    private Long groupsCount;

    private Long languagesCount;

    private Long seriesCount;

    private Long tagsCount;

    private Long typesCount;

}