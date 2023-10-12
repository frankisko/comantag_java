package com.frankisko.comantag.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.frankisko.comantag.dto.Catalog;
import com.frankisko.comantag.dto.LocationRecord;
import com.frankisko.comantag.entities.Location;

@Mapper
public interface LocationMapper {

    LocationRecord getById(Integer idLocation);

    List<Catalog> getCatalog(Integer idCollection);

    void insertIfNeeded(Location location);

    void deleteObsolete();

    void deleteByIdCollection(Integer idCollection);

}
