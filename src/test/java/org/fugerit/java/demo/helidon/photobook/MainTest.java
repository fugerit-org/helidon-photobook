
package org.fugerit.java.demo.helidon.photobook;

import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.metrics.Counter;
import org.eclipse.microprofile.metrics.MetricRegistry;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;

import io.helidon.microprofile.testing.junit5.HelidonTest;
import io.helidon.metrics.api.MetricsFactory;

import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.demo.helidon.photobook.rest.RestHelper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterAll;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@HelidonTest
class MainTest {

    @Inject
    private MetricRegistry registry;

    @Inject
    private WebTarget target;


    @Test
    void testHealth() {
        Response response = target
                .path("health")
                .request()
                .get();
        assertThat(response.getStatus(), is(200));
    }

    @Test
    void testMicroprofileMetrics() {
        Message message = target.path("simple-greet/Joe")
                .request()
                .get(Message.class);

        assertThat(message.getMessage(), is("Hello Joe"));
        Counter counter = registry.counter("personalizedGets");
        double before = counter.getCount();

        message = target.path("simple-greet/Eric")
                .request()
                .get(Message.class);

        assertThat(message.getMessage(), is("Hello Eric"));
        double after = counter.getCount();
        assertEquals(1d, after - before, "Difference in personalized greeting counter between successive calls");
    }

    @AfterAll
    static void clear() {
        MetricsFactory.closeAll();
    }

    @Test
    void testListOk() {
        Response response = target
                .path("/photobook-demo/api/photobook/view/list")
                .request()
                .get( Response.class );
        assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
    }

    @Test
    void testAlbumOk() {
        Response response = target
                .path("/photobook-demo/api/photobook/view/images/springio23")
                .request()
                .get( Response.class );
        assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
    }

    @Test
    void testImageOk() {
        Response response = target
                .path("/photobook-demo/api/photobook/view/download/springio23_1000.jpg")
                .request()
                .get( Response.class );
        assertEquals( response.getStatus(), Response.Status.OK.getStatusCode() );
    }

    @Test
    void testRestHelper() {
        Response res = RestHelper.defaultHandle( () -> {
            if ( Boolean.TRUE.booleanValue() ) {
                throw new ConfigRuntimeException( "scenario exception" );
            }
            return null;
        } );
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), res.getStatus());
    }

}
