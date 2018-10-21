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

public class TestNoteDAO {


    private static Environment env;

    private static DBI dbi;

    private static NoteDAO noteDAO;

    @BeforeClass
    public static void setup() {
        env = new Environment("test-env", Jackson.newObjectMapper(), null, new MetricRegistry(), null);
        dbi = new DBIFactory().build(env, getDataSourceFactory(), "test");
        dbi.registerArgumentFactory(new JodaDateTimeArgumentFactory());
        noteDAO = createNoteDAO(dbi);
        noteDAO.createNoteTable();

    }

    static DataSourceFactory getDataSourceFactory() {
        DataSourceFactory dataSourceFactory = new DataSourceFactory();
        dataSourceFactory.setDriverClass("org.h2.Driver");
        dataSourceFactory.setUrl("jdbc:h2:mem:testDb");
        dataSourceFactory.setUser("sa");
        dataSourceFactory.setPassword("");
        return dataSourceFactory;
    }

    private static NoteDAO createNoteDAO(DBI jdbi) {
        final NoteDAO noteDAO = jdbi.onDemand(NoteDAO.class);
        noteDAO.createNoteTable();
        return noteDAO;
    }

    @Test
    public void testAll()  {
        noteDAO.insert("title", "content", "hampus");
        assertEquals("title", noteDAO.getNoteFor(1,"hampus").getTitle());
        assertEquals("content", noteDAO.getNoteFor(1,"hampus").getContent());

        assertEquals("title", noteDAO.getNoteTitlesFor("hampus").get(0).getTitle());

        noteDAO.deleteNote(0, "hampus");
        assertEquals(null, noteDAO.getNoteFor(0,"hampus"));
        noteDAO.insert("title", "content", "hampus");
        noteDAO.deleteNotes("hampus");
        assertEquals(null, noteDAO.getNoteFor(0,"hampus"));
    }

}

