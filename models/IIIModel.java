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
public class IIIModel extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "model_id",nullable = false)
    @Expose
    public Long modelID;

    @Constraints.Required
    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="member_id", referencedColumnName="member_id")
    @Expose
    public Member member;

    @Column(name = "model_name")
    @Expose
    public String modelName;

    @Column(name = "average_score")
    @Expose
    public float averageScore = 0.0f;

    @Formats.DateTime(pattern="dd/MM/yyyy HH:mm:ss")
    @Column(name = "create_time")
    @Expose
    public Date create_time = new Date();

    @Column(name = "description", columnDefinition = "TEXT")
    @Expose
    public String description;

    @Column(name = "scenario", columnDefinition = "TEXT")
    @Expose
    public String scenario;

    @Column(name = "instruction", columnDefinition = "TEXT")
    @Expose
    public String instruction;

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

   @Column(name = "status")
    @Expose
    public int status = 1;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="category_id", referencedColumnName="category_id")
    @Expose
    public IIIModelCategory modelcategory;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="license_id", referencedColumnName="license_id")
//    @JoinColumn(name="license_name", referencedColumnName="license_name")
    @Expose
    public IIIModelLicense modellicense;

    @OneToMany(mappedBy = "iiimodel" , fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Expose
    public List<IIIModelTag> modelTags;

    @OneToMany(mappedBy = "iiimodel" )
    @Expose
    public List<IIIModelComment> modelComments;

    @OneToMany(mappedBy = "iiimodel", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Expose
    public List<IIIModelAttachment> modelAttachments;

    @OneToMany(mappedBy = "iiimodel", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @OrderBy("isSystemCreated asc")
    @Expose
    public List<IIIModelThumbnail> modelThumbnails;

    @ManyToMany
    @Expose
    public List<Campaign> entitiesA = new ArrayList<Campaign>();

    public static Finder<Long,IIIModel> find = new Finder<Long,IIIModel>(
            Long.class, IIIModel.class
    );

}
