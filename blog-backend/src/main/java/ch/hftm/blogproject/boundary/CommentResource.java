package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.CommentService;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Comment Resource", description = "Comment Management API")
public class CommentResource {

    @Inject
    CommentService commentService;

    // Get all comments
    @GET
    public Uni<Response> getAllComments() {
        return commentService.getAllComments()
            .onItem().transform(comments -> Response.ok(comments).build())
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching comments: " + e.getMessage()).build());
    }

    // Get a comment by ID
    @GET
    @Path("/{commentID}")
    public Uni<Response> getCommentById(@PathParam("commentID") Long commentID) {
        return commentService.getCommentById(commentID)
            .onItem().transform(commentDTO -> Response.ok(commentDTO).build()) // Return 200 OK with the CommentDTO
            .onFailure(NotFoundException.class).recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage()).build()) // Return 404 if NotFoundException is thrown
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching comment: " + e.getMessage()).build()); // Catch all other errors
    }

    // Add a new comment
    @POST
    public Uni<Response> addComment(CommentDTO commentDTO) {
        return commentService.addComment(commentDTO)
            .onItem().transform(createdComment -> Response.status(Response.Status.CREATED).entity(createdComment).build()) // Return 201 Created
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error creating comment: " + e.getMessage()).build());
    }

    // Delete a comment by ID
    @DELETE
    @Path("/{commentID}")
    public Uni<Response> deleteComment(@PathParam("commentID") Long commentID) {
        return commentService.deleteComment(commentID)
            .onItem().transform(ignored -> Response.noContent().build()) // Return 204 No Content on success
            .onFailure(NotFoundException.class).recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage()).build()) // Return 404 if NotFoundException is thrown
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting comment: " + e.getMessage()).build()); // Catch all other errors
    }

    // Count all comments
    @GET
    @Path("/count")
    public Uni<Response> countComments() {
        return commentService.countComments()
            .onItem().transform(count -> Response.ok(count).build()) // Return 200 OK with the count
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error counting comments: " + e.getMessage()).build());
    }
}
