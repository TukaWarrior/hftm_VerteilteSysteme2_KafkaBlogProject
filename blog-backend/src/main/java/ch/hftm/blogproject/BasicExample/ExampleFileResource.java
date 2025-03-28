package ch.hftm.blogproject.BasicExample;

import java.io.FileInputStream;
import java.io.InputStream;

import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.multipart.FileUpload;

import io.smallrye.common.annotation.Blocking;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;


@Path("/examplefiles")
public class ExampleFileResource {

    @Inject
    ExampleFileService exampleFileService;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Blocking // Required for blocking operations like file upload
    public Response uploadImage(@RestForm FileUpload fileUpload) {
        try {
            // Extract metadata from FileUpload
            String fileName = fileUpload.fileName(); // The name of the uploaded file
            long fileSize = fileUpload.size(); // The size of the uploaded file
            String contentType = fileUpload.contentType(); // The MIME type of the uploaded file

            // Get the uploaded file path and create an InputStream
            java.nio.file.Path uploadedFilePath = fileUpload.uploadedFile(); // Fully qualified name
            try (InputStream fileStream = new FileInputStream(uploadedFilePath.toFile())) {
                // Upload the file
                ExampleFile file = exampleFileService.uploadImage(fileName, fileStream, fileSize, contentType);
                return Response.status(Response.Status.CREATED).entity(file).build();
            }
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("Error uploading image: " + e.getMessage()).build();
        }
    }
    

    @GET
    @Path("/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Blocking // Required for blocking operations like file download
    public Response downloadImage(@PathParam("fileName") String fileName) {
        try {
            InputStream fileStream = exampleFileService.downloadImage(fileName);
            return Response.ok(fileStream).header("Content-Disposition", "attachment; filename=\"" + fileName + "\"").build();
        } catch (Exception e) {
            return Response.status(Response.Status.NOT_FOUND)
                .entity("Error downloading image: " + e.getMessage()).build();
        }
    }
}
