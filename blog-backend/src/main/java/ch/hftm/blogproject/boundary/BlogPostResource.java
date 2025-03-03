package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.BlogPostService;
import ch.hftm.blogproject.control.CommentService;
import ch.hftm.blogproject.model.dto.BlogPostDTO;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
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
    public Multi<BlogPostDTO> getBlogPosts() {
        return blogPostService.getBlogPosts();
    }

    @GET
    @Path("/{blogPostID}")
    public Uni<BlogPostDTO> getBLogPost(@PathParam("blogPostID") Long blogPostID) {
        return blogPostService.getBlogPostById(blogPostID);
    }

    @POST
    public Uni<BlogPostDTO> addBlogPost(BlogPostDTO blogPostDTO) {
        return blogPostService.addBlogPost(blogPostDTO);
    }

    // @GET
    // @Operation(summary = "Get all BlogPosts", description = "Returns a list of BlogPosts with optional search and pagination.")
    // public Response OLDgetAllBlogPosts(@QueryParam("searchString") Optional<String> searchString, @QueryParam("page") Optional<Integer> page) {
    //     try {
    //         List<BlogPostDTO> blogPosts = blogPostService.OLDgetBlogPosts(searchString, page);
    //         return Response.ok(blogPosts).build();
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @GET
    // @Path("/{blogPostID}")
    // @Operation(summary = "Get one BlogPost by ID", description = "Returns a BlogPost by its ID.")
    // public Response getBlogPost(@PathParam("blogPostID") Long blogPostID) {
    //     try {
    //         BlogPostDTO blogPost = blogPostService.OLDgetBlogPostById(blogPostID);
    //         return Response.ok(blogPost).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @POST
    // @Operation(summary = "Add one new BlogPost", description = "Creates a new BlogPost")
    // public Response addBlogPost(@Valid BlogPostDTO blogPostDTO) {
    //     try {
    //         String creator = jsonWebToken.getName(); 
    //         blogPostDTO.setCreator(creator);

    //         BlogPostDTO createdBlogPost = blogPostService.OLDaddBlogPost(blogPostDTO);
    //         return Response.status(Response.Status.CREATED).entity(createdBlogPost).build();
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // Put and patch are the same for now.
    // @PUT
    // @Path("/{blogPostID}")
    // @Operation(summary = "Update a BlogPost", description = "Updates an existing BlogPost")
    // public Response putBlogPost(@PathParam("blogPostID") Long blogPostID, BlogPostDTO blogPostDTO) {
    //     try {
    //         blogPostDTO.setBlogPostID(blogPostID);
    //         BlogPostDTO updatedBlogPost = blogPostService.OLDputBlogPost(blogPostDTO);
    //         return Response.ok(updatedBlogPost).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }
    
    // Put and patch are the same for now.
    // @PATCH
    // @Path("/{blogPostID}")
    // @Operation(summary = "Partially Update a BlogPost", description = "Partially updates an existing BlogPost")
    // public Response patchBlogPost(@PathParam("blogPostID") Long blogPostID, BlogPostDTO blogPostDTO) {
    //     try {
    //         blogPostDTO.setBlogPostID(blogPostID);
    //         BlogPostDTO updatedBlogPost = blogPostService.OLDputBlogPost(blogPostDTO);
    //         return Response.ok(updatedBlogPost).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @DELETE
    // @Path("/{blogPostID}")
    // @Operation(summary = "Delete one BlogPost by ID", description = "Deletes an existing BlogPost")
    // public Response deleteBlogPost(@PathParam("blogPostID") Long id) {
    //     try {
    //         BlogPostDTO deletedBlogPost = blogPostService.OLDdeleteBlogPost(id);
    //         return Response.ok(deletedBlogPost).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @DELETE
    // @Operation(summary = "Delete all BlogPosts", description = "Deletes all BlogPosts")
    // public Response deleteAllBlogPosts() {
    //     try {
    //         blogPostService.OLDdeleteAllBlogPosts();
    //         return Response.noContent().build();
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @GET
    // @Path("/count")
    // @Operation(summary = "Count BlogPosts", description = "Returns the total number of BlogPosts in the system.")
    // public Response countBlogPosts() {
    //     try {
    //         Long count = blogPostService.countBlogPosts();
    //         return Response.ok(count).build();
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    private Response buildErrorResponse(Response.Status status, String message) {
        return Response.status(status).entity(message).build();
    }

