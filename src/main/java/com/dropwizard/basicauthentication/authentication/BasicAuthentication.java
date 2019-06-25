package com.dropwizard.basicauthentication.authentication;

import com.dropwizard.basicauthentication.dao.UserDAO;
import com.dropwizard.basicauthentication.domain.User;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.hibernate.UnitOfWork;

import java.util.List;
import java.util.Optional;

public class BasicAuthentication implements Authenticator<BasicCredentials, User> {

    private UserDAO userDAO;

    public BasicAuthentication(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    @UnitOfWork
    public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {

        List<String> users = userDAO.getAllUserNames();
        
        if(users.contains(basicCredentials.getUsername()) &&
                userDAO.getPasswordOfUser(basicCredentials.getUsername()).equals(basicCredentials.getPassword())) {
            return Optional.of(new User(basicCredentials.getUsername(), basicCredentials.getPassword()));
        }

        return Optional.empty();
    }
}
