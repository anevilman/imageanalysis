package com.dupont.imageanalysis.db;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "OBJECTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectDBModel {

    public static Set<String> convertCollection(Collection<ObjectDBModel> objects) {
        return Optional.ofNullable(objects).stream()
                .flatMap(Collection::stream)
                .map(ObjectDBModel::getObjectName)
                .collect(Collectors.toSet());
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBJECTS_PK")
    private Long objectsPK;

    @Column(name = "OBJECT_NAME")
    private String objectName;

    @ManyToMany(mappedBy = "imageObjects")
    private List<ImageDBModel> objectImages;

}
