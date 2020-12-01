package si.fri.rso.albify.albumservice.api.v1.resources;

import org.bson.types.ObjectId;
import org.glassfish.jersey.server.ContainerRequest;
import si.fri.rso.albify.albumservice.lib.Album;
import si.fri.rso.albify.albumservice.lib.Image;
import si.fri.rso.albify.albumservice.lib.ImageId;
import si.fri.rso.albify.albumservice.models.converters.AlbumConverter;
import si.fri.rso.albify.albumservice.models.entities.AlbumEntity;
import si.fri.rso.albify.albumservice.services.beans.AlbumBean;
import si.fri.rso.albify.albumservice.services.beans.ImageServiceBean;
import si.fri.rso.albify.albumservice.services.filters.Authenticate;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
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

    @Inject
    private ImageServiceBean imageServiceBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    @Path("/{albumId}")
    @Authenticate
    public Response getAlbum(@PathParam("albumId") String albumId, @Context ContainerRequest request) {
        if (!ObjectId.isValid(albumId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!entity.getUserId().toString().equals(request.getProperty("userId").toString())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        return Response.status(Response.Status.OK).entity(AlbumConverter.toDto(entity)).build();
    }

    @GET
    // @Authenticate
    public Response getAlbums() {
        List<Album> albums = albumBean.getAlbums(uriInfo);
        return Response.status(Response.Status.OK).entity(albums).build();
    }

    @POST
    @Authenticate
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
    @Authenticate
    public Response removeAlbum(@PathParam("albumId") String albumId) {
        if (!ObjectId.isValid(albumId)) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        AlbumEntity entity = albumBean.removeAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response
                .status(Response.Status.OK).entity(AlbumConverter.toDto(entity)).build();
    }

    @GET
    @Path("/{albumId}/images")
    @Authenticate
    public Response getAlbumImages(@PathParam("albumId") String albumId) {

        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        List<Image> images = imageServiceBean.getImages((AlbumConverter.toDto(entity)).getImages());
        long count = imageServiceBean.getImagesCount((AlbumConverter.toDto(entity)).getImages());
        return Response
                .status(Response.Status.OK)
                .header("X-Total-Count", count)
                .entity(images).build();
    }

    @PUT
    @Path("/{albumId}/images")
    @Authenticate
    public Response addImageToAlbum(@PathParam("albumId") String albumId, ImageId imageId, @Context ContainerRequest request) {
        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!entity.getUserId().toString().equals(request.getProperty("userId").toString())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        if (imageId == null || imageId.getId() == null) {
            return Response.status(400, "Image ID is not present.").build();
        }

        Image image = imageServiceBean.getImage(imageId.getId());
        if (image == null || image.getId() == null) {
            return Response.status(404, "Image doesn't exists.").build();
        }

        AlbumEntity updatedEntity = albumBean.addImageToAlbum(albumId, new ObjectId(imageId.getId()));
        if (updatedEntity == null) {
            return Response.status(500, "There was a problem while adding image to album.").build();
        }
        return Response.status(Response.Status.OK).entity(AlbumConverter.toDto(updatedEntity)).build();
    }

    @DELETE
    @Path("/{albumId}/images/{imageId}")
    @Authenticate
    public Response removeImageFromAlbum(@PathParam("albumId") String albumId, @PathParam("imageId") String imageId, @Context ContainerRequest request) {
        AlbumEntity entity = albumBean.getAlbum(albumId);
        if (entity == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        if (!entity.getUserId().toString().equals(request.getProperty("userId").toString())) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }

        Image image = imageServiceBean.getImage(imageId);
        if (image == null || image.getId() == null) {
            return Response.status(404, "Image doesn't exists.").build();
        }

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
