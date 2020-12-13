package si.fri.rso.albify.albumservice.services.filters;

import org.glassfish.jersey.server.ContainerRequest;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class OutgoingRequestFilter implements ClientRequestFilter {

    private final ContainerRequest containerRequest;

    public OutgoingRequestFilter(ContainerRequest containerRequest) {
        this.containerRequest = containerRequest;
    }

    @Override
    public void filter(ClientRequestContext ctx)  {
        ctx.getHeaders().add("x-request-id", containerRequest.getHeaderString("x-request-id"));
        ctx.getHeaders().add("Authorization", containerRequest.getHeaderString("Authorization"));
    }
}
