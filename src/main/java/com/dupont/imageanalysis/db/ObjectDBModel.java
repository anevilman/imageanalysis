package com.dupont.imageanalysis.db;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "OBJECTS")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectDBModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OBJECTS_PK")
    private Long objectsPK;

    @Column(name = "OBJECT_NAME")
    private String objectName;

    @ManyToMany(mappedBy = "imageObjects")
    private List<ImageDBModel> objectImages;

}
