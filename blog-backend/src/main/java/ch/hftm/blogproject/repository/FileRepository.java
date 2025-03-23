package ch.hftm.blogproject.repository;

import java.util.List;

import ch.hftm.blogproject.model.entity.FileEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileRepository implements PanacheRepository<FileEntity>{

    @Transactional
    public FileEntity persistFile(FileEntity file) {
        persist(file);
        return file;
    }

    public List<FileEntity> findAllFiles() {
        return this.listAll();
    }

    public FileEntity findFileById(Long fileId) {
        return findById(fileId);
    }

    public FileEntity findFileByFileName(String fileName) {
        return find("fileName", fileName).firstResult();
    }

    @Transactional
    public void deleteFileById(Long fileId) {
        delete("id", fileId);
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
