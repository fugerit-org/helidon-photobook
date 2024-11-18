package org.fugerit.java.demo.helidon.photobook;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.ConfigValue;
import org.eclipse.microprofile.config.spi.ConfigBuilder;
import org.eclipse.microprofile.config.spi.ConfigProviderResolver;
import org.eclipse.microprofile.config.spi.ConfigSource;
import org.eclipse.microprofile.config.spi.Converter;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

import java.io.File;
import java.util.List;
import java.util.Optional;

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
        ConfigProviderResolver current = ConfigProviderResolver.instance();
        ConfigProviderResolver.setInstance(new ConfigProviderResolver() {
            @Override
            public Config getConfig() {
                return new CustomConfig( current.getConfig(), mongoUrl );
            }

            @Override
            public Config getConfig(ClassLoader loader) {
                return new CustomConfig( current.getConfig(loader), mongoUrl );
            }

            @Override
            public ConfigBuilder getBuilder() {
                return current.getBuilder();
            }

            @Override
            public void registerConfig(Config config, ClassLoader classLoader) {
                current.registerConfig(config, classLoader);
            }

            @Override
            public void releaseConfig(Config config) {
                current.releaseConfig(config);
            }
        });
    }

}

@Slf4j
class CustomConfig implements Config {

    private Config wrapped;

    private String mongoUrl;

    public CustomConfig(Config wrapped, String mongoUrl) {
        this.wrapped = wrapped;
        this.mongoUrl = mongoUrl;
    }

    @Override
    public Iterable<ConfigSource> getConfigSources() {
        return wrapped.getConfigSources();
    }

    @Override
    public ConfigValue getConfigValue(String propertyName) {
        return wrapped.getConfigValue(propertyName);
    }

    @Override
    public <T> Optional<Converter<T>> getConverter(Class<T> forType) {
        return wrapped.getConverter(forType);
    }

    @Override
    public <T> Optional<T> getOptionalValue(String propertyName, Class<T> propertyType) {
        log.info( "propertyName getOptionalValue : {}", propertyName );
        if ( "helidon.mongodb.connection-string".equals( propertyName ) && propertyType.isAssignableFrom( String.class ) ) {
            return Optional.of( (T) mongoUrl );
        } else {
            return wrapped.getOptionalValue(propertyName, propertyType);
        }
    }

    @Override
    public <T> Optional<List<T>> getOptionalValues(String propertyName, Class<T> propertyType) {
        return wrapped.getOptionalValues(propertyName, propertyType);
    }

    @Override
    public Iterable<String> getPropertyNames() {
        return wrapped.getPropertyNames();
    }

    @Override
    public <T> T getValue(String propertyName, Class<T> propertyType) {
        log.info( "propertyName getValue : {}", propertyName );
        if ( "helidon.mongodb.connection-string".equals( propertyName ) && propertyType.isAssignableFrom( String.class ) ) {
            return (T) mongoUrl;
        } else {
            return wrapped.getValue(propertyName, propertyType);
        }
    }

    @Override
    public <T> List<T> getValues(String propertyName, Class<T> propertyType) {
        return wrapped.getValues(propertyName, propertyType);
    }

    @Override
    public <T> T unwrap(Class<T> type) {
        return wrapped.unwrap(type);
    }
}
