package poc.camel.eip.routes.test;

import java.util.Arrays;
import java.util.List;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringBootRunner;
import org.junit.After;
import org.junit.Before;
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
public class POCRouteAdviceAndMockTest {

    /** The to endpoint. */
    @EndpointInject(uri = MOCK_TO)
    private MockEndpoint toEndpoint;

    /** The camel context. */
    @Autowired
    private CamelContext camelContext;

    /** The producer. */
    @EndpointInject(uri = MOCK_FROM)
    private ProducerTemplate producer;

    /** The Constant MOCK_TO. */
    protected static final String MOCK_TO = "mock:" + POCRoute.ROUTE_NAME;

    /** The Constant MOCK_FROM. */
    protected static final String MOCK_FROM = "direct:POCRouteTest-from";

    /**
     * Setup.
     *
     * @throws Exception the exception
     */
    @Before
    public void setUp() throws Exception {

        camelContext.getRouteDefinition(POCRoute.ROUTE_NAME)
                .adviceWith(camelContext, new AdviceWithRouteBuilder() {

                    @Override
                    public void configure() throws Exception {

                        replaceFromWith(MOCK_FROM);
                        interceptSendToEndpoint(POCRoute.ROUTE_TO)
                                .skipSendToOriginalEndpoint().to(MOCK_TO);
                    }
                });
    }

    /**
     * Send messages.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendMessages() throws Exception {

        final List<String> messages = Arrays.asList("A message",
                "Another message", "Yet another message");

        toEndpoint.expectedMessageCount(messages.size());
        toEndpoint.expectedBodiesReceived(messages);

        messages.forEach(message -> {
            producer.sendBody(message);
        });

        toEndpoint.assertIsSatisfied();
    }

    /**
     * Send messages and assert wrong message count.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendMessagesAssertWrongMessageCount() throws Exception {

        final List<String> messages = Arrays.asList("A message",
                "Another message", "Yet another message");

        toEndpoint.expectedMessageCount(messages.size() + 1);

        messages.forEach(message -> {
            producer.sendBody(message);
        });

        toEndpoint.assertIsNotSatisfied();
    }

    /**
     * Send messages and assert wrong bodies received.
     *
     * @throws Exception the exception
     */
    @Test
    public void sendMessagesAssertWrongBodiesReceived() throws Exception {

        final List<String> messages = Arrays.asList("A message",
                "Another message", "Yet another message");

        final List<String> wrongMessages = Arrays.asList("A wrong message",
                "Another wrong message", "Yet another wrong message");

        toEndpoint.expectedBodiesReceived(wrongMessages);

        messages.forEach(message -> {
            producer.sendBody(message);
        });

        toEndpoint.assertIsNotSatisfied();
    }

    /**
     * Tear down.
     *
     * @throws Exception the exception
     */
    @After
    public void tearDown() throws Exception {
        toEndpoint.reset();
    }
}
