package poc.camel.eip.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

/**
 * A POC Route.
 */
@Component
public final class POCRoute extends RouteBuilder {

    /** The Constant ROUTE_NAME. */
    public static final String ROUTE_NAME = "POC_ROUTE";

    /** The Constant ROUTE_FROM. */
    public static final String ROUTE_FROM = "timer:"
            + POCRoute.class.getSimpleName() + "?period=10s";

    /** The Constant ROUTE_TO. */
    public static final String ROUTE_TO = "log:" + POCRoute.class.getName();

    @Override
    public void configure() throws Exception {

        from(ROUTE_FROM).routeId(ROUTE_NAME).to(ROUTE_TO);

    }
}
