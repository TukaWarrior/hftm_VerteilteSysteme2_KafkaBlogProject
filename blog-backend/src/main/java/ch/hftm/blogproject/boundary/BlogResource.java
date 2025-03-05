package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.jwt.JsonWebToken;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.BlogService;
import ch.hftm.blogproject.model.dto.BlogDTO;
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

@Path("/blog")
@Tag(name = "Blog Resource", description = "Blog Management API")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class BlogResource {

    @Inject
    BlogService blogService;
    @Inject
    JsonWebToken jsonWebToken;

    // Get all blogs
    @GET
    public Uni<Response> getBlogs() {
        return blogService.getAllBlogs()
            .onItem().transform(blogs -> Response.ok(blogs).build())
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blogs: " + e.getMessage()).build());
    }

    // Get a blog by ID
    @GET
    @Path("/{blogID}")
    public Uni<Response> getBlog(@PathParam("blogID") Long blogID) {
        return blogService.getBlogById(blogID)
            .onItem().transform(blogDTO -> Response.ok(blogDTO).build()) // Return 200 OK with the BlogDTO
            .onFailure(NotFoundException.class).recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage()).build()) // Return 404 if NotFoundException is thrown
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blog: " + e.getMessage()).build()); // Catch all other errors
    }

    // Add a new blog
    @POST
    public Uni<Response> addBlog(BlogDTO bloDTO) {
        // Set the creator of the blog from the JWT token
        // bloDTO.setCreator(jsonWebToken.getName());
        return blogService.addBlog(bloDTO)
            .onItem().transform(createdBlog -> Response.status(Response.Status.CREATED).entity(createdBlog).build()) // Return 201 Created
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error creating blog: " + e.getMessage()).build());
    }

    // Delete a blog by ID
    @DELETE
    @Path("/{blogID}")
    public Uni<Response> deleteBlog(@PathParam("blogID") Long blogID) {
        return blogService.deleteBlog(blogID)
            .onItem().transform(ignored -> Response.noContent().build()) // Return 204 No Content on success
            .onFailure(NotFoundException.class).recoverWithItem(e -> Response.status(Response.Status.NOT_FOUND)
                .entity(e.getMessage()).build()) // Return 404 if NotFoundException is thrown
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting blog: " + e.getMessage()).build()); // Catch all other errors
    }

    // Delete all blogs
    @DELETE
    public Uni<Response> deleteAllBlogs() {
        return blogService.deleteAllBlogs()
            .onItem().transform(ignored -> Response.noContent().build()) // Return 204 No Content on success
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting all blogs: " + e.getMessage()).build());
    }

    // Count all blogs
    @GET
    @Path("/count")
    public Uni<Response> countBlogs() {
        return blogService.countBlogs()
            .onItem().transform(count -> Response.ok(count).build()) // Return 200 OK with the count
            .onFailure().recoverWithItem(e -> Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error counting blogs: " + e.getMessage()).build());
    }
}
