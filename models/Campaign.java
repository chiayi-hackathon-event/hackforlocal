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
 * Created by apple on 15/10/24.
 */
@Entity
public class Campaign extends Model{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Expose
    @Column(name = "campaign_id")
    public Long campaignID;

    @Constraints.Required
    @Expose
    @Column(name = "campaign_title")
    public String campaignTitle;

    @Column(name = "campaign_desc" , columnDefinition = "TEXT")
    @Expose
    public String campaignDesc;

    @Constraints.Required
    @Column(name = "campaign_start_date")
    @Formats.DateTime(pattern="yyyy-MM-dd")
    @Expose
    public Date campaignStartDate;

    @Constraints.Required
    @Column(name = "campaign_end_date")
    @Formats.DateTime(pattern="yyyy-MM-dd")
    @Expose
    public Date campaignEndDate;

    @Expose
    @Column(name = "campaign_image")
    public String campaignImage;


    @Column(name = "create_time")
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss.ff zzz")
    @Expose
    public Date createTime = new Date();

    @Column(name = "member_id")
    @Expose
    public Long member_id ;


    @ManyToMany(mappedBy = "entitiesA")
    public List<IIIModel> entitiesB = new ArrayList<IIIModel>();

    public static Model.Finder<Long, Campaign> find = new Model.Finder<Long, Campaign>(
            Long.class, Campaign.class
    );

}
