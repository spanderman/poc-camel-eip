package poc.camel.eip.routes.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.NotifyBuilder;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import poc.camel.eip.routes.POCRoute;

/**
 * The Class POCRouteTest.
 */
@RunWith(CamelSpringBootRunner.class)
@SpringBootTest
public class POCRouteTest {

    /** The camel context. */
    @Autowired
    private CamelContext camelContext;

    /** The Constant MESSAGE_COUNT. */
    private static final int MESSAGE_COUNT = 2;

    /**
     * Send messages.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendMessages() throws Exception {

        Object[] bodies = Collections.nCopies(MESSAGE_COUNT, null).toArray();

        NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext)
                .from(POCRoute.ROUTE_FROM).fromRoute(POCRoute.ROUTE_NAME)
                .wereSentTo(POCRoute.ROUTE_TO).whenCompleted(bodies.length)
                .and().wereSentTo(POCRoute.ROUTE_TO).whenBodiesDone(bodies)
                .create();

        assertTrue(notifyBuilder.matches(
                POCRoute.PERIOD_SECONDS * MESSAGE_COUNT, TimeUnit.SECONDS));
    }

    /**
     * Send messages and assert wrong message count.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendMessagesAssertWrongMessageCount() throws Exception {

        Object[] bodies = Collections.nCopies(MESSAGE_COUNT, null).toArray();

        NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext)
                .from(POCRoute.ROUTE_FROM).fromRoute(POCRoute.ROUTE_NAME)
                .wereSentTo(POCRoute.ROUTE_TO).whenCompleted(bodies.length * 2)
                .create();

        assertFalse(notifyBuilder.matches(
                POCRoute.PERIOD_SECONDS * MESSAGE_COUNT, TimeUnit.SECONDS));
    }

    /**
     * Send messages and assert wrong bodies received.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendMessagesAssertWrongBodiesReceived() throws Exception {

        Object[] bodies = Collections.nCopies(MESSAGE_COUNT, "wrong body")
                .toArray();

        NotifyBuilder notifyBuilder = new NotifyBuilder(camelContext)
                .from(POCRoute.ROUTE_FROM).fromRoute(POCRoute.ROUTE_NAME)
                .wereSentTo(POCRoute.ROUTE_TO).whenBodiesDone(bodies).create();

        assertFalse(notifyBuilder.matches(
                POCRoute.PERIOD_SECONDS * MESSAGE_COUNT, TimeUnit.SECONDS));
    }
}
