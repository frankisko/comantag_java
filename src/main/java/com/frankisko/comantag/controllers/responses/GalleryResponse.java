package com.frankisko.comantag.controllers.responses;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.frankisko.comantag.dto.GalleryLocation;

import lombok.Data;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@Data
@ToString
public class GalleryResponse {

    private List<GalleryLocation> rows;

    private Integer mediaCount;

}