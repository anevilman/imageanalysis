package com.dupont.imageanalysis.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface ObjectRepository extends CrudRepository<ObjectDBModel, Long> {

    List<ObjectDBModel> findByObjectNameIn(Set<String> objects);

}
