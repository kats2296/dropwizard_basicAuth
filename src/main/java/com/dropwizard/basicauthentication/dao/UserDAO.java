package com.dropwizard.basicauthentication.dao;

import com.dropwizard.basicauthentication.domain.User;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SQLQuery;
import org.hibernate.SessionFactory;

import java.util.List;

public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<User> getAllUsers() {
        return list(criteria());
    }

    public String getPasswordOfUser(String username) {
        SQLQuery query = currentSession().createSQLQuery("Select password from User where username = :username");
        query.setParameter("username", username);
        List password = query.list();
        if (password.size() == 0)
            return null;

        else
            return password.get(0).toString();
    }

    public List<String> getAllUserNames() {
        SQLQuery query = currentSession().createSQLQuery("Select username from User");
        List users = query.list();
        if (users.size() == 0)
            return null;

        else
            return users;
    }
}
