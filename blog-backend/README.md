# blog-backend
This project is part of the "Distributed Systems 2" course from the third year of study at the HFTM in Grenchen.

It contains two parts:
1. A demo showing how to implement a separate validation service.
    It provides a http endpoint to manage blog posts. 
    It validates these blogposts by using a separate application. Commnication is done using apache kafka. 
2. A demo showing how to implement a storage bucket for file upload and download. 
    It utilises a locally running minIO instance as a storage bucket. Files and their metadata can be accessed via an http endpoint. 

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

# Accessing the Webviews
After the project has been started, the following two links should be accessible:

**Dev UI:**         http://localhost:8080/q/dev/

**Swagger UI:**     http://localhost:8080/q/swagger-ui/

**Others:**          To access the webUI for the MinIO storage bucket or the mysql database, use the links generated in the quarkus dev ui. 

# Testing the application - Kafka Communication for blogs 

After starting up booth applications and entering swagger UI, you can access these http endpoints.

**Blog http endpoint**

| **HTTP Method** | **Path**         | **Description**                                   |
| --------------- | ---------------- | ------------------------------------------------- |
| `GET`           | `/blog`          | Fetch all blogs from the database.                |
| `GET`           | `/blog/{blogID}` | Fetch a specific blog by its ID.                  |
| `POST`          | `/blog`          | Add a new blog to the database.                   |
| `PATCH`         | `/blog/{blogID}` | Update the content of an existing blog by its ID. |
| `DELETE`        | `/blog/{blogID}` | Delete a specific blog by its ID.                 |
| `DELETE`        | `/blog`          | Delete all blogs from the database.               |
| `GET`           | `/blog/count`    | Count the total number of blogs in the database.  |

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


# Testing the application - Storage bucket for files
Files uploaded to the application get stored in a local storage bucked, created using MinIO. Each file uploaded has a corresponding FileEntity stored in a mysql database that contains important metadata. This is not necessary, but makes searches for specific attributes and sorting much easier, as MinIo is not designed like a typical database. 
A FileEntity contains the primary key used in MinIOs storage bucket to identify each file. 

After starting up the application and entering swagger UI, you can access these http endpoints.

**File Endpoint**

| **HTTP Method** | **Path**              | **Description**                                                                               |
| --------------- | --------------------- | --------------------------------------------------------------------------------------------- |
| `GET`           | `/file`               | Fetch the metadata of all files from the database.                                            |
| `GET`           | `/file/{id}`          | Downloads a specific file by its id from the storage bucket.                                  |
| `GET`           | `/file/{id}/metadata` | Fetch a specific files metadata by its id from the database.                                  |
| `POST`          | `/file`               | Upload a new file to the storage bucket. its metadata gets stored in the database.            |
| `DELETE`        | `/file/{id}`          | Delete a specific file by its id from the storage bucket, and its metadata from the database. |
| `GET`           | `/file/count`         | Count the total number of files in the database.                                              |

**Example File Endpoint**

There is also an additonal endpoint to upload files. Its implementation is much simpler, as it does not store metadata to the files in a separate database. 
Its code is also not separated by concerns as nicely, but it serves as a short example. 
| **HTTP Method** | **Path**                 | **Description**                                                   |
| --------------- | ------------------------ | ----------------------------------------------------------------- |
| `GET`           | `/examplefile`           | Download a specific file by its fileName from the storage bucket. |
| `POST`          | `/examplefile{fileName}` | Upload a new file to the storage bucket.                          |

## Things to know about implementing file storage
While completing this small project, I encountered several challenges in regards to file storage. 

**Input Streams**
The methods to upload and download files from the storage bucket take and return InputStreams. 
Streams have to be handlecd carefully so it is ensured that they are closed after usage.
The easiest way to implement a file upload is to directly initialize an InputStream at the http endpoint and passing it to the method uploading the file to the storage bucket. 
For a proper separation of concerns however, it is impractical to pass InputStreams around across the application. Passing them from the http resource layer, to the service layer, to the storage layer, creates cahallenges in managing their lifecycles. 
There are a few solutions, that each have a drawback however. 

- A: Streams could be managed directly in the storage layer, however then the file data needs to be passed to it. Files smaller than 10MB can easily be stored in a byte array and wrapped in a container entity. However, doing so with larger files massively increases the memory usage of the application. 
- B: Streams could be managed directly in the storage layer, but instead of storing the files data in a byte array, they are written to a temporay location on the disk, when the data is recieved by either the http endpoint or the storage layer from the storage bucket. The files location is then stored in an entity class, which could also hold additional attributes. When the data needs to be sent to an http endpoint or to the storage bucket, the file is read from the disk and uploaded into an InputStream. This methods is more error prone and adds a significant delay as it requires an io operation to the disk, whci his significantly slower than writing to memory. 
- C: Instead of using InputStreams, OutputStreams are used in the storage layer. By doing so, the steaming logic is fully encapsulated in the storage layer, which will take full responsibility for handling the steaming process. This reduces the risk of resource leaks as it handles closure even during errors or client disconnects. This seems to be the overall best solution for this specific usecase. However, OutputStreams are tied to JAX_RS Streaming interface and require a slightly more complex implementation. 

**File Size Limits**
While MinIo itself has generous file size limits, passing large ammount of data in a http body is not recommended and even bloced by many servers and clients. Transfering files larger than 10MB or multiple files at once should be implemented using MultiPart File uploads and downloads. 
Due to time constraints in this project, I did not manage to implement these features in time. 


**MinIO Primary Key**
MinIo uses the fileName aka. the storage key as the primary key for each file in a storage bucket. This does not pose a problem but it works a bit different that a conventional databases. 
 
# Useful Links
MiniIO Application Properties Configuratio Reference

- https://docs.quarkiverse.io/quarkus-minio/dev/index.html#_native_support_only
- https://min.io/docs/minio/linux/developers/java/API.html
- https://min.io/docs/minio/linux/developers/java/API.html



