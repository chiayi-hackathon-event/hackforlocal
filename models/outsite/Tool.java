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
public class Tool extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tool_id",nullable = false)
    @Expose
    public Long toolID;


    @Column(name = "tool_name")
    @Expose
    public String toolName;

    @Column(name = "banner")
    @Expose
    public String toolBanner;

    @Column(name = "program")
    @Expose
    public String toolProgram;



    @Column(name = "download")
    @Expose
    public int download = 0;

    @Column(name = "viewed_times")
    @Expose
    public int viewedTimes = 0;

    @Column(name = "collect_count")
    @Expose
    public int collectCount = 0;

    @Column(name = "like_count")
    @Expose
    public int likeCount = 0;

    @Column(name = "figure")
    @Expose
    public String toolFigure;

   @Column(name = "form")
    @Expose
    public String toolForm;

   @Column(name = "announce")
    @Expose
    public String toolAnnounce;   


/*

    @OneToMany(mappedBy = "tool", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Expose
    public List<ToolModelAttachment> toolAttachments;

    @OneToMany(mappedBy = "tool", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Expose
    public List<ToolModelThumbnail> toollThumbnails;
*/

    public static Finder<Long,Tool> find = new Finder<Long,Tool>(
            Long.class, Tool.class
    );

}
