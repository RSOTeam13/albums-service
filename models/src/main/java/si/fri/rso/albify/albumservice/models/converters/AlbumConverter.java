package si.fri.rso.albify.albumservice.models.converters;

import org.bson.types.ObjectId;
import si.fri.rso.albify.albumservice.lib.Album;
import si.fri.rso.albify.albumservice.models.entities.AlbumEntity;

import java.util.ArrayList;
import java.util.List;

public class AlbumConverter {

    public static Album toDto(AlbumEntity entity) {

        Album dto = new Album();
        dto.setId(entity.getId().toString());
        dto.setUserId(entity.getUserId().toString());
        dto.setName(entity.getName());
        dto.setCreatedAt(entity.getCreatedAt());

        List<String> parsedImages = new ArrayList<>();
        for (ObjectId image : entity.getImages()) {
            parsedImages.add(image.toString());
        }
        dto.setImages(parsedImages.toArray(new String[0]));

        return dto;

    }

    public static AlbumEntity toEntity(Album dto) {

        AlbumEntity entity = new AlbumEntity();
        entity.setName(dto.getName());

        return entity;

    }

}
