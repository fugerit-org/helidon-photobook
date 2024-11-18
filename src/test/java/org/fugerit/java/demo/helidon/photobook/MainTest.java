
package org.fugerit.java.demo.helidon.photobook;

import io.helidon.metrics.api.MetricsFactory;
import io.helidon.microprofile.testing.junit5.HelidonTest;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.metrics.MetricRegistry;
import org.fugerit.java.core.cfg.ConfigRuntimeException;
import org.fugerit.java.demo.helidon.photobook.rest.RestHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@HelidonTest
class MainTest {

    private HelidonConfig config = new HelidonConfig();

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
