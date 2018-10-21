package com.example.documentApp.resources;

import com.example.documentApp.auth.ExampleAuthenticator;
import com.example.documentApp.auth.ExampleAuthorizor;
import com.example.documentApp.core.Note;
import com.example.documentApp.core.Response;
import com.example.documentApp.core.User;
import com.example.documentApp.dao.NoteDAO;
import com.example.documentApp.dao.UserDAO;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.junit.*;

import javax.ws.rs.client.Entity;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.*;

public class TestNoteResource {

    private static final UserDAO userDAO = mock(UserDAO.class);
    private static final NoteDAO noteDAO = mock(NoteDAO.class);

    @ClassRule
    public static final ResourceTestRule noteResource = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new ExampleAuthenticator(userDAO))
                    .setAuthorizer(new ExampleAuthorizor())
                    .setRealm("Basic")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new NoteResource(noteDAO))
            .build();

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        when(userDAO.findUsernamePasswordCombination("hampus", "hampus")).thenReturn("hampus");
        when(userDAO.findRoleFor("hampus")).thenReturn("USER");
    }

    @After
    public void tearDown(){
        reset(noteDAO);
    }

    @Test
    public void testCreateSuccess() throws Exception{
        Note note  = new Note(0,"title", "content", "hampus");
        Response expectedJSONResponse = new Response("success", null);

        Response actualJSONResponse  = HTTPHelper.post(noteResource, "/note/create", Entity.json(note), "hampus", "hampus");

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testCreateEmptyNote() throws Exception{
        Note note  = new Note(0,"", "", "");
        Response expectedJSONResponse = new Response("failure", null);

        Response actualJSONResponse  = HTTPHelper.post(noteResource, "/note/create", Entity.json(note), "hampus", "hampus");

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testDeleteSuccess() throws Exception{
        Note note  = new Note(0,"title", "content", "hampus");
        Response expectedJSONResponse = new Response("success", null);

        Response actualJSONResponse  = HTTPHelper.post(noteResource, "/note/delete", Entity.json(note), "hampus", "hampus");

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testEditSuccess() throws Exception{
        Note note  = new Note(0,"title", "content", "hampus");
        Response expectedJSONResponse = new Response("success", null);

        Response actualJSONResponse  = HTTPHelper.post(noteResource, "/note/edit", Entity.json(note), "hampus", "hampus");

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testListSuccess() throws Exception{
        List<Note> notes = new LinkedList<>();
        notes.add(new Note(0, "title", "content", "hampus"));
        when(noteDAO.getNoteTitlesFor("hampus")).thenReturn(notes);

        Response actualJSONResponse  = HTTPHelper.get(noteResource, "/note/list", "hampus", "hampus");

        Assert.assertEquals("[{id=0, title=title, content=content, user=hampus}]", actualJSONResponse.getData().toString().trim());
    }

    @Test
    public void testShowSuccess() throws Exception{
        when(noteDAO.getNoteFor(0, "hampus")).thenReturn(new Note(0, "title", "content", "hampus"));

        Response actualJSONResponse  = HTTPHelper.post(noteResource, "/note/show", Entity.json(new Note(0,null,null,null)), "hampus", "hampus");

        Assert.assertEquals("{id=0, title=title, content=content, user=hampus}", actualJSONResponse.getData().toString().trim());
    }

}
