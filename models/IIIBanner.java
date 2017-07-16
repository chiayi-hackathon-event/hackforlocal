package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by roger.lee on 2016/1/7.
 */
@Entity
public class IIIBanner {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Expose
    public Long id;

    @Expose
    public String bannerImage;

    @Expose
    public String externalUrl;

    @Constraints.Required
    @Column(name = "show_start_date")
    @Formats.DateTime(pattern="yyyy-MM-dd")
    @Expose
    public Date showStartDate;

    @Constraints.Required
    @Column(name = "show_end_date")
    @Formats.DateTime(pattern="yyyy-MM-dd")
    @Expose
    public Date showEndDate;
   
    @Constraints.Required
    @Column(name = "member_id")
    @Expose
    public Long memberID;

    public static Model.Finder<Long,IIIBanner> find = new Model.Finder<Long,IIIBanner>(
            Long.class, IIIBanner.class
    );
}
