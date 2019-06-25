package com.dropwizard.basicauthentication.controller;

import com.codahale.metrics.annotation.Timed;
import com.dropwizard.basicauthentication.dao.UserDAO;
import com.dropwizard.basicauthentication.domain.User;
import io.dropwizard.auth.Auth;
import io.dropwizard.hibernate.UnitOfWork;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/v1.0/user")
@Produces(MediaType.APPLICATION_JSON)
public class UserController {

    private UserDAO userDAO;

    public UserController(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @GET
    @Path("/authenticate")
    @Timed
    @UnitOfWork

    public String checkIfUserExists(@Auth User user) {
        return "Hello World !!";
    }

    @GET
    @Timed
    @UnitOfWork
    public Response getAllUsers() {
        List<User> users = userDAO.getAllUsers();
        if(users.size() == 0)
            return Response.status(Response.Status.NOT_FOUND).entity("NO USER PRESENT IN THE DATABASE").build();

        else
            return Response.ok(users).build();
    }

}
