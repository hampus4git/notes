package com.example.documentApp.resources;

import com.example.documentApp.auth.ExampleAuthenticator;
import com.example.documentApp.auth.ExampleAuthorizor;
import com.example.documentApp.core.LoginForm;
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

import static org.mockito.Mockito.*;

public class TestUserResource {

    private static final UserDAO userDAO = mock(UserDAO.class);
    private static final NoteDAO noteDAO = mock(NoteDAO.class);

    @ClassRule
    public static final ResourceTestRule userResource = ResourceTestRule.builder()
            .setTestContainerFactory(new GrizzlyWebTestContainerFactory())
            .addProvider(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                    .setAuthenticator(new ExampleAuthenticator(userDAO))
                    .setAuthorizer(new ExampleAuthorizor())
                    .setRealm("Basic")
                    .buildAuthFilter()))
            .addProvider(RolesAllowedDynamicFeature.class)
            .addProvider(new AuthValueFactoryProvider.Binder<>(User.class))
            .addResource(new UserResource(userDAO, noteDAO))
            .build();

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @After
    public void tearDown(){
        reset(userDAO);
        reset(noteDAO);
    }

    @Test
    public void testLoginSuccess() throws Exception{
        LoginForm loginForm = new LoginForm("hampus", "hampus");
        Response expectedJSONResponse = new Response("success", null);
        when(userDAO.findUsernamePasswordCombination("hampus","hampus")).thenReturn("hampus");

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/login", Entity.json(loginForm));

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testLoginFUnauthorized() throws Exception{
        LoginForm loginForm = new LoginForm("pontus", "pontus");
        Response expectedJSONResponse = new Response("unauthorized", null);

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/login", Entity.json(loginForm));

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testRegisterSuccess() throws Exception{
        LoginForm loginForm = new LoginForm("pontus", "pontus");
        Response expectedJSONResponse = new Response("success", null);

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/register", Entity.json(loginForm));

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testRegisterEmptyInput() throws Exception {
        LoginForm loginForm = new LoginForm("", "");
        Response expectedJSONResponse = new Response("invalid input", null);

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/register", Entity.json(loginForm));

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testRegisterNullInput() throws Exception {
        LoginForm loginForm = new LoginForm(null, null);
        Response expectedJSONResponse = new Response("invalid input", null);

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/register", Entity.json(loginForm));

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testRegisterAlreadyPresentUser() throws Exception{
        LoginForm loginForm = new LoginForm("pontus", "pontus");
        Response expectedJSONResponse = new Response("username already present", null);
        when(userDAO.findUsername("pontus")).thenReturn("pontus");

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/register", Entity.json(loginForm));

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }

    @Test
    public void testUnregisterSuccess() throws Exception{
        Response expectedJSONResponse = new Response("success", null);
        when(userDAO.findUsernamePasswordCombination("hampus","hampus")).thenReturn("hampus");
        when(userDAO.findRoleFor("hampus")).thenReturn("USER");

        Response actualJSONResponse  = HTTPHelper.post(userResource, "/user/unregister", Entity.json(""), "hampus", "hampus");

        Assert.assertEquals(expectedJSONResponse.getStatus(), actualJSONResponse.getStatus());
    }
}
