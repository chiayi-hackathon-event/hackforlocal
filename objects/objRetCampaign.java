package objects;

import com.google.gson.annotations.Expose;
import models.Campaign;

import java.util.List;

/**
 * Created by abow on 15/11/1.
 */
public class objRetCampaign extends objRetWS{
    @Expose
    public List<Campaign> campaigns;

    public objRetCampaign(){super();}

    public objRetCampaign(int retCode, String retMessage){super(retCode,retMessage);}

    public objRetCampaign(Class c, String retCode, String retMessage){
        super(c,retCode,retMessage);
    }
}
