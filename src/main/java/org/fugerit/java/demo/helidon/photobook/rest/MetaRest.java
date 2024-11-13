package org.fugerit.java.demo.helidon.photobook.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.util.PropsIO;

import java.util.Properties;

@ApplicationScoped
@Slf4j
@Path("/api/meta")
public class MetaRest {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/version")
    public Response getVersion() {
        return RestHelper.defaultHandle( () -> {
            Properties buildProps = PropsIO.loadFromClassLoader( "build.properties" );
            log.info( "buildProps : {}", buildProps );
            return Response.ok().entity( buildProps ).build();
        } );
    }

    private static final String[] ADD_PROPS = { "java.version", "java.vendor", "os.name", "os.version", "os.arch" };

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/info")
    public Response getInfo() {
        return RestHelper.defaultHandle( () -> {
            StringBuilder info = new StringBuilder();
            for ( String key : ADD_PROPS ) {
                info.append( key );
                info.append( " = " );
                info.append( System.getProperty( key ) );
                info.append( ", " );
            }
            info.append( "max memory (mb) : " );
            info.append( Runtime.getRuntime().maxMemory()/1024/1024 );
            String res = info.toString();
            return Response.ok().entity( res ).build();
        } );
    }

}
