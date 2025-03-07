# text-validator

The Blog Validation Service is a Quarkus-based microservice that validates blogs from the blog-backend for profanity. 
It communicates using apache kafka. 
It listens to a kafka topic for incomming blog validation requests, processes the content, and sends validation responses to another kafka topic.

# Github Package
Install from the command line:
```sh
docker pull ghcr.io/tukawarrior/text-validator:latest
```
or 

Download from this URL:

ghcr.io/tukawarrior/text-validator


# Running the application in dev mode

You can run your application in dev mode: 

```shell script
./mvnw quarkus:dev
```
Quarkus dev UI: 
http://localhost:8080/q/dev/

# Delay for testing
The validation process has a build in delay from 10 seconds. This is used to make it visible in the blog backend that the blogs are first persisted with the validation status set to false. The delay gives time for the user to retrieve the blogs while the validation response is still missing. 

To disable the delay, comment out the following code in BlogValidationService.java
```java
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
        }
```