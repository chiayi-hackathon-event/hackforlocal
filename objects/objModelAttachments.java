package objects;

/**
 * Created by Saber on 2015/11/20.
 */
public class objModelAttachments {
    protected String fileName;
    protected String fileShowname;
    protected Long fileSize;
    protected String fileSizeUnit;
    protected String fileThumbnail;
    protected String fileType;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileShowname(String fileShowname) {
        this.fileShowname = fileShowname;
    }

    public String getFileShowname() {
        return fileShowname;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSizeUnit(String fileSizeUnit) {
        this.fileSizeUnit = fileSizeUnit;
    }

    public String getFileSizeUnit() {
        return fileSizeUnit;
    }

    public void setFileThumbnail(String fileThumbnail) {
        this.fileThumbnail = fileThumbnail;
    }

    public String getFileThumbnail() {
        return fileThumbnail;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getFileType() {
        return fileType;
    }
}
