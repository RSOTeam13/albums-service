package si.fri.rso.albify.albumservice.services.beans;

import si.fri.rso.albify.albumservice.lib.Image;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
public class ImageServiceBean {
    private Client httpClient;
    private String baseUrl;

    private Logger log = Logger.getLogger(ImageServiceBean.class.getName());

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient();
        baseUrl = "http://localhost:8081/v1";
    }

    /**
     * Calls image service and returns image by ID.
     * @param imageId Image ID.
     * @return Image.
     */
    public Image getImage(String imageId) {
        return httpClient
                .target(baseUrl + "/images/" + imageId)
                .request().get(new GenericType<Image>() {
                });
    }

    /**
     * Calls image service and returns list of images.
     * @return List of images.
     */
    public List<Image> getImages(String[] images) {
        String url = baseUrl + "/images";
        if (images.length > 0) {
            url += "?";
            for (String image : images) {
                url += "filterIds=" + image + "&";
            }
        }

        return httpClient
                .target(url)
                .request().get(new GenericType<List<Image>>() {
                });
    }

    /**
     * Calls image service and returns count if images based on filter.
     * @param images Filter images.
     * @return
     */
    public Long getImagesCount(String[] images) {
        String url = baseUrl + "/images/count";
        if (images.length > 0) {
            url += "?";
            for (String image : images) {
                url += "filterIds=" + image + "&";
            }
        }

        return httpClient
                .target(url)
                .request().get(new GenericType<Long>() {
                });
    }

}