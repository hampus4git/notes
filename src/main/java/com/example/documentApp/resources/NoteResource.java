package com.example.documentApp.resources;

import com.codahale.metrics.annotation.Timed;
import com.example.documentApp.core.Note;
import com.example.documentApp.core.Response;
import com.example.documentApp.core.User;
import com.example.documentApp.dao.NoteDAO;
import io.dropwizard.auth.Auth;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/note")
public class NoteResource {

    private final NoteDAO noteDAO;

    public NoteResource(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    @GET
    @Path("/list")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response<List<Note>> list(@Auth User user){
        Response<List<Note>> response = new Response<>();
        response.setStatus("success");
        response.setData(noteDAO.getNoteTitlesFor(user.getName()));
        return response;
    }

    @POST
    @Path("/show")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response getNote(@Auth User user, Note note){
        return new Response<>(Response.SUCCESS, noteDAO.getNoteFor(note.getId(), user.getName()));
    }

    @POST
    @Path("/edit")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response editNote(@Auth User user, Note updateNote){
        try {
            noteDAO.updateNote(updateNote.getId(), user.getName(), updateNote.getTitle(), updateNote.getContent());
            return new Response(Response.SUCCESS, null);
        }catch(Exception e){
            return new Response(Response.FAILURE, null);
        }
    }

    @POST
    @Path("/create")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response createNote(@Auth User user, Note updateNote){
        try {
            if(updateNote.getTitle()!=null && !updateNote.getTitle().isEmpty()) {
                noteDAO.insert(updateNote.getTitle(), updateNote.getContent(), user.getName());
                return new Response(Response.SUCCESS, null);
            }
            return new Response(Response.FAILURE, null);
        }catch(Exception e){
            return new Response(Response.FAILURE, null);
        }
    }

    @POST
    @Path("/delete")
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @RolesAllowed({"USER"})
    public Response deleteNote(@Auth User user, Note note){
        try {
            noteDAO.deleteNote(note.getId(), user.getName());
            return new Response(Response.SUCCESS, null);
        }catch(Exception e){
            return new Response(Response.FAILURE, null);
        }
    }


}
