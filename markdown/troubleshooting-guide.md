[Back to Main Documentation](../README.md)

> [!NOTE]
> This is a guide that aims to help solve recurring problems regarding Java, Maven and Quarkus. I expand this as I aquire further knowledge and dealdealt with future problems.

# Table of Contents

- [Back to Main Documentation](../README.md)
- [Software Versions](#software-versions)
- [Troubleshooting Guide](#troubleshooting-guide)
  - [Searching the Problem](#searching-the-problem)
    - [Step 1: Ensure the issue is not caused by faulty code](#step-1-ensure-the-issue-is-not-caused-by-faulty-code)
    - [Step 2: Clean the project using the Maven wrapper](#step-2-clean-the-project-using-the-maven-wrapper)
    - [Step 3: Clean install the project using the Maven wrapper](#step-3-clean-install-the-project-using-the-maven-wrapper)
    - [Step 4: Run the application using the Maven wrapper](#step-4-run-the-application-using-the-maven-wrapper)
    - [Step 5: Start with the local Maven installation](#step-5-start-with-the-local-maven-installation)
    - [Step 6: Clean install the project using the local Maven installation](#step-6-clean-install-the-project-using-the-local-maven-installation)
    - [Step 7: Run the application using the local Maven installation](#step-7-run-the-application-using-the-local-maven-installation)
  - [Problems with Dependencies](#problems-with-dependencies)
    - [Step 1: Delete the local dependency repository](#step-1-delete-the-local-dependency-repository)
    - [Step 2: Update the dependencies to their newest release](#step-2-update-the-dependencies-to-their-newest-release)
    - [Step 3: Run the application using the local Maven installation](#step-3-run-the-application-using-the-local-maven-installation)
  - [Problems with the Maven Wrapper](#problems-with-the-maven-wrapper)
    - [Step 1: Check the Maven version](#step-1-check-the-maven-version)
    - [Step 2: Rebuild the Maven wrapper](#step-2-rebuild-the-maven-wrapper)
  - [Problems with Java and Maven](#problems-with-java-and-maven)
    - [Step 1: Check the Java version](#step-1-check-the-java-version)
    - [Step 2: Check the Maven version](#step-2-check-the-maven-version)
    - [System Environment Variable JAVA_HOME](#system-environment-variable-java_home)
    - [System Environment Variable PATH (Maven)](#system-environment-variable-path-maven)
  - [Problems with Quarkus](#problems-with-quarkus)

# Software versions
This project currently uses the folowing versions of various dependencies.

**Maven:** apache-maven-3.9.9

**Java version:** java 21.0.1 2023-10-17 LTS


# Troubleshooting Guide
This troubleshooting guide provides step-by-step instructions to resolve common issues when building or running this Quarkus project.

## Searching the problem
If you are unaware about what the problem causes, follow this guide. 

1. Ensure that the issue is not caused by faulty code that leads to a compile error.

2. Try cleaning the project using the maven wrapper:
    ```shell script
    ./mvnw clean
    ```
    If ./mvnw clean fails, there may be an issue with the Maven wrapper. 
    
    **See chapter: "Problems with the Maven Wrapper"**

3. Try clean installing the project using the maven wrapper. 
    ```shell script
    ./mvnw clean install
    ```
    If the ./mvnw clean install command fails but ./mvnw clean succeeds, there may be a problem with the dependencies. 
    
    **See chapter "Problems with dependencies"**

4. Try running the application using the maven wrapper:
    ```shell script
    ./mvnw quarkus:dev
    ```
    If the ./mvnw clean and ./mvnw clean install commands succeeded, but the ./mvnw quarkus:dev command fails, there may be a problem with the quarkus configuration. 

    **See chapter: "Problems with quarkus"**



5. Additionally, you can check if the programm starts using the local maven installation. Clean install and run the project using your local maven installation:

    ```shell script
    mvn clean
    ```
    If mvn clean fails, there may be a problem with the local maven installation. 

    **See chapter: "Problems with Java and Maven"**

6. Try clean installing the project using the local maven installation.
    ```shell script
    mvn clean install
    ```
    If the mvn clean install command fails but mvn clean succeeds, there may be a problem with the dependencies.

    **See chapter: "Problems with dependencies"**

7. Try running the application using the local maven installation:
    ```shell script
    mvn quarkus:dev
    ```
    If the mvn clean and mvn clean install commands succeeded, but the .mvn quarkus:dev command fails, there may be a problem with the quarkus configuration. 

    **See chapter: "Problems with quarkus"**

    If the command succeeds and the application starts, there may be a problem with the maven wrapper. 

    **See chapter: "Problem with the Maven wrapper"**


## Problems with dependencies
If dependencies are causing issues, reinstall them. To do so, first delete the current dependencies from the lcoal maven repository. 

1. Delete the local dependency repository: C://User/*name*/.m2/repository
    ```shell script
    rmdir /s /q C:\Users\<YourUsername>\.m2\repository\
    ```

    Then reinstall the dependencies:
    ```shell script
    mvn clean install
    ```
    If the command succeeds, the dependencies are successfully reinstalled. 
    If the mvn clean install fails, dependencies may be incompatible. 
    
2. Update the dependencies to their newest release. Caution! The newest releases of dependencies may also not be fully compatible. 
    ```shell script
    mvn versions:use-latest-releases
    ```

    Then reinstall the dependencies again:
    ```shell script
    mvn clean install
    ```
    If the commands succeeded, the dependencies are now updated to the newest release. If the mvn clean install command failed, the updated dependencies may be incompatible Solve the incompatibilities or roll back to a previously working commit. If any mvn command fails, there may be a problem with the local maven installation. 

    **See chapter "Problems with Java and Maven**


3. If the mvn clean install command succeeds, run the application using the local maven installation:
    ```
    mvn quarkus:dev
    ```
    If mvn quarkus:dev fails, there may be a problem with the quarkus configuration. 

    **See chapter: "Problems with quarkus"**

    If the command succeeds and the application starts, the local maven installation and the dependencies are functional. 

## Problems with the Maven Wrapper
If you have issues with the maven wrapper follow this section:

The Maven Wrapper is a script included in a project that allows you to run Maven without needing to have Maven installed globally on your system.
It ensures that a specific version of Maven, as defined in the project, is used, regardless of what version is installed globally.

1. Check the version of the maven version provided by the wrapper inside the project. Make sure the maven version displayed corresponds with the minimum required version listed in the chapter "Required Dependencies.
    ```shell script
    ./mvnw --version
    ```

2. If the version doesen't match the required maven version or if the command fails, delete the wrapper and then reinstall it. To do so, delete the .mvn folder inside the project root.
    ```
    Remove-Item -Recurse -Force .mvn
    ```

    Then rebuild the maven wrapper. 
    ```shell script
    mvn wrapper:wrapper
    ```
    If rebuilding the maven wrapper fails, there may be a problem with the local maven installation. 
    
    **See chapter: "Problems with Java and Maven"**



## Problems with Java and Maven
If any of the mvn commands are not functioning correctly, the local Java or Maven installation may be broken or incompatible. 

1. Check the java version
    ```shell script
    java -version
    ```
    For the minimum required version, **check chapter "Used dependencies"** 

    If the --version command shows a lower or different version than the minimum required version, update the Java jdk installation. If it is not working after the installation, check the JAVA_HOME system environment variable. 
    
    **See below. System Environment Variable JAVA_HOME**

    If the command fails with a message containing "is not recognized as the name of a cmdlet", the system didn't detect the java installation. Install java or check the JAVA_HOME system environment variable. 

    **See below. System Environment Variable JAVA_HOME**


2. Check the maven version
    ```shell script
    mvn -version
    ```
    For the minimum required version, **check chapter "Version Controll"**

    If the --version command shows a lower or different version than the minimum required version, update the Maven installation. If it is not working after the installation, check the PATH system environment variable. 

    **See below. System Environment Variable PATH (Maven)**

    If the command fails with a message containing "is not recognized as the name of a cmdlet", the system didn't detect the maven installation. Install maven or check the PATH system environment variable. 

    **See below. System Environment Variable PATH (Maven)**


**System Environment Variable JAVA_HOME**

Make sure that your JAVA_HOME environment variable is pointing to a jdk installation that fullfills the minimum required version. 
E.g. JAVA_HOME = C:\Program Files\Java\jdk-21

**System Environment Variable PATH (Maven)**

Make sure that the PATH system environment variable lists a maven installation of at least the minimum requried version.
E.g. Path = c:\program files\apache-maven-3.9.9\bin

## Problems with quarkus
Follow this section for solving problems with the quarkus configuration.

![Help me](https://media.tenor.com/10Zdx_RXqgcAAAAM/programming-crazy.gif)


## Problems with keycloak
IF the user token can not be retrieved due to "user not fully set up", go to authentication in the left tab, required action and turn off the required user action. Likely, turn off "Verify Profile".