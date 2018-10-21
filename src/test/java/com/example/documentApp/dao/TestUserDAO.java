package com.example.documentApp.dao;

import com.codahale.metrics.MetricRegistry;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.jdbi.args.JodaDateTimeArgumentFactory;
import io.dropwizard.setup.Environment;
import org.junit.BeforeClass;
import org.junit.Test;
import org.skife.jdbi.v2.DBI;

import static org.junit.Assert.assertEquals;

public class TestUserDAO {


    private static Environment env;

    private static DBI dbi;

    private static UserDAO userDAO;

    @BeforeClass
    public static void setup() {
        env = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build(env, getDataSourceFactory(), "test");
        dbi.registerArgumentFactory(new JodaDateTimeArgumentFactory());
        userDAO = createUserDAO(dbi);
        userDAO.createLoignTable();

    }

    static DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.h2.Driver");
        dataSourceFactory.setUrl("jdbc:h2:mem:testDb");
        dataSourceFactory.setUser("sa");
        dataSourceFactory.setPassword("");
        return dataSourceFactory;
    }

    private static UserDAO createUserDAO(DBI jdbi) {
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        userDAO.createLoignTable();
        return userDAO;
    }

    @Test
    public void testAll()  {
        userDAO.insert("hampus", "hampus", "user");
        assertEquals("hampus",userDAO.findUsername("hampus"));
        assertEquals("hampus",userDAO.findUsernamePasswordCombination("hampus", "hampus"));
        assertEquals("user",userDAO.findRoleFor("hampus"));
        userDAO.delete("hampus");
        assertEquals(null,userDAO.findUsername("hampus"));
    }

}

