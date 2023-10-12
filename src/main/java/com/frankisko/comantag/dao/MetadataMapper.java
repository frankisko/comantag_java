package com.frankisko.comantag.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.frankisko.comantag.dto.Catalog;
import com.frankisko.comantag.dto.MetadataRecord;
import com.frankisko.comantag.dto.MetadataRow;
import com.frankisko.comantag.entities.Metadata;

@Mapper
public interface MetadataMapper {
    List<MetadataRow> getAllByType(Integer idCollection, String type);

    List<Catalog> getCatalogByType(Integer idCollection, String type);

    MetadataRecord getById(Integer idMetadata);

    void insert(Metadata metadata);

    void insertIfNeeded(Integer idCollection, String name, String type);

    void update(Metadata metadata);

    void delete(Integer idMetadata);

    void deleteByIdCollection(Integer idCollection);

}