package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelCampaign  extends Model {


    @Column(name = "iiimodel_model_id")
    @Expose
    public int iiimodel_model_id;

    @ManyToOne
    @Column(name = "campaign_campaign_id")
    @Expose
    public int campaign_campaign_id;



    public static Finder<Long,IIIModelCampaign> find = new Finder<Long,IIIModelCampaign>(
            Long.class, IIIModelCampaign.class
    );

}
