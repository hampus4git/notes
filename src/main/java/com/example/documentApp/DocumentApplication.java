package com.example.documentApp;

import com.example.documentApp.auth.ExampleAuthenticator;
import com.example.documentApp.auth.ExampleAuthorizor;
import com.example.documentApp.core.User;
import com.example.documentApp.dao.UserDAO;
import com.example.documentApp.dao.NoteDAO;
import com.example.documentApp.resources.UserResource;
import com.example.documentApp.resources.NoteResource;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

public class DocumentApplication extends Application<DocumentConfiguration> {

    public static void main(String[] args) throws Exception {
        String[] startArgs = {"server", "src/main/resources/notes.yml"};
        new DocumentApplication().run(startArgs);
    }

    @Override
    public String getName() {
        return "Document-Website";
    }

    @Override
    public void initialize(Bootstrap<DocumentConfiguration> bootstrap) {
        bootstrap.addBundle(new AssetsBundle("/assets/", "/", "index.htm"));
        bootstrap.addBundle(new AssetsBundle("/assets/js", "/app.js", "app.js", "app"));
        bootstrap.addBundle(new AssetsBundle("/assets/css", "/styles.css", "styles.css", "style"));


        registerViewAndController(bootstrap, "user.login");
        registerViewAndController(bootstrap, "user.register");
        registerViewAndController(bootstrap, "note.create");
        registerViewAndController(bootstrap, "note.edit");
        registerViewAndController(bootstrap, "note.list");
        registerViewAndController(bootstrap, "note.show");




        /*bootstrap.addBundle(new AssetsBundle("/assets/views", "/views/note.list.view.html", "note.list.view.html", "notelistView"));
        bootstrap.addBundle(new AssetsBundle("/assets/js/controllers", "/app-controller/note.list.controller.js", "note.list.controller.js","notelistControl"));
*/
        bootstrap.addBundle(new AssetsBundle("/assets/js/services", "/app-service/user.service.js", "user.service.js", "authenticationService"));
        bootstrap.addBundle(new AssetsBundle("/assets/js/services", "/app-service/note.service.js", "note.service.js", "notelistService"));
    }

    private void registerViewAndController(Bootstrap<DocumentConfiguration> bootstrap, String name){
        registerView(bootstrap, name+ ".view.html");
        registerController(bootstrap,name+ ".controller.js");
    }

    private void registerView(Bootstrap<DocumentConfiguration> bootstrap, String fileName){
        bootstrap.addBundle(new AssetsBundle("/assets/views", "/views/" + fileName, fileName, fileName));
    }

    private void registerController(Bootstrap<DocumentConfiguration> bootstrap, String fileName) {
        bootstrap.addBundle(new AssetsBundle("/assets/js/controllers", "/app-controller/" + fileName, fileName, fileName));
    }

    @Override
    public void run(DocumentConfiguration configuration,
                    Environment environment) {

        final DBI jdbi = createJDBI(configuration, environment);

        final UserDAO userDAO = createLoginDAO(jdbi);
        final NoteDAO noteDAO = createNoteDAO(jdbi);

        setupResources(environment, userDAO, noteDAO);

        setupAuthentication(environment, userDAO);

    }

    public DBI createJDBI(DocumentConfiguration configuration,
                          Environment environment) {
        final DBIFactory factory = new DBIFactory();
        final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "mysql");
        return jdbi;
    }

    public UserDAO createLoginDAO(DBI jdbi) {
        final UserDAO userDAO = jdbi.onDemand(UserDAO.class);
        userDAO.createLoignTable();
        return userDAO;
    }

    public NoteDAO createNoteDAO(DBI jdbi) {
        final NoteDAO noteDAO = jdbi.onDemand(NoteDAO.class);
        noteDAO.createNoteTable();
        return noteDAO;
    }

    public void setupResources(Environment environment, UserDAO userDAO, NoteDAO noteDAO) {
        final UserResource userResource = new UserResource(
                userDAO, noteDAO
        );
        final NoteResource noteResource = new NoteResource(noteDAO);
        environment.jersey().setUrlPattern("/api/*");
        environment.jersey().register(userResource);
        environment.jersey().register(noteResource);
    }

    public void setupAuthentication(Environment environment, UserDAO userDAO) {
        environment.jersey().register(new AuthDynamicFeature(
                new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new ExampleAuthenticator(userDAO))
                        .setAuthorizer(new ExampleAuthorizor())
                        .setRealm("Basic")
                        .buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
    }


}
