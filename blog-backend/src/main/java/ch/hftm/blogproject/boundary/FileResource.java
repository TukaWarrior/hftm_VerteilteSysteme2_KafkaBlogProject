package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.control.FileService;
import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import jakarta.inject.Inject;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("/file")
@Tag(name = "File Resource", description = "API to manage files")
// @Consumes(MediaType.APPLICATION_JSON)
// @Produces(MediaType.APPLICATION_JSON)
public class FileResource {
    
    @Inject
    FileService fileService;

    @POST
    public Response saveFile(FileDTO fileDTO) {
        try {
            FileDTO savedFile = fileService.saveFile(fileDTO);
            return Response.status(Response.Status.CREATED).entity(savedFile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}")
    public Response getFileById(@PathParam("id") Long id) {
        try {
            FileDTO fileDTO = fileService.getFileById(id);
            return Response.ok(fileDTO).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/search")
    public Response getFileByFilename(@QueryParam("filename") String filename) {
        try {
            FileDTO fileDTO = fileService.getFileByFilename(filename);
            return Response.ok(fileDTO).build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @GET
    @Path("/count")
    public Response countFiles() {
        try {
            long count = fileService.countFiles();
            return Response.ok(count).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response deleteFile(@PathParam("id") Long id) {
        try {
            fileService.deleteFile(id);
            return Response.noContent().build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e.getMessage()).build();
        }
    }
}
