package ch.hftm.blogproject.boundary;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.CommentService;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.DatabaseException;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Comment Resource", description = "Comment Management API")
public class CommentResource {

    @Inject
    CommentService commentService;

    @GET
    @Authenticated
    @Operation(summary = "Get all Comments", description = "Returns a list of all Comments with optional search and pagination.")
    public Response getAllComments(@QueryParam("searchString") Optional<String> searchString, @QueryParam("page") Optional<Integer> page) {
        try {
            List<CommentDTO> comments = commentService.getComments(searchString, page);
            return Response.ok(comments).build();
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("/{commentID}")
    @Authenticated
    @Operation(summary = "Get a Comment by ID", description = "Returns a Comment by its ID.")
    public Response getCommentById(@PathParam("commentID") Long id) {
        try {
            CommentDTO comment = commentService.getCommentById(id);
            return Response.ok(comment).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("/count")
    @RolesAllowed({"admin"})
    @Operation(summary = "Count Comments", description = "Returns the total number of Comments in the system.")
    public Response countComments() {
        try {
            Long count = commentService.countComments();
            return Response.ok(count).build();
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Response buildErrorResponse(Response.Status status, String message) {
        return Response.status(status).entity(message).build();
    }
}
