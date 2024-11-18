package org.fugerit.java.demo.helidon.photobook.rest;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.fugerit.java.demo.helidon.photobook.dto.ResultDTO;
import org.fugerit.java.demo.helidon.photobook.service.PhotobookService;

import java.io.IOException;
import java.util.Locale;

@ApplicationScoped
@Slf4j
@Path("/photobook-demo/api/photobook/view")
public class PhotobookRest {


    private static final Integer DEF_PAGE_SIZE = 10;

    @Inject
    private PhotobookService photobookService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/list")
    public Response getVersion() {
        return RestHelper.defaultHandle( () -> {
            Document doc =  this.photobookService.listPhotobooks(Locale.ITALY.getCountry(), 10, 1);
            ResultDTO<Document> dto = new ResultDTO<>( doc );
            return Response.ok().entity(  dto ).build();
        } );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images/{photobookId}/language/{language}/current_page/{currentPage}/page_size/{pageSize}")
    public Response images( @PathParam( "photobookId" ) String photobookId, @PathParam( "language" ) String language,
                                                       @PathParam( "currentPage" ) Integer currentPage, @PathParam( "pageSize" ) Integer pageSize ) {
        return RestHelper.defaultHandle( () -> {
            Document doc =  this.photobookService.listImages( photobookId, language, pageSize, currentPage);
            ResultDTO<Document> dto = new ResultDTO<>( doc );
            return Response.ok().entity(  dto ).build();
        } );
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/images/{photobookId}")
    public Response images( @PathParam( "photobookId" ) String photobookId ) {
        return this.images(photobookId, "def", 1, DEF_PAGE_SIZE);
    }

    @GET
    @Produces( "image/jpeg" )
    @Path(value = "/download/{imagePath}" )
    public byte[] downloadImage( @PathParam( "imagePath" ) String imagePath ) throws IOException {
        imagePath = imagePath.substring( 0, imagePath.indexOf( '.' ) );
        String[] split = imagePath.split( "_" );
        log.debug( "photobookId : {}, imageId : {}", split[0], split[1] );
        return this.photobookService.downloadImage( split[0], split[1] );
    }

}
