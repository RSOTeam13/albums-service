package si.fri.rso.albify.albumservice.models.entities;

import org.bson.types.ObjectId;

import java.util.ArrayList;

public class AlbumEntity {

    private ObjectId id;
    private String name;
    private ArrayList<ObjectId> images;

    public ArrayList<ObjectId> getImages() {
        return images;
    }

    public void setImages(ArrayList<ObjectId> images) {
        this.images = images;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}