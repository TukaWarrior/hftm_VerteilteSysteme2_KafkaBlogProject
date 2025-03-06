package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.CommentService;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
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
    public Response getAllComments() {
        try {
            return Response.ok(commentService.getAllComments()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching comments: " + e.getMessage()).build();
        }
    }

    // Get all comments by blog ID
    @GET
    @Path("/blog/{blogID}")
    public Response getCommentsByBlogId(@PathParam("blogID") Long blogID) {
        try {
            return Response.ok(commentService.getCommentsByBlogId(blogID)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching comments for blog ID " + blogID + ": " + e.getMessage()).build();
        }
    }

    // Get a comment by ID
    @GET
    @Path("/{commentID}")
    public Response getCommentById(@PathParam("commentID") Long commentID) {
        try {
            return Response.ok(commentService.getCommentById(commentID)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching comment: " + e.getMessage()).build();
        }
    }

    // Add a new comment
    @POST
    public Response addComment(CommentDTO commentDTO) {
        try {
            CommentDTO createdComment = commentService.addComment(commentDTO);
            return Response.status(Response.Status.CREATED).entity(createdComment).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error creating comment: " + e.getMessage()).build();
        }
    }

    // Delete a comment by ID
    @DELETE
    @Path("/{commentID}")
    public Response deleteComment(@PathParam("commentID") Long commentID) {
        try {
            commentService.deleteComment(commentID);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting comment: " + e.getMessage()).build();
        }
    }

    // Delete all comments
    @DELETE
    public Response deleteAllComments() {
        try {
            commentService.deleteAllComments();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting all comments: " + e.getMessage()).build();
        }
    }

    // Count all comments
    @GET
    @Path("/count")
    public Response countComments() {
        try {
            return Response.ok(commentService.countComments()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error counting comments: " + e.getMessage()).build();
        }
    }
}
