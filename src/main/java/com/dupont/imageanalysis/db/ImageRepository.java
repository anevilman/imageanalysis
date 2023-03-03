package com.dupont.imageanalysis.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends CrudRepository<ImageDBModel, Long> {

    List<ImageDBModel> findByImageObjectsIn(List<ObjectDBModel> objects);
    Optional<ImageDBModel> findByImageId(String imageId);
}
