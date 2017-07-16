package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;

import javax.persistence.*;

import java.util.Date;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelComment extends Model {
    @Id
    @Column(name = "id")
    @Expose
    public Long id;

    @Formats.DateTime(pattern="dd/MM/yyyy HH:mm:ss")
    @Column(name = "create_time")
    @Expose
    public Date create_time = new Date();

    @ManyToOne
    @JoinColumn(name="model_id")
    public IIIModel iiimodel;

    @Column(name = "member_id")
    @Expose
    public Long memberID;

    @Column(name = "member_nickname")
    @Expose
    public String memberNickname;

    @Column(name = "message")
    @Expose
    public String message;

    @Column(name = "parent_id")
    @Expose
    public Long parentID;


}
