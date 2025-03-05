package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.BlogPostService;
import ch.hftm.blogproject.model.dto.BlogPostDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
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

@Path("/blogpost")
@Tag(name = "BlogPost Resource", description = "BlogPost Management API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BlogPostResource {

    @Inject
    BlogPostService blogPostService;
    @Inject
    JsonWebToken jsonWebToken;

    // Get all blog posts
    @GET
    public Uni<Response> getBlogPosts() {
        return blogPostService.getAllBlogPosts()
            .onItem().transform(blogPosts -> Response.ok(blogPosts).build())
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blog posts: " + e.getMessage()).build());
    }

    // Get a blog post by ID
    @GET
    @Path("/{blogPostID}")
    public Uni<Response> getBlogPost(@PathParam("blogPostID") Long blogPostID) {
        return blogPostService.getBlogPostById(blogPostID)
            .onItem().transform(blogPostDTO -> Response.ok(blogPostDTO).build()) // Return 200 OK with the BlogPostDTO
            .onFailure(NotFoundException.class).recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage()).build()) // Return 404 if NotFoundException is thrown
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blog post: " + e.getMessage()).build()); // Catch all other errors
    }

    // Add a new blog post
    @POST
    public Uni<Response> addBlogPost(BlogPostDTO blogPostDTO) {
        // Set the creator of the blog post from the JWT token
        blogPostDTO.setCreator(jsonWebToken.getName());
        return blogPostService.addBlogPost(blogPostDTO)
            .onItem().transform(createdBlogPost -> Response.status(Response.Status.CREATED).entity(createdBlogPost).build()) // Return 201 Created
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error creating blog post: " + e.getMessage()).build());
    }

    // Delete a blog post by ID
    @DELETE
    @Path("/{blogPostID}")
    public Uni<Response> deleteBlogPost(@PathParam("blogPostID") Long blogPostID) {
        return blogPostService.deleteBlogPost(blogPostID)
            .onItem().transform(ignored -> Response.noContent().build()) // Return 204 No Content on success
            .onFailure(NotFoundException.class).recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage()).build()) // Return 404 if NotFoundException is thrown
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting blog post: " + e.getMessage()).build()); // Catch all other errors
    }

    // Delete all blog posts
    @DELETE
    public Uni<Response> deleteAllBlogPosts() {
        return blogPostService.deleteAllBlogPosts()
            .onItem().transform(ignored -> Response.noContent().build()) // Return 204 No Content on success
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting all blog posts: " + e.getMessage()).build());
    }

    // Count all blog posts
    @GET
    @Path("/count")
    public Uni<Response> countBlogPosts() {
        return blogPostService.countBlogPosts()
            .onItem().transform(count -> Response.ok(count).build()) // Return 200 OK with the count
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error counting blog posts: " + e.getMessage()).build());
    }
}
