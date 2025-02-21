# Blog Project - Java Backend
This is my school project, that is part of the "Distributed Systems" course from the third year of study at the HFTM in Grenchen.
The goal is to build up a web-api backend, which I can utilise and extend in future courses. 
This project uses the java framework Quarkus.


<!-- [![Build Status](https://github.com/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject/actions/workflows/WORKFLOW_FILE/badge.svg)](https://github.com/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject/actions/workflows/WORKFLOW_FILE) -->
<!-- [![codecov](https://codecov.io/gh/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject/branch/main/graph/badge.svg)](https://codecov.io/gh/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject) -->
<!-- ![License](https://img.shields.io/github/license/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject) -->
[![Known Vulnerabilities](https://snyk.io/test/github/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject/badge.svg)](https://snyk.io/test/github/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject)
![GitHub issues](https://img.shields.io/github/issues/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject)
![GitHub pull requests](https://img.shields.io/github/issues-pr/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject)
![GitHub stars](https://img.shields.io/github/stars/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject?style=social)
![GitHub forks](https://img.shields.io/github/forks/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject?style=social)
![GitHub watchers](https://img.shields.io/github/watchers/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject?style=social)
<!-- ![GitHub release (latest by date)](https://img.shields.io/github/v/release/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject) -->

<!-- ![GitHub contributors](https://img.shields.io/github/contributors/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject) -->
<!-- [![Codacy Badge](https://app.codacy.com/project/badge/Grade/REPOSITORY_ID)](https://www.codacy.com/gh/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject/dashboard?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject&amp;utm_campaign=Badge_Grade) -->
<!-- [![GitHub Pages](https://img.shields.io/badge/GitHub-Pages-blue?logo=github)](https://TukaWarrior.github.io/hftm_VerteilteSysteme_Project_BlogProject) -->

<!-- [![Project Board](https://img.shields.io/badge/Project-Board-blue)](https://github.com/TukaWarrior/hftm_VerteilteSysteme_Project_BlogProject/projects) -->
---

# 📄 Table of Contents

1. [Introduction](#introduction)
2. [Starting the Application](#starting-the-application)
    - [Running in Production Mode](#running-in-production-mode)
    - [Running in Development Mode](#running-in-development-mode)
3. [Accessing the Webview](#accessing-the-webview)
4. [Keycloak Setup](#keycloak)
5. [Endpoints](#endpoints)
    - [Authentication](#authentication)
    - [BlogPosts](#blogposts)
    - [Comments](#comments)
6. [Miscellaneous](#miscellaneous)
    - [Grading](#grading)
    - [Roadmap](#roadmap)
    - [Important Files](#important-files)
    - [Useful Subsidiary Tools](#useful-subsidiary-tools)

# Starting the application
> [!IMPORTANT]  
> All commands in this documentation are meant to be run from the root of the project. 
> Make sure Docker is running and no containers with the same names, ports and docker network are present. 
> If you switch between dev and prod mode, it may be necessary to delete the existing containers and volume mappings in docker. 

## Running in production mode

1. Get the package from GitHub
    ```bash
    docker pull ghcr.io/tukawarrior/hftm_verteiltesysteme_project_blogproject:1.0.0-snapshot
    ```

2. Create a docker network.
    ```bash
    docker network create blogproject-nw
    ```

3. Start a MySQL container for persisting keycloak data. 
    ```bash
    docker run --name keycloak-mysql --network blogproject-nw -v keycloak-db:/var/lib/mysql -e MYSQL_ROOT_PASSWORD=vs4tw -e MYSQL_USER=dbuser -e MYSQL_PASSWORD=dbuser -e MYSQL_DATABASE=keycloakdb -d mysql:8.0
    ```

4. Start a keycloak container. 
    ```bash
    docker run --name keycloak --network blogproject-nw -v "$(pwd)\src\main\resources\keycloak\import:/opt/keycloak/data/import" -v "$(pwd)\src\main\resources\keycloak\export:/opt/keycloak/data/export" -e KEYCLOAK_ADMIN=admin -e KEYCLOAK_ADMIN_PASSWORD=admin -e KC_HTTP_PORT=8180 -e KC_HOSTNAME_URL=http://keycloak:8180 -p 8180:8180 -e KC_DB=mysql -e KC_DB_URL=jdbc:mysql://keycloak-mysql:3306/keycloakdb -e KC_DB_USERNAME=dbuser -e KC_DB_PASSWORD=dbuser -d quay.io/keycloak/keycloak:25.0.5 start-dev --import-realm
    ```
    Automatically imports realm and userdata.

5. Start a MySQL container for persisting quarkus application data. 
    ```bash
    docker run --name quarkus-blogproject-mysql --network blogproject-nw -v blogproject-db:/var/lib/mysql -p 3306:3306 -e MYSQL_ROOT_PASSWORD=vs4tw -e MYSQL_USER=dbuser -e MYSQL_PASSWORD=dbuser -e MYSQL_DATABASE=blogprojectdb -d mysql:8.0
    ```

6. When the other containers are fully operational, start the quarkus application container. 
    ```bash
    docker run --name quarkus-blogproject --network blogproject-nw -i --rm -p 8080:8080 lucab/ch.hftm/blogproject:1.0.0-SNAPSHOT
    ```
    If you see an `Connection refused` error, the other containers may not have startet up yet. Wait a few more seconds and try again. 

## Running in development mode
1. 
    ```bash
    ./mvnw quarkus:dev
    or 
    ./mvnw compile quarkus:dev
    ```
    The quarkus application, keycloak, MySQL for keycloak and MySQL for quarkus should automatically start.
    There will be an error at startup because quarkus hibernate couldn't drop certain tables. The application will work as intended. 


## Accessing the Webview
After the project has been started, the following two links should be accessible:

**Dev UI:**         http://localhost:8080/q/dev/
**Swagger UI:**     http://localhost:8080/q/swagger-ui/
**Keycloak:**       http://localhost:8180

# Keycloak

**Users**

| Username | Password | Role     |
|----------|----------|----------|
| alice    | alice    | admin    |
| bob      | bob      | moderator|
| carl     | carl     | user     |

**Roles**
- The user / authenticated can retrieve data and create content.
- The moderator can additionally edit and delete individual entries.
- The admin has access to all functionality, including bulk delte, count etc. 

**Client ID:** backend-service

**Client Secret:** secret

# Endpoints

### Authentication

<details>
<summary><b>Login User Anna (admin)</b></summary>

- **Type:** `GET`
- **Path:** `http://keycloak:8180/realms/blogproject/protocol/openid-connect/token`
- **Constraints:** `OAuth 2.0`, `username alice`, `password alice`, `client-id backend-service`, `client-secret secret` 
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    {
        "access_token": "eyJhb...",
        "expires_in": 1500,
        "refresh_expires_in": 1800,
        "refresh_token": "eyJhb...",
        "token_type": "Bearer",
        "not-before-policy": 0,
        "session_state": "a456512e-554e-4ae1-8dbc-2ab4f1e55131",
        "scope": "email profile"
    }
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http -f POST http://keycloak:8180/realms/blogproject/protocol/openid-connect/token \
    grant_type=password \
    client_id=backend-service \
    client_secret=secret \
    username=alice \
    password=alice
    ```
> [!Note]  
> If application is run unsing dev mode, use http://localhost:8180...

</details>

<details>
<summary><b>Login User Bob (moderator)</b></summary>

- **Type:** `GET`
- **Path:** `http://keycloak:8180/realms/blogproject/protocol/openid-connect/token`
- **Constraints:** `OAuth 2.0`, `username bob`, `password bob`, `client-id backend-service`, `client-secret secret` 
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    {
        "access_token": "eyJhb...",
        "expires_in": 1500,
        "refresh_expires_in": 1800,
        "refresh_token": "eyJhb...",
        "token_type": "Bearer",
        "not-before-policy": 0,
        "session_state": "a456512e-554e-4ae1-8dbc-2ab4f1e55131",
        "scope": "email profile"
    }
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http -f POST http://keycloak:8180/realms/blogproject/protocol/openid-connect/token \
    grant_type=password \
    client_id=backend-service \
    client_secret=secret \
    username=bob \
    password=bob
    ```
> [!Note]  
> If application is run unsing dev mode, use http://localhost:8180...
</details>

<details>
<summary><b>Login User Carl (user)</b></summary>

- **Type:** `GET`
- **Path:** `http://keycloak:8180/realms/blogproject/protocol/openid-connect/token`
- **Constraints:** `OAuth 2.0`, `username carl`, `password carl`, `client-id backend-service`, `client-secret secret` 
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    {
        "access_token": "eyJhb...",
        "expires_in": 1500,
        "refresh_expires_in": 1800,
        "refresh_token": "eyJhb...",
        "token_type": "Bearer",
        "not-before-policy": 0,
        "session_state": "a456512e-554e-4ae1-8dbc-2ab4f1e55131",
        "scope": "email profile"
    }
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http -f POST http://keycloak:8180/realms/blogproject/protocol/openid-connect/token \
    grant_type=password \
    client_id=backend-service \
    client_secret=secret \
    username=carl \
    password=carl
    ```
> [!Note]  
> If application is run unsing dev mode, use http://localhost:8180...
</details>

### BlogPosts

<details>
<summary><b>Get all BlogPost</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost`
- **Constraints:** `Authenticated`
- **Response types:** `200 OK`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    [
        {
            "blogPostID": 1,
            "comments": [
            {
                "blogPostID": 1,
                "commentID": 1,
                "content": "This is a new comment",
                "createdAt": "2024-09-18T17:41:59Z",
                "creator": "alice"
            }
            ],
            "createdAt": "2024-09-18T17:36:26Z",
            "creator": "alice",
            "lastChangedAt": "2024-09-18T17:37:19Z",
            "title": "Bruno replaced this content"
        },
        {
            "blogPostID": 2,
            "createdAt": "2024-09-18T17:36:27Z",
            "creator": "alice",
            "lastChangedAt": "2024-09-18T17:37:37Z",
            "title": "Bruno replaced this content"
        }
    ]
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http GET :8080/blogpost searchString=="keyword" page==1 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Get one BlogPost by ID</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost/{blogPostID}`
- **Constraints:** `Authenticated`
- **Response types:** `200 OK`, `404 NOT FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    {
    "blogPostID": 30,
    "comments": [
        {
        "blogPostID": 30,
        "commentID": 1,
        "content": "This is a new comment",
        "createdAt": "2024-09-18T17:41:59Z",
        "creator": "alice"
        }
    ],
    "createdAt": "2024-09-18T17:36:26Z",
    "creator": "alice",
    "lastChangedAt": "2024-09-18T17:37:19Z",
    "title": "Bruno replaced this content"
    }
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http GET :8080/blogpost/1 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Add one new BlogPost</b></summary>

- **Type:** `POST`
- **Path:** `host:port/blogpost`
- **Constraints:** `Authenticated`
- **Response types:** `201 CREATED`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http POST :8080/blogpost \
        title="New Blog Title" \
        content="This is the content of the new blog post" \
        Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Put one BlogPost by ID</b></summary>

- **Type:** `PUT`
- **Path:** `host:port/blogpost/{blogPostID}`
- **Constraints:** `moderator`, `admin`
- **Response types:** `200 OK` `404 NOT_FOUND` `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http PUT :8080/blogpost/1 \
        title="Updated Blog Title" \
        content="Updated content of the blog post" \
        Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Patch one BlogPost by ID</b></summary>

- **Type:** `PATCH`
- **Path:** `host:port/blogpost/{blogPostID}`
- **Constraints:** `moderator`, `admin`
- **Response types:** `200 OK`, `404 NOT FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http PATCH :8080/blogpost/1 \
        title="Partially Updated Blog Title" \
        Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Delete one BlogPost by ID</b></summary>

- **Type:** `DELETE`
- **Path:** `host:port/blogpost/{blogPostID}`
- **Constraints:** `moderator`, `admin`
- **Response types:** `200 OK`, `404 NOT FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http DELETE :8080/blogpost/1 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Delete all BlogPosts</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost`
- **Constraints:** `admin`
- **Response types:** `200 OK`, `404 NOT FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http DELETE :8080/blogpost Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Count all BlogPosts</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost/count`
- **Constraints:** `admin`
- **Response types:** `200 OK`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    1
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http GET :8080/blogpost/count Authorization:"Bearer <your_token>"
    ```
</details>

### Comments

<details>
<summary><b>Get all Comments</b></summary>

- **Type:** `GET`
- **Path:** `host:port/comment`
- **Constraints:** `Authenticated`
- **Response types:** `200 OK`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    [
        {
            "blogPostID": 1,
            "commentID": 1,
            "content": "This is a new comment",
            "createdAt": "2024-09-18T17:41:59Z",
            "creator": "alice"
        },
        {
            "blogPostID": 2,
            "commentID": 2,
            "content": "This is a new comment",
            "createdAt": "2024-09-18T17:54:53Z",
            "creator": "alice"
        }
    ]
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http -v GET :8080/comment searchString=="some search term" page==1 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Get one Comment by ID</b></summary>

- **Type:** `GET`
- **Path:** `host:port/comment`
- **Constraints:** `Authenticated`
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    {
    "blogPostID": 1,
    "commentID": 1,
    "content": "This is a new comment",
    "createdAt": "2024-09-18T17:54:53Z",
    "creator": "alice"
    }
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http -v GET :8080/comment/1 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Count all Comments</b></summary>

- **Type:** `GET`
- **Path:** `host:port/comment/count`
- **Constraints:** `admin`
- **Response types:** `200 OK`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    1
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http -v GET :8080/comment/count Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Get all Comments of a BlogPost by blogPostID</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost/{blogPostID}/comment`
- **Constraints:** `Authenticated`
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    [
        {
            "blogPostID": 30,
            "commentID": 1,
            "content": "This is a new comment",
            "createdAt": "2024-09-18T17:41:59Z",
            "creator": "alice"
        },
        {
            "blogPostID": 30,
            "commentID": 2,
            "content": "This is a new comment",
            "createdAt": "2024-09-18T17:49:33Z",
            "creator": "alice"
        }
    ]
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http GET :8080/blogpost/1/comment Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Get one Comment by CommentID from one BlogPost by BlogPostID</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost/{blogPostID}/comment/{commentID}`
- **Constraints:** `Authenticated`
- **Response types:** `200 OK` `404 NOT_FOUND` `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    {
    "blogPostID": 30,
    "commentID": 1,
    "content": "This is a new comment",
    "createdAt": "2024-09-18T17:41:59Z",
    "creator": "alice"
    }
    ```
- **Example request with HTTPie CLI:**
    ```sh
    http GET :8080/blogpost/1/comment/2 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Add one Comment to a BlogPost by BlogPostID</b></summary>

- **Type:** `POST`
- **Path:** `host:port/blogpost/{blogPostID}/comment`
- **Constraints:** `Authenticated`
- **Response types:** `201 CREATED`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http POST :8080/blogpost/1/comment \
        content="This is a new comment" \
        Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Update one Comment by CommentID of one BlogPost by blogPostID</b></summary>

- **Type:** `PUT`
- **Path:** `host:port/blogpost/{blogPostID}/comment`
- **Constraints:** `moderator`, `admin`
- **Response types:** `201 CREATED`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http PUT :8080/blogpost/1/comment/2 \
        content="Updated comment content" \
        Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Delete one Comment by CommentID of one BlogPost by blogPostID</b></summary>

- **Type:** `DELETE`
- **Path:** `host:port/blogpost/{blogPostID}/comment`
- **Constraints:** `moderator`, `admin`
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example request with HTTPie CLI:**
    ```sh
    http DELETE :8080/blogpost/1/comment/2 Authorization:"Bearer <your_token>"
    ```
</details>

<details>
<summary><b>Count all Comments of a BlogPost by blogPostID</b></summary>

- **Type:** `GET`
- **Path:** `host:port/blogpost/{blogPostID}/comment/count`
- **Constraints:** `moderator`, `admin`
- **Response types:** `200 OK`, `404 NOT_FOUND`, `500 INTERNAL_SERVER_ERROR`
- **Example response body:**
    ```json
    1
    ```
- **Request with HTTPie CLI:**
    ```sh
    http GET :8080/blogpost/1/comment/count Authorization:"Bearer <your_token>"
    ```
</details>

# Miscellaneous
## Grading
The grading of this project will be done by collaborator simeonlin.
This project will be randomly sampled and graded after every tasks.
Each sample will be assessed according to the following scheme with a maximum of 4 points:
* Initial repository status / starting point before the last task (max. 2 points)
    * ✅ 2 points: Suitable project setup and evidence of initiative.
    * ☑️ 1 point: Suitable project setup, but little evidence of initiative. (e.g., Only commits with sample solutions)
    * ❌ 0 points: Starting point is incorrect.
* Last task (max. 2 points)
    * ✅ 2 points: Current task has been implemented or it is evident that the student has engaged appropriately with the current task.
    * ☑️ 1 point: Little effort towards the last task is evident.
    * ❌ 0 points: No effort towards the last task is evident.

## Roadmap
Features to be implemented
- [] Accounts
    - [] Account entity that corresponds to keycloak users.

## Important files

[Troubleshooting guide](./markdown/troubleshooting-guide.md)

## Useful subsidiary tools
- **Json Web Tokens:**    https://jwt.io/
- **Bruno API Client:**    https://www.usebruno.com/