package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.BlogService;
import ch.hftm.blogproject.model.dto.BlogDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.util.ExceptionMapper;
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
public class BlogResource {

    @Inject
    BlogService blogService;
    @Inject
    ExceptionMapper exceptionMapper;

    // ========================================| Get Endpoints |========================================
    // Get all blogs
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBlogs() {
        try {
            return Response.status(Response.Status.OK).entity(blogService.getAllBlogs()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // Get a blog by ID
    @GET
    @Path("/{blogID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBlog(@PathParam("blogID") Long blogID) {
        try {
            return Response.ok(blogService.getBlogById(blogID)).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(exceptionMapper.toExceptionDTO(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Post Endpoints |========================================
    // Add a new blog
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Save a new blog", description = "Add a new blog post to the database. When successful, returns the created blog.")
    @RequestBody(description = "Blog JSON. `title`, `content` and `creator` are required. `id`, `createdAt`, `lastChangedAt` and `validationStatus` are hidden and automatically generated.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation=BlogDTO.class), example = "{\"title\":\"This is the title of the blog\",\"content\":\"This is the content of the blog.\",\"creator\":\"ExampleUser\"}"))
    public Response addBlog(@Valid BlogDTO blogDTO) {
        try {
            BlogDTO createdBlog = blogService.addBlog(blogDTO);
            return Response.status(Response.Status.CREATED).entity(createdBlog).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(exceptionMapper.toExceptionDTO(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Patch Endpoints |========================================
    @PATCH
    @Path("/{blogID}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Patch a blog", description = "Change the content of an existing blog in the database. When successful, returns the updated blog.")
    @RequestBody(description = "Blog JSON. `title`, `content` and `creator` are required. `id` is not required. `createdAt`, `lastChangedAt` and `validationStatus` are hidden and automatically generated.", required = true, content = @Content(mediaType = "application/json", schema = @Schema(implementation=BlogDTO.class), example = "{\"title\":\"This is the updated title of the blog\",\"content\":\"This is the updated content of the blog.\",\"creator\":\"UpdatedExampleUser\"}"))
    public Response patchBlog(Long blogID, @Valid BlogDTO blogDTO) {
        try {
            BlogDTO updatedBlog = blogService.updateBlog(blogID, blogDTO);
            return Response.status(Response.Status.OK).entity(updatedBlog).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Delete Endpoints |========================================
    // Delete a blog by ID
    @DELETE
    @Path("/{blogID}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBlog(@PathParam("blogID") Long blogID) {
        try {
            blogService.deleteBlog(blogID);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(exceptionMapper.toExceptionDTO(e)).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // Delete all blogs
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteAllBlogs() {
        try {
            blogService.deleteAllBlogs();
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Utility Endpoints |========================================
    // Count all blogs
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countBlogs() {
        try {
            return Response.status(Response.Status.OK).entity(blogService.countBlogs()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }
}