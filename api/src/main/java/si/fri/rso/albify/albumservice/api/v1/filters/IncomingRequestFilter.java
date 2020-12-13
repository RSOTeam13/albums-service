package si.fri.rso.albify.albumservice.api.v1.filters;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import java.util.UUID;

@Provider
@ApplicationScoped
public class IncomingRequestFilter implements ContainerRequestFilter {

    @Override
    public void filter(ContainerRequestContext ctx)  {
        String applicationRequestId = ctx.getHeaderString("x-request-id");
        if (applicationRequestId == null) {
            ctx.getHeaders().add("x-request-id", UUID.randomUUID().toString());
        }
    }
}
