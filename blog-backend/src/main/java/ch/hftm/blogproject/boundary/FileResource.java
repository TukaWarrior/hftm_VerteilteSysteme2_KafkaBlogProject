package ch.hftm.blogproject.boundary;

import org.eclipse.microprofile.openapi.annotations.tags.Tag;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileDownload;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import ch.hftm.blogproject.control.FileService;
import ch.hftm.blogproject.model.dto.FileMetadataDTO;
import ch.hftm.blogproject.model.exception.DatabaseException;
import ch.hftm.blogproject.model.exception.NotFoundException;
import ch.hftm.blogproject.util.ExceptionMapper;
import io.minio.credentials.AssumeRoleBaseProvider;
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

    // ========================================| Post Endpoints ========================================
    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@RestForm FileUpload fileUpload) {
        try {
            FileMetadataDTO savedFile = fileService.uploadFile(fileUpload);
            return Response.status(Response.Status.CREATED).entity(savedFile).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(exceptionMapper.toExceptionDTO(e)).build();
        }
    }

    // ========================================| Get Endpoints |========================================
    @GET
    @Path("/metadata")
    public Response getAllFiles() {
        try {
            return Response.status(Response.Status.OK).entity(fileService.getAllFiles()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error fetching blogs: " + e.getMessage()).build();
        }
    }

    @GET
    @Path("/{id}/metadata")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFileMetadata(@PathParam("id") Long id) {
        try {
            FileMetadataDTO fileMetadataDTO = fileService.getFileMetadataById(id);
            return Response.status(Response.Status.OK).entity(fileMetadataDTO).build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Error retrieving metadata: " + e.getMessage()).build();
        }
    }

    // Currently NOT WORKING Error: Not Found
    // @GET
    // @Path("/{filename}/metadata")
    // @Produces(MediaType.APPLICATION_JSON)
    // public Response getFileMetadata(@PathParam("filename") String filename) {
    //     try {
    //         FileMetadataDTO fileMetadataDTO = fileService.getFileMetadataByFileName(filename);
    //         return Response.status(Response.Status.OK).entity(fileMetadataDTO).build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.NOT_FOUND).entity("Error retrieving metadata: " + e.getMessage()).build();
    //     }
    // }

    // @GET
    // @Path("/{id}")
    // @Produces(MediaType.APPLICATION_OCTET_STREAM)
    // public Response downloadFile(@PathParam("id") Long id) {
    //     try {
    //         FileDownload fileDownload = fileService.
    //         return Response.status(Response.Status.OK).entity().build();
    //     } catch (Exception e) {
    //         return Response.status(Response.Status.NOT_FOUND).entity("Error downloading file").build();
    //     }
    // }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("id") Long id) {
        try {
            // Get metadata with download stream
            FileMetadataDTO fileMetadataDTO = fileService.downloadFile(id);

            // Build proper HTTP response with headers
            return Response.ok(fileMetadataDTO.getDownloadStream())
                .header("Content-Disposition", "attachment; filename=\"" + fileMetadataDTO.getFileName() + "\"")
                .header("Content-Length", fileMetadataDTO.getFileSize())
                .build();
        } catch (NotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("File not found: " + e.getMessage())
                .header("Content-Type", "text/plain") // Set to text to prevent sending download headers. 
                .build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error downloading file: " + e.getMessage())
                .header("Content-Type", "text/plain")
                .build();
        }
    }

    // ========================================| Delete Endpoints |========================================
    @DELETE
    @Path("/{id}")
    public Response deleteFile(@PathParam("id") Long id) {
        try {
            fileService.deleteFile(id);
            return Response.status(Response.Status.NO_CONTENT).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error deleting file").build();
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
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error counting files.").build();
        }
    }
}

    
