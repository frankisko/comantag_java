package com.frankisko.comantag.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.frankisko.comantag.dto.CollectionRow;
import com.frankisko.comantag.dto.CollectionRecord;
import com.frankisko.comantag.entities.Collection;

@Mapper
public interface CollectionMapper {

    List<CollectionRow> getAll();

    CollectionRecord getById(Integer idCollection);

    String getLocation(Integer idCollection);

    void insert(Collection collection);

    void update(Collection collection);

    void delete(Integer idCollection);

    void lastUpdate(Integer idCollection);

}
