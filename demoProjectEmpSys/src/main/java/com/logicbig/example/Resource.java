package com.logicbig.example;

import com.google.gson.Gson;
import com.model.movies.MovieModel;

import javax.print.attribute.standard.MediaSize;
import javax.validation.Payload;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.logging.Logger;


@Path("/movie-recommendations")
public class Resource {

    private static final Logger logger = Logger.getLogger(Resource.class.getName());


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getMovieTitlesFromApp(){
       return new Gson().toJson(App.getMovieTitleFromDatabase());
    }



    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String createNewMovieEntry(String payLoad){
        String newEntity = App.createMovieEntryInDatabase(payLoad);
        logger.info(newEntity);
        return newEntity;
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String changeMovieEntry(String payLoad){

        boolean context = App.changeMovieEntryInDatabase(payLoad);

        if(context)
            return Response.ok("Successfully Changed!",MediaType.TEXT_PLAIN).build().toString();

        return Response.notModified("The movie could not be changed ").build().toString();
    }



    @DELETE
    @Path("/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String deleteMovieEntry(@PathParam("id") int movieId) {
        boolean context = App.deleteMovieEntryFromDatabase(movieId);

        if (context) {
            return Response.ok("Successfully Deleted!", MediaType.TEXT_PLAIN).build().toString();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build().toString();
        }
    }


}