package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import ch.hftm.blogproject.model.entity.File;
import ch.hftm.blogproject.util.FileMapper;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/file")
@Tag(name = "File Resource", description = "API to manage files")
// @Consumes(MediaType.APPLICATION_JSON)
// @Produces(MediaType.APPLICATION_JSON)
public class FileResource {
    
    // @GET
    // @Path("/{fileId}")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getFileMetadata(@PathParam("fileId") Long fileId) {
    //     try {
    //         File file = fileService.getFile(fileId);
    //         return Response.ok(FileMapper.toFileDTO(file)).build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
    //             .entity("Error getting file: " + e.getMessage()).build();
    //     }
    // }
}
