package com.frankisko.comantag.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.frankisko.comantag.config.Globals;
import com.frankisko.comantag.dao.MetadataMapper;
import com.frankisko.comantag.dto.Catalog;
import com.frankisko.comantag.dto.MetadataRecord;
import com.frankisko.comantag.dto.MetadataRow;
import com.frankisko.comantag.entities.Collection;
import com.frankisko.comantag.entities.Metadata;

@Service
public class MetadataService {

    private final MediaMetadataService mediaMetadataService;

    private final MetadataMapper metadataMapper;

    private final Globals globals;

    public MetadataService(MediaMetadataService mediaMetadataService, MetadataMapper metadataMapper, Globals globals) {
        this.mediaMetadataService = mediaMetadataService;
        this.metadataMapper = metadataMapper;
        this.globals = globals;
    }

    public List<MetadataRow> getAllByType(String type) {
        return metadataMapper.getAllByType(globals.getIdCollection(), type);
    }

    public List<Catalog> getCatalogByType(String type) {
        return metadataMapper.getCatalogByType(globals.getIdCollection(), type);
    }

    public void insert(MetadataRecord record) {
        Long epochSeconds = System.currentTimeMillis() / 1000;

        Collection collection = new Collection();
        collection.setIdCollection(globals.getIdCollection());

        Metadata metadata = new Metadata();
        metadata.setName(record.getName());
        metadata.setType(record.getType());
        metadata.setCollection(collection);
        metadata.setCreatedAt(epochSeconds);
        metadata.setUpdatedAt(epochSeconds);

        metadataMapper.insert(metadata);
    }

    public void insertIfNeeded(String name, String type) {
        metadataMapper.insertIfNeeded(globals.getIdCollection(), name, type);
    }

    public MetadataRecord getById(Integer idMetadata) {
        return metadataMapper.getById(idMetadata);
    }

    public void update(Integer idMetadata, MetadataRecord record) {
        Metadata metadata = new Metadata();
        metadata.setIdMetadata(idMetadata);
        metadata.setName(record.getName());
        metadata.setUpdatedAt(System.currentTimeMillis() / 1000);

        metadataMapper.update(metadata);
    }

    public void delete(Integer idMetadata) {
        mediaMetadataService.deleteByIdMetadata(idMetadata);
        metadataMapper.delete(idMetadata);
    }

    public void deleteByIdCollection(Integer idCollection) {
        metadataMapper.deleteByIdCollection(idCollection);
    }

}