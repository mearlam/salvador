package com.salvador.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

/**
 * Created by IntelliJ IDEA.
 * User: mearlam
 * Date: 25/02/13
 * Time: 14:49
 */
@Path("/runner")
public class TestRunnerService {

    @GET
    @Path("/{id}")
    public Response update(@PathParam("id") String id) {
        return Response.ok().build();
    }
}
