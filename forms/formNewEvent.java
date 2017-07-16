package forms;

import objects.objModelAttachments;
import objects.objModelThumbnails;

import java.util.List;

/**
 * Created by Saber on 2015/11/19.
 */
public class formNewEvent {
    protected String title;
    protected String organizer;
    protected String coOrganizer;
    protected String showStartDate;
    protected String showEndDate;
    protected String showTime;
    protected String location;
    protected String description;
    protected String images;
    protected String imageThumbnail;
    protected String externalUrls;



    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setCoOrganizer(String coOrganizer) {
        this.coOrganizer = coOrganizer;
    }

    public String getCoOrganizer() {
        return coOrganizer;
    }

    public void setShowStartDate(String showStartDate) {
        this.showStartDate = showStartDate;
    }

    public String getShowStartDate() {
        return showStartDate;
    }

    public void setShowEndDate(String showEndDate) {
        this.showEndDate = showEndDate;
    }

    public String getShowEndDate() {
        return showEndDate;
    }

    public void setShowTime(String showTime) {
        this.showTime = showTime;
    }

    public String getShowTime() {
        return showTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public String getImages() {
        return images;
    }

    public void setExternalUrls(String externalUrls) {
        this.externalUrls = externalUrls;
    }

    public String getExternalUrls() {
        return externalUrls;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public String getImageThumbnail() {
        return imageThumbnail;
    }
}
