package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by apple on 15/10/24.
 */
@Entity
public class Events {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Expose
    public Long id;

    @Constraints.Required
    @Expose
    public String title;

    @Constraints.Required
    @Expose
    public String organizer;

    @Column(name = "co_organizer")
    @Expose
    public String coOrganizer;

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

    @Column(name = "show_time")
    @Expose
    public String showTime;

    @Expose
    public String location;

    @Expose
    @Column(name = "description", columnDefinition = "TEXT")
    public String description;

    @Expose
    public String images;

    @Expose
    @Column(name = "image_thumbnail", columnDefinition = "TEXT")
    public String imageThumbnail;
    @Expose
    public String externalUrls;

    @Column(name = "create_time")
    @Formats.DateTime(pattern="yyyy-MM-dd HH:mm:ss.ff zzz")
    @Expose
    public Date createTime = new Date();

    @Constraints.Required
    @Column(name = "member_id")
    @Expose
    public Long memberID;

    public static Model.Finder<Long,Events> find = new Model.Finder<Long,Events>(
            Long.class, Events.class
    );

}
