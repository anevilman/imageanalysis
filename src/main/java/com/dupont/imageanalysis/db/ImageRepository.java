package com.dupont.imageanalysis.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImageRepository extends CrudRepository<ImageDBModel, Long> {

    List<ImageDBModel> findUniqueByImageObjectsIn(List<ObjectDBModel> objects);
    ImageDBModel findByImageId(String imageId);
}
