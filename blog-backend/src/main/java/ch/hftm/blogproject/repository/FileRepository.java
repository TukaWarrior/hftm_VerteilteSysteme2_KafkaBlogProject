package ch.hftm.blogproject.repository;

import ch.hftm.blogproject.model.entity.File;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileRepository implements PanacheRepository<File>{

    @Transactional
    public File saveFile(File file) {
        persist(file);
        return file;
    }

    public File findFileById(Long fileId) {
        return find("fileId", fileId).firstResult();
    }

    public File findFileByFileName(String fileName) {
        return find("fileName", fileName).firstResult();
    }

    @Transactional
    public void deleteFileById(Long fileId) {
        delete("fileId", fileId);
    }

    // Delete all files
    @Transactional
    public void deleteAllFiles() {
        this.deleteAll();
    }

    // Count all files
    public long countFiles() {
        return count();
    }
}
