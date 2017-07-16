package forms;

/**
 * Created by roger.lee on 2016/1/7.
 */
public class formNewBanner {
    protected String bannerImage;
    protected String externalUrl;
    protected String showStartDate;
    protected String showEndDate;

    public void setBannerImage(String bannerImage) {
        this.bannerImage = bannerImage;
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    public String getExternalUrl() {
        return externalUrl;
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
}
