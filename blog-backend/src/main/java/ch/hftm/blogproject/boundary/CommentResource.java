package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.CommentService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Comment Resource", description = "Comment Management API")
public class CommentResource {

    @Inject
    CommentService commentService;

    // @GET
    // @Authenticated
    // @Operation(summary = "Get all Comments", description = "Returns a list of all Comments with optional search and pagination.")
    // public Response getAllComments(@QueryParam("searchString") Optional<String> searchString, @QueryParam("page") Optional<Integer> page) {
    //     try {
    //         List<CommentDTO> comments = commentService.getComments(searchString, page);
    //         return Response.ok(comments).build();
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @GET
    // @Path("/{commentID}")
    // @Authenticated
    // @Operation(summary = "Get a Comment by ID", description = "Returns a Comment by its ID.")
    // public Response getCommentById(@PathParam("commentID") Long id) {
    //     try {
    //         CommentDTO comment = commentService.getCommentById(id);
    //         return Response.ok(comment).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @GET
    // @Path("/count")
    // @RolesAllowed({"admin"})
    // @Operation(summary = "Count Comments", description = "Returns the total number of Comments in the system.")
    // public Response countComments() {
    //     try {
    //         Long count = commentService.countComments();
    //         return Response.ok(count).build();
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // private Response buildErrorResponse(Response.Status status, String message) {
    //     return Response.status(status).entity(message).build();
    // }
}
