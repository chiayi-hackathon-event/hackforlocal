package objects;

/**
 * Created by Saber on 2015/11/20.
 */
public class objModelThumbnails {
    protected String thumbnailFilename;
    protected String thumbnailShowname;
    protected Long thumbnailResize;
    protected Boolean isSystemCreated;

    public void setThumbnailFilename(String thumbnailFilename) {
        this.thumbnailFilename = thumbnailFilename;
    }

    public String getThumbnailFilename() {
        return thumbnailFilename;
    }

    public void setThumbnailShowname(String thumbnailShowname) {
        this.thumbnailShowname = thumbnailShowname;
    }

    public String getThumbnailShowname() {
        return thumbnailShowname;
    }

    public void setThumbnailResize(Long thumbnailResize) {
        this.thumbnailResize = thumbnailResize;
    }

    public Long getThumbnailResize() {
        return thumbnailResize;
    }

    public void setIsSystemCreated(Boolean isSystemCreated) {
        this.isSystemCreated = isSystemCreated;
    }

    public Boolean getIsSystemCreated() {
        return isSystemCreated;
    }

}
