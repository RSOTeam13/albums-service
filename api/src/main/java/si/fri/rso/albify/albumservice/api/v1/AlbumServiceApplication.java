package si.fri.rso.albify.albumservice.api.v1;

import si.fri.rso.albify.albumservice.api.v1.filters.IncomingRequestFilter;
import si.fri.rso.albify.albumservice.api.v1.resources.AlbumResource;
import si.fri.rso.albify.albumservice.services.filters.AuthenticationFilter;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/v1")
public class AlbumServiceApplication extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        final Set<Class<?>> resources = new HashSet<Class<?>>();
        resources.add(IncomingRequestFilter.class);
        resources.add(AlbumResource.class);
        resources.add(AuthenticationFilter.class);

        return resources;
    }

}
