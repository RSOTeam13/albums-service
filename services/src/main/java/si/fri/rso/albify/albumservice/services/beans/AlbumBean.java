package si.fri.rso.albify.albumservice.services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.*;
import com.mongodb.client.model.FindOneAndUpdateOptions;
import com.mongodb.client.model.ReturnDocument;
import com.mongodb.client.result.InsertOneResult;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.types.ObjectId;
import si.fri.rso.albify.albumservice.lib.Album;
import si.fri.rso.albify.albumservice.models.converters.AlbumConverter;
import si.fri.rso.albify.albumservice.models.entities.AlbumEntity;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.mongodb.ConnectionString;
import static com.mongodb.client.model.Filters.eq;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;

@RequestScoped
public class AlbumBean {

    private Logger log = Logger.getLogger(AlbumBean.class.getName());

    private CodecRegistry pojoCodecRegistry = fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                fromProviders(PojoCodecProvider.builder().automatic(true).build()));

    private MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(new ConnectionString("mongodb://localhost:27017/rso"))
                .codecRegistry(pojoCodecRegistry)
                .build();


    private MongoClient mongoClient = MongoClients.create(settings);
    private MongoDatabase database = mongoClient.getDatabase("albify");
    private MongoCollection<AlbumEntity> albumsCollection = database.getCollection("albums", AlbumEntity.class);

    /**
     * Returns album by its ID.
     * @param albumId Album ID.
     * @return Album entity.
     */
    public AlbumEntity getAlbum(String albumId) {
        try {
            AlbumEntity entity = albumsCollection.find(eq("_id", new ObjectId(albumId))).first();
            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns list of albums.
     * @param uriInfo Filtering parameters.
     * @return List of albums.
     */
    public List<Album> getAlbums(UriInfo uriInfo) {
        QueryParameters queryParameters = QueryParameters.query(uriInfo
                .getRequestUri()
                .getQuery())
                .defaultOffset(0)
                .defaultLimit(100)
                .maxLimit(100)
                .build();

        try {
            return albumsCollection
                    .find()
                    .limit(queryParameters.getLimit().intValue())
                    .skip(queryParameters.getOffset().intValue())
                    .into(new ArrayList<>())
                    .stream()
                    .map(AlbumConverter::toDto)
                    .collect(Collectors.toList());

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates new album.
     * @param album to create.
     * @return Newly created album.
     */
    public AlbumEntity createAlbum(Album album) {
        try {
            AlbumEntity entity = AlbumConverter.toEntity(album);
            entity.setImages(new ArrayList<>());
            entity.setCreatedAt(new Date());

            InsertOneResult result = albumsCollection.insertOne(entity);
            if (result != null) {
                return entity;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes album.
     * @param albumId Album ID.
     * @return Deleted album.
     */
    public AlbumEntity removeAlbum(String albumId) {
        try {
            AlbumEntity entity = albumsCollection.findOneAndDelete(eq("_id", new ObjectId(albumId)));
            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Adds image to album.
     * @param albumId Album ID.
     * @param imageId ID of the images to add to album.
     * @return Updated album.
     */
    public AlbumEntity addImageToAlbum(String albumId, ObjectId imageId) {
        try {
            AlbumEntity entity = albumsCollection.findOneAndUpdate(
                    eq("_id", new ObjectId(albumId)),
                    new BasicDBObject("$addToSet", new BasicDBObject("images", imageId)),
                    new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            );

            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Removes image from the album.
     * @param albumId Album ID.
     * @param imageId ID of the image to remove from album.
     * @return Updated album.
     */
    public AlbumEntity removeImageFromAlbum(String albumId, ObjectId imageId) {
        try {
            AlbumEntity entity = albumsCollection.findOneAndUpdate(
                    eq("_id", new ObjectId(albumId)),
                    new BasicDBObject("$pull", new BasicDBObject("images", imageId)),
                    new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER)
            );

            if (entity != null && entity.getId() != null) {
                return entity;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
