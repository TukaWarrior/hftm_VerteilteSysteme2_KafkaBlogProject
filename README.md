# Overview
This project is part of the **Distributed Systems 2** course at the hftm. It aims to demonstrate communication between two applications using apache kafka.

The project contains two applications:
**blog-backend**: A blog backend application, using a mysql database and providing http endpoints to manage blogs. 
**text-validator**: A validation service that takes incomming blogs and checks if they contain profanity. 

Each application can be found in their respective subfolders. 

## Prerequisites
- [**Java 21**](https://adoptium.net/)
- [**Maven**](https://maven.apache.org/)
- [**Docker**](https://www.docker.com/) (must be running)

## Downloading the application
You can clone / download the repository. 

Alternatively, booth applications are avaiable as public github packages.

- [**blog-backend**](https://github.com/users/tukawarrior/packages/container/package/blog-backend)
- [**text-validator**](https://github.com/users/tukawarrior/packages/container/package/text-validator)


Install from the command line:
```sh
docker pull ghcr.io/tukawarrior/blog-backend:latest
docker pull ghcr.io/tukawarrior/text-validator:latest
```

## Running the applications

### Running the blog-backend:

1. Navigate to the `blog-backend` subfolder.

2. Run the application: 
    ```shell script
    ./mvnw quarkus:dev
    ```

3. Access the http endpoints using Swagger UI: 
http://localhost:8080/q/swagger-ui

### Running the text-validator:
1. Navigate to the `text-validator` subfolder.

2. Run the application: 
    ```shell script
    ./mvnw quarkus:dev
    ```

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

**Delay for testing**

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



# Notes:
These are my personal notes:

**Building a docker image:**
```sh
docker build -t ghcr.io/tukawarrior/blog-backend:latest -f src/main/docker/Dockerfile.jvm .
docker build -t ghcr.io/tukawarrior/text-validator:latest -f src/main/docker/Dockerfile.jvm .
```

**Publishing to GitHub Package Repository**
```sh
docker push ghcr.io/tukawarrior/blog-backend:latest
docker push ghcr.io/tukawarrior/text-validator:latest

```

**Logging in to GitHub using the access token:**

```
echo <token> | docker login ghcr.io -u <your-github-username> --password-stdin
```

**The packages:**
ghcr.io/tukawarrior/blog-backend
ghcr.io/tukawarrior/text-validator

Install from the command line:
```sh
docker pull ghcr.io/tukawarrior/blog-backend:latest
docker pull ghcr.io/tukawarrior/text-validator:latest
```
