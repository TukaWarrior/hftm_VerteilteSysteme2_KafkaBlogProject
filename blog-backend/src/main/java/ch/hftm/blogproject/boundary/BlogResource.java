package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.BlogService;
import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
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

    // Get all blogs
    @GET
    public Response getBlogs() {
        try {
            return Response.ok(blogService.getAllBlogs()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blogs: " + e.getMessage()).build();
        }
    }

    // Get a blog by ID
    @GET
    @Path("/{blogID}")
    public Response getBlog(@PathParam("blogID") Long blogID) {
        try {
            return Response.ok(blogService.getBlogById(blogID)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blog: " + e.getMessage()).build();
        }
    }

    // Add a new blog
    @POST
    @Operation(summary = "Save a new blog", description = "Add a new blog post to the database. When successful, returns the created blog.")
    @RequestBody(description = "Blog JSON. `title`, `content` and `creator` are required. `id`, `createdAt`, `lastChangedAt` and `validationStatus` are hidden and automatically generated.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation=BlogDTO.class), example = "{\"title\":\"This is the title of the blog\",\"content\":\"This is the content of the blog.\",\"creator\":\"ExampleUser\"}"))
    public Response addBlog(@Valid BlogDTO blogDTO) {
        try {
            BlogDTO createdBlog = blogService.addBlog(blogDTO);
            return Response.status(Response.Status.CREATED).entity(createdBlog).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error creating blog: " + e.getMessage()).build();
        }
    }

    @PATCH
    @Path("/{blogID}")
    @Operation(summary = "Patch a blog", description = "Change the content of an existing blog in the database. When successful, returns the updated blog.")
    @RequestBody(description = "Blog JSON. `title`, `content` and `creator` are required. `id` is not required. `createdAt`, `lastChangedAt` and `validationStatus` are hidden and automatically generated.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation=BlogDTO.class), example = "{\"title\":\"This is the updated title of the blog\",\"content\":\"This is the updated content of the blog.\",\"creator\":\"UpdatedExampleUser\"}"))
    public Response patchBlog(Long blogID, @Valid BlogDTO blogDTO) {
        try {
            BlogDTO updatedBlog = blogService.updateBlog(blogID, blogDTO);
            return Response.ok(updatedBlog).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error updating blog: " + e.getMessage()).build();
        }
    }

    // Delete a blog by ID
    @DELETE
    @Path("/{blogID}")
    public Response deleteBlog(@PathParam("blogID") Long blogID) {
        try {
            blogService.deleteBlog(blogID);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting blog: " + e.getMessage()).build();
        }
    }

    // Delete all blogs
    @DELETE
    public Response deleteAllBlogs() {
        try {
            blogService.deleteAllBlogs();
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error deleting all blogs: " + e.getMessage()).build();
        }
    }

    // Count all blogs
    @GET
    @Path("/count")
    public Response countBlogs() {
        try {
            return Response.ok(blogService.countBlogs()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error counting blogs: " + e.getMessage()).build();
        }
    }
}