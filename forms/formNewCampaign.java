package forms;

/**
 * Created by Saber on 2016/2/16.
 */
public class formNewCampaign {
    protected String campaignTitle;
    protected String campaignStartDate;
    protected String campaignEndDate;

    public void setCampaignTitle(String campaignTitle) {
        this.campaignTitle = campaignTitle;
    }

    public String getCampaignTitle() {
        return campaignTitle;
    }

    public void setCampaignStartDate(String campaignStartDate) {
        this.campaignStartDate = campaignStartDate;
    }

    public String getCampaignStartDate() {
        return campaignStartDate;
    }

    public void setCampaignEndDate(String campaignEndDate) {
        this.campaignEndDate = campaignEndDate;
    }

    public String getCampaignEndDate() {
        return campaignEndDate;
    }
}
