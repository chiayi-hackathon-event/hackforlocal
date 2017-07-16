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
public class Active_info extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "email")
    @Expose
    public String email;


    @Column(name = "md5")
    @Expose
    public String md5;

    @Column(name = "content")
    @Expose
    public String content;

    @Column(name = "title")
    @Expose
    public String title;



    @Column(name = "tag_type")
    @Expose
    public int tag_type = 0;

    @Column(name = "time")
    @Expose
    Date time = new Date();



    public static Finder<Long,Active_info> find = new Finder<Long,Active_info>(
            Long.class, Active_info.class
    );

}
