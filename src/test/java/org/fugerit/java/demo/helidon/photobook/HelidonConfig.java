package org.fugerit.java.demo.helidon.photobook;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.simple.config.microprofile.helper.SimpleConfigOverride;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.util.Map;

@Slf4j
@ApplicationScoped
public class HelidonConfig {

    final GenericContainer mongoDBContainer = new GenericContainer( "mongo:8.0.3" )
            .withCopyToContainer(MountableFile.forHostPath( new File( "src/test/resources/mongo-db/mongo-init.js" ).getPath() ), "/docker-entrypoint-initdb.d/mongo-init.js" )
            .withExposedPorts( 27017 );

    String mongoUrl;

    public HelidonConfig() {
        mongoDBContainer.start();
        int port = mongoDBContainer.getFirstMappedPort();
        mongoUrl = String.format( "mongodb://localhost:%s/photobook_demo", port );
        log.info( "mongodbUrl : {}" , mongoUrl );
        log.info( "mongo db setup OK : {}" , mongoUrl );
        SimpleConfigOverride.overrideConfig( Map.of( "helidon.mongodb.connection-string" , mongoUrl ) );
    }

}
