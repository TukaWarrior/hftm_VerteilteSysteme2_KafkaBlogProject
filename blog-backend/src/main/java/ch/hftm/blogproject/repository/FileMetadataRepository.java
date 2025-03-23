package ch.hftm.blogproject.repository;

import java.util.List;

import ch.hftm.blogproject.model.entity.FileMetadataEntity;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class FileMetadataRepository implements PanacheRepository<FileMetadataEntity>{

    @Transactional
    public FileMetadataEntity persistFile(FileMetadataEntity file) {
        persist(file);
        return file;
    }

    public List<FileMetadataEntity> findAllFiles() {
        return this.listAll();
    }

    public FileMetadataEntity findFileById(Long fileId) {
        return findById(fileId);
    }

    public FileMetadataEntity findFileByFileName(String fileName) {
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