// Comments
    // @GET
    // @Path("/{blogPostID}/comment")
    // @Operation(summary = "Get all Comments for a BlogPost", description = "Returns a list of Comments for a specific BlogPost.")
    // public Response getCommentsForBlogPost(@PathParam("blogPostID") Long blogPostID) {
    //     try {
    //         List<CommentDTO> comments = commentService.getAllCommentsOfBlogPost(blogPostID);
    //         return Response.ok(comments).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @POST
    // @Path("/{blogPostID}/comment")
    // @Operation(summary = "Add one Comment to a BlogPost", description = "Adds a new Comment to a specific BlogPost.")
    // public Response addCommentToBlogPost(@PathParam("blogPostID") Long blogPostID, @Valid CommentDTO commentDTO) {
    //     try {
    //         String creator = jsonWebToken.getName();
    //         commentDTO.setCreator(creator);
    //         CommentDTO createdComment = commentService.addCommentToBlog(blogPostID, commentDTO);
    //         return Response.status(Response.Status.CREATED).entity(createdComment).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @GET
    // @Path("/{blogPostID}/comment/{commentID}")
    // @Operation(summary = "Get a Comment by ID from a BlogPost", description = "Returns a Comment by its ID under a specific BlogPost.")
    // public Response getCommentFromBlogPost(@PathParam("blogPostID") Long blogPostID, @PathParam("commentID") Long commentID) {
    //     try {
    //         CommentDTO comment = commentService.getCommentFromBlogPost(blogPostID, commentID);
    //         return Response.ok(comment).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @PUT
    // @Path("/{blogPostID}/comment/{commentID}")
    // @Operation(summary = "Update a Comment under a BlogPost", description = "Updates an existing Comment under a specific BlogPost.")
    // public Response updateCommentFromBlogPost(@PathParam("blogPostID") Long blogPostID, @PathParam("commentID") Long commentID, CommentDTO commentDTO) {
    //     try {
    //         commentDTO.setCommentID(commentID); // Ensure the ID in DTO is the same as the path ID
    //         CommentDTO updatedComment = commentService.updateCommentOfBlog(blogPostID, commentDTO);
    //         return Response.ok(updatedComment).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @DELETE
    // @Path("/{blogPostID}/comment/{commentID}")
    // @Operation(summary = "Delete a Comment from a BlogPost", description = "Deletes a specific Comment under a BlogPost.")
    // public Response deleteCommentFromBlogPost(@PathParam("blogPostID") Long blogPostID, @PathParam("commentID") Long commentID) {
    //     try {
    //         CommentDTO deletedComment = commentService.deleteCommentFromBlog(blogPostID, commentID);
    //         return Response.ok(deletedComment).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }

    // @GET
    // @Path("/{blogPostID}/comment/count")
    // @Operation(summary = "Count Comments for a BlogPost", description = "Returns the total number of Comments for a specific BlogPost.")
    // public Response countCommentsForBlogPost(@PathParam("blogPostID") Long blogPostID) {
    //     try {
    //         Long count = commentService.countCommentsFromBlogPost(blogPostID);
    //         return Response.ok(count).build();
    //     } catch (NotFoundException e) {
    //         return buildErrorResponse(Response.Status.NOT_FOUND, e.getMessage());
    //     } catch (DatabaseException e) {
    //         return buildErrorResponse(Response.Status.INTERNAL_SERVER_ERROR, e.getMessage());
    //     }
    // }
}
