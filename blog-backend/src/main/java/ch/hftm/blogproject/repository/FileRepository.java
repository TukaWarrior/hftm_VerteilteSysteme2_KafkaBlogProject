package ch.hftm.blogproject.repository;

import ch.hftm.blogproject.model.entity.File;
import io.quarkus.hibernate.orm.panache.PanacheRepository;

public class FileRepository implements PanacheRepository<File>{

    public void saveFile(File file) {
        persist(file);
    }

    public File findFileById(String fileId) {
        return find("fileId", fileId).firstResult();
    }

    public File findFileByFileName(String fileName) {
        return find("fileName", fileName).firstResult();
    }

    public void deleteFileById(String fileId) {
        delete("fileId", fileId);
    }

    public long countFiles() {
        return count();
    }
}
