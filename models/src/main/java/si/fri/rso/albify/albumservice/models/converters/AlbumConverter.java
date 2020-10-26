package si.fri.rso.albify.albumservice.models.converters;

import org.bson.types.ObjectId;
import si.fri.rso.albify.albumservice.lib.Album;
import si.fri.rso.albify.albumservice.models.entities.AlbumEntity;

public class AlbumConverter {

    public static Album toDto(AlbumEntity entity) {

        Album dto = new Album();
        dto.setId(entity.getId().toString());
        dto.setName(entity.getName());

        return dto;

    }

    public static AlbumEntity toEntity(Album dto) {

        AlbumEntity entity = new AlbumEntity();
        entity.setName(dto.getName());

        return entity;

    }

}
