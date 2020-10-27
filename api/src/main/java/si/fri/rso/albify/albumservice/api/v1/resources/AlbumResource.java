package si.fri.rso.albify.albumservice.api.v1.resources;

import org.bson.types.ObjectId;
import si.fri.rso.albify.albumservice.lib.Album;
import si.fri.rso.albify.albumservice.lib.ImageId;
import si.fri.rso.albify.albumservice.models.converters.AlbumConverter;
import si.fri.rso.albify.albumservice.models.entities.AlbumEntity;
import si.fri.rso.albify.albumservice.services.beans.AlbumBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/albums")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AlbumResource {

    private Logger log = Logger.getLogger(AlbumResource.class.getName());

    @Inject
    private AlbumBean albumBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("/{albumId}")
    public Response getAlbum(@PathParam("albumId") String albumId) {
        if (!ObjectId.isValid(albumId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        return Response.status(Response.Status.OK).entity(AlbumConverter.toDto(entity)).build();
    }

    @GET
    public Response getAlbums() {
        List<Album> albums = albumBean.getAlbums(uriInfo);
        return Response.status(Response.Status.OK).entity(albums).build();
    }

    @POST
    public Response createAlbum(Album album) {
        if (album == null || album.getName() == null) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        } else {
            AlbumEntity entity = albumBean.createAlbum(album);
            return Response.status(Response.Status.CREATED).entity(AlbumConverter.toDto(entity)).build();
        }
    }

    @DELETE
    @Path("/{albumId}")
    public Response removeAlbum(@PathParam("albumId") String albumId) {
        if (!ObjectId.isValid(albumId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AlbumEntity entity = albumBean.removeAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(AlbumConverter.toDto(entity)).build();
    }

    @PUT
    @Path("/{albumId}/images")
    public Response addImageToAlbum(@PathParam("albumId") String albumId, ImageId imageId) {
        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (imageId == null || imageId.getId() == null) {
            return Response.status(400, "Image ID is not present.").build();
        }

        // TODO: Check if image exists.
        AlbumEntity updatedEntity = albumBean.addImageToAlbum(albumId, new ObjectId(imageId.getId()));
        if (updatedEntity == null) {
            return Response.status(500, "There was a problem while adding image to album.").build();
        }
        return Response.status(Response.Status.OK).entity(AlbumConverter.toDto(updatedEntity)).build();
    }

    @DELETE
    @Path("/{albumId}/images/{imageId}")
    public Response removeImageFromAlbum(@PathParam("albumId") String albumId, @PathParam("imageId") String imageId) {
        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // TODO: Check if image exist.
        if (!entity.getImages().contains(new ObjectId(imageId))) {
            return Response.status(400, "Image is not in provided album.").build();
        }

        AlbumEntity updatedEntity = albumBean.removeImageFromAlbum(albumId, new ObjectId(imageId));
        if (updatedEntity == null) {
            return Response.status(500, "There was a problem while removing image from album.").build();
        }
        return Response.status(Response.Status.OK).entity(AlbumConverter.toDto(updatedEntity)).build();
    }
}
