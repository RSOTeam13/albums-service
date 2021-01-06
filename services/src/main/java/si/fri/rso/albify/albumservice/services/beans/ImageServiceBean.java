package si.fri.rso.albify.albumservice.services.beans;

import org.glassfish.jersey.server.ContainerRequest;
import si.fri.rso.albify.albumservice.lib.Image;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import java.util.List;
import java.util.logging.Logger;
import si.fri.rso.albify.albumservice.services.filters.OutgoingRequestFilter;;

@RequestScoped
public class ImageServiceBean {

    @Inject
    ContainerRequest request;

    private Client httpClient;
    private String baseUrl;

    private Logger log = Logger.getLogger(ImageServiceBean.class.getName());

    @PostConstruct
    private void init() {
        httpClient = ClientBuilder.newClient().register(new OutgoingRequestFilter(request));
        baseUrl = "http://image-service:8080/v1";
        // baseUrl = "http://localhost:8081/v1";
    }

    /**
     * Calls image service and returns image by ID.
     * @param imageId Image ID.
     * @return Image.
     */
    public Image getImage(String imageId) {
        try {
            System.out.println("INFOOOOO: Calling images service!");
            return httpClient
                    .target(baseUrl + "/images/" + imageId)
                    .request()
                    .get(new GenericType<Image>() {});
        } catch (Exception e) {
            System.out.println("INFOOOOO: Images service error.");
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Calls image service and returns list of images.
     * @return List of images.
     */
    public List<Image> getImages(String[] images) {
        try {
            String url = baseUrl + "/images";
            if (images.length > 0) {
                url += "?";
                for (String image : images) {
                    url += "filterIds=" + image + "&";
                }
            }

            return httpClient
                    .target(url)
                    .request()
                    .get(new GenericType<List<Image>>() {});

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
                .request()
                .get(new GenericType<Long>() {});
    }

}