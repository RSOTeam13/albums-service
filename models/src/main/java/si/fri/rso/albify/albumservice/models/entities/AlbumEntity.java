package si.fri.rso.albify.albumservice.models.entities;

import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.Date;

public class AlbumEntity {

    private ObjectId id;
    private ObjectId userId;
    private String name;
    private ArrayList<ObjectId> images;
    private Date createdAt;

    public ObjectId getUserId() {
        return userId;
    }

    public void setUserId(ObjectId userId) {
        this.userId = userId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

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