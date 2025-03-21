package ch.hftm.blogproject.util;

import org.jboss.resteasy.reactive.multipart.FilePart;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class FileUtils {

    public String getMimeType(FilePart file) {
        return file.contentType();
    }

    public long getFileSize(FilePart file) {
        return file.size();
    }
}