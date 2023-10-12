package com.frankisko.comantag.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import com.frankisko.comantag.dto.MediaInfo;
import com.frankisko.comantag.dto.MediaDB;
import com.frankisko.comantag.dto.MediaStatistic;
import com.frankisko.comantag.dto.MediaToScrap;
import com.frankisko.comantag.dto.GalleryMedia;
import com.frankisko.comantag.entities.Media;

@Mapper
public interface MediaMapper {

    List<MediaStatistic> getStatistics(Integer idCollection);

    MediaInfo getInfo(Integer idMedia);

    List<MediaDB> getMediaDB(Integer idCollection);

    void view(Integer idMedia);

    List<GalleryMedia> getGalleryMedia(Integer idCollection, Map<String, Object> filters);

    void rating(Media media);

    void scrapMedia(Integer idMedia);

    void scrapMediaBulk(List<Integer> idsMedia);

    void unscrapMedia(Integer idMedia);

    void unscrapAll(Integer idCollection);

    void delete(Integer idMedia);

    void deleteByIdCollection(Integer idCollection);

    void insert(Media media);

    Integer countMediaInCollection(Integer idCollection);

    Integer countPendingMediaInCollection(Integer idCollection);

    MediaToScrap getMediaToScrap(Integer idCollection);

    void markAsProcessed(Media media);

}
