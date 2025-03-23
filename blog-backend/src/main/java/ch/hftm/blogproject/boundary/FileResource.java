package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.control.FileService;
import ch.hftm.blogproject.model.dto.FileDTO;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.util.ExceptionMapper;
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

@Path("/file")
@Tag(name = "File Resource", description = "API to manage files")
public class FileResource {
    
    @Inject
    FileService fileService;
    @Inject
    ExceptionMapper exceptionMapper;

    // ========================================| Get Endpoints |========================================
    @GET
    @Path("/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllFiles() {
        try {
            return Response.status(Response.Status.OK).entity(fileService.getAllFilesMetadata()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    @GET
    @Path("/{id}/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileMetadata(@PathParam("id") Long id) {
        try {
            FileDTO fileMetadataDTO = fileService.getFileMetadataById(id);
            return Response.status(Response.Status.OK).entity(fileMetadataDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("id") Long id) {
        try {
            // Get metadata with download stream
            FileDTO fileMetadataDTO = fileService.downloadFile(id);

            // Build proper HTTP response with headers
            return Response.ok(fileMetadataDTO.getDownloadStream())
                .header("Content-Disposition", "attachment; filename=\"" + fileMetadataDTO.getFileName() + "\"")
                .header("Content-Length", fileMetadataDTO.getFileSize())
                .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity(exceptionMapper.toExceptionDTO(e))
                .header("Content-Type", "application/json") // Set to text to prevent sending download headers. 
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(exceptionMapper.toExceptionDTO(e))
                .header("Content-Type", "application/json")
                .build();
        }
    }

    // ========================================| Post Endpoints |========================================
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@RestForm FileUpload fileUpload) {
        try {
            FileDTO savedFile = fileService.uploadFile(fileUpload);
            return Response.status(Response.Status.CREATED).entity(savedFile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Delete Endpoints |========================================
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteFile(@PathParam("id") Long id) {
        try {
            fileService.deleteFile(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Utility Endpoints |========================================
    @GET
    @Path("/count")
    @Produces(MediaType.APPLICATION_JSON)
    public Response countFiles() {
        try {
            long filecount = fileService.countFiles();
            return Response.status(Response.Status.OK).entity(filecount).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }
}

    
