# blog-backend
This project is part of the "Distributed Systems 2" course from the third year of study at the HFTM in Grenchen.
It provides a http endpoint to manage blog posts. 
It validates these blogposts by using a separate application. Commnication is done using apache kafka. 


# Github Package
Install from the command line:
```sh
docker pull ghcr.io/tukawarrior/blog-backend:latest
```
or 

Download from this URL:

ghcr.io/tukawarrior/blog-backend

# Running the application in dev mode

You can run your application in dev mode: 

```shell script
./mvnw quarkus:dev
```

# Accessing the Webview
After the project has been started, the following two links should be accessible:

**Dev UI:**         http://localhost:8080/q/dev/

**Swagger UI:**     http://localhost:8080/q/swagger-ui/


## Testing the application: 

After starting up booth applications and entering swagger UI, you can access these http endpoints.

| **HTTP Method** | **Path**                  | **Description**                                                                                   |
|------------------|---------------------------|---------------------------------------------------------------------------------------------------|
| `GET`           | `/blog`                  | Fetch all blogs from the database.                                                               |
| `GET`           | `/blog/{blogID}`         | Fetch a specific blog by its ID.                                                                 |
| `POST`          | `/blog`                  | Add a new blog to the database.                                                                  |
| `PATCH`         | `/blog/{blogID}`         | Update the content of an existing blog by its ID.                                                |
| `DELETE`        | `/blog/{blogID}`         | Delete a specific blog by its ID.                                                               |
| `DELETE`        | `/blog`                  | Delete all blogs from the database.                                                             |
| `GET`           | `/blog/count`            | Count the total number of blogs in the database.                                                 |

When creating a new blog, its validation status will be set to false. 
The backend will message the blog to the text-validator, which will check it against a list of banned words. 
If none of the banned words are present in the blogts title or content, the response will be valid, after which the blog-backend updates the validation status of the blog in the database. 

To demonstrate this more clearly, the validation process in the text-validator has a build in delay of 10 seconds. This gives the user time to post a blog, check that the validation status is set to false, and see it set to true 10 seconds later. Without a delay, these operations happen to fast to observe. 

To disable the delay, comment out the following code in BlogValidationService.java in the text-validator application.
```java
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
```



# Useful Links
MiniIO Application Properties Configuratio Reference
https://docs.quarkiverse.io/quarkus-minio/dev/index.html#_native_support_only
https://min.io/docs/minio/linux/developers/java/API.html

# Maby todo
Diagram with storage layer, business logic layer, http layer.


# Talking Points for presentation
MiniIo requires fileSize and fileTZype as separate parameters

File Name: Minio uses filename as unique identifier. Using the id of the file entity in mysql in minio filename requires an additional database call to retrieve the it, as the id gets set on the database when creating the entity. Solution is to use a uuid in minios filename. Alternatively, the upload date. 

File SIze Limit imposed by most HTTP Servers and Clients to about 10MB. Uploading greater files requires a multipart upload. 





In enterprise applications:
For large files (>10MB):

StreamingOutput is the recommended approach in JAX-RS
Clearly defined ownership of stream lifecycle
Single responsibility for stream management
For small/medium files:

ByteArray approach is often cleaner and safer
Eliminates stream management concerns
For files with unknown size:

Temporary file approach (save to disk first) offers a balance
Recommendation for Your Application
Based on your structure:

If your files are typically small (<10MB), use the byte array approach
If your files can be large, use StreamingOutput in your resource layer
Either way, minimize stream passing between methods