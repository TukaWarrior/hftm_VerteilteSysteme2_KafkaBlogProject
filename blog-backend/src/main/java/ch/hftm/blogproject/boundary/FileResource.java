package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FilePart;

import ch.hftm.blogproject.control.FileService;
import ch.hftm.blogproject.model.dto.FileDTO;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/file")
@Tag(name = "File Resource", description = "API to manage files")
public class FileResource {
    
    @Inject
    FileService fileService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@RestForm FilePart fileUpload) {
        try {
            FileDTO savedFile = fileService.uploadFile(fileUpload);
            return Response.status(Response.Status.CREATED).entity(savedFile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error uploading file: " + e.getMessage()).build();
        }
    }

    // @GET
    // @Path("/{id}")
    // @Produces(MediaType.APPLICATION_OCTET_STREAM)
    // public Response downloadFile(@PathParam("id") Long id) {
    //     try {
    //         FileDTO fileDTO = fileService.getFileWithData(id);
    //         return Response.ok(fileDTO.getFileData())
    //             .header("Content-Disposition", "attachment; filename=\"" + fileDTO.getFileName() + "\"")
    //             .build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.NOT_FOUND).entity("Error downloading file: " + e.getMessage()).build();
    //     }
    // }

    // @GET
    // @Path("/{id}/metadata")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getFileMetadata(@PathParam("id") Long id) {
    //     try {
    //         FileDTO fileDTO = fileService.getFileMetadata(id);
    //         return Response.ok(fileDTO).build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.NOT_FOUND).entity("Error retrieving metadata: " + e.getMessage()).build();
    //     }
    // }

    // @DELETE
    // @Path("/{id}")
    // public Response deleteFile(@PathParam("id") Long id) {
    //     try {
    //         fileService.deleteFile(id);
    //         return Response.noContent().build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting file: " + e.getMessage()).build();
    //     }
    // }
}
