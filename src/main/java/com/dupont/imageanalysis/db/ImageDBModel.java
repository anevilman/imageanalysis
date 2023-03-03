package com.dupont.imageanalysis.db;

import com.dupont.imageanalysis.models.Image;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "IMAGES")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageDBModel {

    public static Image mapToOutputModel(ImageDBModel dbModel) {
        return Image.builder()
                .id(dbModel.imageId)
                .label(dbModel.imageLabel)
                .objects(dbModel.imageObjects.stream()
                        .map(ObjectDBModel::getObjectName)
                        .collect(Collectors.toSet())
                )
                .build();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGES_PK")
    private Long imagesPK;

    @Column(name = "IMAGE_ID")
    private String imageId;
    @Column(name = "IMAGE_LABEL")
    private String imageLabel;
    @ManyToMany
    @JoinTable(
            name = "IMAGE_OBJECT_MAP",
            joinColumns = @JoinColumn(name = "IMAGES_PK"),
            inverseJoinColumns = @JoinColumn(name = "OBJECTS_PK")
    )
    private List<ObjectDBModel> imageObjects;
}
