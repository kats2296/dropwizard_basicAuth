package com.dropwizard.basicauthentication;

import com.dropwizard.basicauthentication.authentication.BasicAuthentication;
import com.dropwizard.basicauthentication.config.BasicAuthConfiguration;
import com.dropwizard.basicauthentication.controller.UserController;
import com.dropwizard.basicauthentication.dao.UserDAO;
import com.dropwizard.basicauthentication.domain.User;
import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.UnitOfWorkAwareProxyFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserManagementApplication extends Application<BasicAuthConfiguration> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserManagementApplication.class);
    private static final String SQL = "sql";

    private final HibernateBundle<BasicAuthConfiguration> hibernateBundle
            = new HibernateBundle<BasicAuthConfiguration>(User.class)
    {

        @Override
        public DataSourceFactory getDataSourceFactory(
                BasicAuthConfiguration configuration
        ) {
            return configuration.getDataSourceFactory();
        }

    };

    @Override
    public void initialize(final Bootstrap<BasicAuthConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
    }


    @Override
    public void run(BasicAuthConfiguration basicAuthConfiguration, Environment environment) {

        final UserDAO userDAO = new UserDAO(hibernateBundle.getSessionFactory());

        BasicAuthentication basicAuthentication = new UnitOfWorkAwareProxyFactory(hibernateBundle).
                create(BasicAuthentication.class, UserDAO.class, userDAO);

        environment.jersey().register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                .setAuthenticator(basicAuthentication)
                .setRealm("BASIC-AUTH-REALM")
                .buildAuthFilter()));

        LOGGER.info("Registering REST resources");
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));

        environment.jersey().register(new UserController(userDAO));
    }

    public static void main(String[] args) throws Exception {
        new UserManagementApplication().run(args);
    }


}
