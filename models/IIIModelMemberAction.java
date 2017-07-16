package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelMemberAction extends Model {


//    @Column(name = "model_id")
//    @Expose
//    public Long modelID;
//
//
//    @Column(name = "member_id")
//    @Expose
//    public Long memberID;
    @EmbeddedId
    public IIIModelMemberActionPK pk;

    @Column(name = "rating")
    @Expose
    public float rating = 0.0f;

    @Column(name = "download_times")
    @Expose
    public int downloadTimes = 0;

    @Column(name = "viewed_times")
    @Expose
    public int viewedTimes = 0;

    @Column(name = "shared_times")
    @Expose
    public int sharedTimes = 0;

    @Column(name = "is_collect")
    @Expose
    public boolean isCollect = false;

    @Column(name = "is_liked")
    @Expose
    public boolean isLiked = false;

    @Column(name = "is_download")
    @Expose
    public boolean isDownload = false;



    public static Finder<IIIModelMemberActionPK,IIIModelMemberAction> find = new Finder<IIIModelMemberActionPK,IIIModelMemberAction>(
            IIIModelMemberActionPK.class, IIIModelMemberAction.class
    );

}

