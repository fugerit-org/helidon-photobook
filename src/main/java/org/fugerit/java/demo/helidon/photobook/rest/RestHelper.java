package org.fugerit.java.demo.helidon.photobook.rest;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.fugerit.java.core.function.UnsafeSupplier;

@Slf4j
public class RestHelper {

    private RestHelper() {}

    public static Response defaultHandle(UnsafeSupplier<Response, Exception> fun ) {
        Response res = null;
        try {
            res = fun.get();
            if ( res == null ) {
                res = Response.status(Response.Status.BAD_REQUEST).build();
            }
        } catch (Exception e) {
            log.info("Error : " + e, e);
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        return res;
    }

}
