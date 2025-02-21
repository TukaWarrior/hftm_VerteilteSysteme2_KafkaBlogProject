package ch.hftm.blogproject.boundary;

import java.util.List;
import java.util.Optional;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.BlogPostService;
import ch.hftm.blogproject.control.CommentService;
import ch.hftm.blogproject.model.dto.BlogPostDTO;
import ch.hftm.blogproject.model.dto.CommentDTO;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import io.quarkus.security.Authenticated;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/blogpost")
@Tag(name = "BlogPost Resource", description = "BlogPost Management API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BlogPostResource {

    @Inject
    BlogPostService blogPostService;
    @Inject
    CommentService commentService;
    @Inject
    JsonWebToken jsonWebToken;

    @GET
    @Authenticated
    @Operation(summary = "Get all BlogPosts", description = "Returns a list of BlogPosts with optional search and pagination.")
    public Response getAllBlogPosts(@QueryParam("searchString") Optional<String> searchString, @QueryParam("page") Optional<Integer> page) {
        try {
            List<BlogPostDTO> blogPosts = blogPostService.getBlogPosts(searchString, page);
            return Response.ok(blogPosts).build();
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("/{blogPostID}")
    @Authenticated
    @Operation(summary = "Get one BlogPost by ID", description = "Returns a BlogPost by its ID.")
    public Response getBlogPost(@PathParam("blogPostID") Long blogPostID) {
        try {
            BlogPostDTO blogPost = blogPostService.getBlogPostById(blogPostID);
            return Response.ok(blogPost).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @POST
    @Authenticated
    @Operation(summary = "Add one new BlogPost", description = "Creates a new BlogPost")
    public Response addBlogPost(@Valid BlogPostDTO blogPostDTO) {
        try {
            String creator = jsonWebToken.getName(); 
            blogPostDTO.setCreator(creator);

            BlogPostDTO createdBlogPost = blogPostService.addBlogPost(blogPostDTO);
            return Response.status(Response.Status.CREATED).entity(createdBlogPost).build();
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    // Put and patch are the same for now.
    @PUT
    @Path("/{blogPostID}")
    @RolesAllowed({"admin", "moderator"})
    @Operation(summary = "Update a BlogPost", description = "Updates an existing BlogPost")
    public Response putBlogPost(@PathParam("blogPostID") Long blogPostID, BlogPostDTO blogPostDTO) {
        try {
            blogPostDTO.setBlogPostID(blogPostID);
            BlogPostDTO updatedBlogPost = blogPostService.putBlogPost(blogPostDTO);
            return Response.ok(updatedBlogPost).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
    
    // Put and patch are the same for now.
    @PATCH
    @Path("/{blogPostID}")
    @RolesAllowed({"admin", "moderator"})
    @Operation(summary = "Partially Update a BlogPost", description = "Partially updates an existing BlogPost")
    public Response patchBlogPost(@PathParam("blogPostID") Long blogPostID, BlogPostDTO blogPostDTO) {
        try {
            blogPostDTO.setBlogPostID(blogPostID);
            BlogPostDTO updatedBlogPost = blogPostService.putBlogPost(blogPostDTO);
            return Response.ok(updatedBlogPost).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @Path("/{blogPostID}")
    @RolesAllowed({"admin", "moderator"})
    @Operation(summary = "Delete one BlogPost by ID", description = "Deletes an existing BlogPost")
    public Response deleteBlogPost(@PathParam("blogPostID") Long id) {
        try {
            BlogPostDTO deletedBlogPost = blogPostService.deleteBlogPost(id);
            return Response.ok(deletedBlogPost).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @RolesAllowed({"admin"})
    @Operation(summary = "Delete all BlogPosts", description = "Deletes all BlogPosts")
    public Response deleteAllBlogPosts() {
        try {
            blogPostService.deleteAllBlogPosts();
            return Response.noContent().build();
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("/count")
    @RolesAllowed({"admin"})
    @Operation(summary = "Count BlogPosts", description = "Returns the total number of BlogPosts in the system.")
    public Response countBlogPosts() {
        try {
            Long count = blogPostService.countBlogPosts();
            return Response.ok(count).build();
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private Response buildErrorResponse(Response.Status status, String message) {
        return Response.status(status).entity(message).build();
    }

// Comments
    @GET
    @Path("/{blogPostID}/comment")
    @Authenticated
    @Operation(summary = "Get all Comments for a BlogPost", description = "Returns a list of Comments for a specific BlogPost.")
    public Response getCommentsForBlogPost(@PathParam("blogPostID") Long blogPostID) {
        try {
            List<CommentDTO> comments = commentService.getAllCommentsOfBlogPost(blogPostID);
            return Response.ok(comments).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @POST
    @Path("/{blogPostID}/comment")
    @Authenticated
    @Operation(summary = "Add one Comment to a BlogPost", description = "Adds a new Comment to a specific BlogPost.")
    public Response addCommentToBlogPost(@PathParam("blogPostID") Long blogPostID, @Valid CommentDTO commentDTO) {
        try {
            String creator = jsonWebToken.getName();
            commentDTO.setCreator(creator);
            CommentDTO createdComment = commentService.addCommentToBlog(blogPostID, commentDTO);
            return Response.status(Response.Status.CREATED).entity(createdComment).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("/{blogPostID}/comment/{commentID}")
    @Authenticated
    @Operation(summary = "Get a Comment by ID from a BlogPost", description = "Returns a Comment by its ID under a specific BlogPost.")
    public Response getCommentFromBlogPost(@PathParam("blogPostID") Long blogPostID, @PathParam("commentID") Long commentID) {
        try {
            CommentDTO comment = commentService.getCommentFromBlogPost(blogPostID, commentID);
            return Response.ok(comment).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @PUT
    @Path("/{blogPostID}/comment/{commentID}")
    @RolesAllowed({"admin", "moderator"})
    @Operation(summary = "Update a Comment under a BlogPost", description = "Updates an existing Comment under a specific BlogPost.")
    public Response updateCommentFromBlogPost(@PathParam("blogPostID") Long blogPostID, @PathParam("commentID") Long commentID, CommentDTO commentDTO) {
        try {
            commentDTO.setCommentID(commentID); // Ensure the ID in DTO is the same as the path ID
            CommentDTO updatedComment = commentService.updateCommentOfBlog(blogPostID, commentDTO);
            return Response.ok(updatedComment).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @DELETE
    @Path("/{blogPostID}/comment/{commentID}")
    @RolesAllowed({"admin", "moderator"})
    @Operation(summary = "Delete a Comment from a BlogPost", description = "Deletes a specific Comment under a BlogPost.")
    public Response deleteCommentFromBlogPost(@PathParam("blogPostID") Long blogPostID, @PathParam("commentID") Long commentID) {
        try {
            CommentDTO deletedComment = commentService.deleteCommentFromBlog(blogPostID, commentID);
            return Response.ok(deletedComment).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    @GET
    @Path("/{blogPostID}/comment/count")
    @RolesAllowed({"admin", "moderator"})
    @Operation(summary = "Count Comments for a BlogPost", description = "Returns the total number of Comments for a specific BlogPost.")
    public Response countCommentsForBlogPost(@PathParam("blogPostID") Long blogPostID) {
        try {
            Long count = commentService.countCommentsFromBlogPost(blogPostID);
            return Response.ok(count).build();
        } catch (NotFoundException e) {
            return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
        } catch (DatabaseException e) {
            return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
