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
public class scad_info extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tool_id",nullable = false)
    @Expose
    public Long toolID;


    @Column(name = "scad")
    @Expose
    public String Scad;

    @Column(name = "viewpoint")
    @Expose
    public String viewpoint;

    @Column(name = "mail_subject")
    @Expose
    public String Subject;

    @Column(name = "template")
    @Expose
    public String Template;

    @Column(name = "number")
    @Expose
    public int Number = 0;

    @Column(name = "type")
    @Expose
    public String Type;

    @Column(name = "default")
    @Expose
    public String  Default;


    public static Finder<Long,scad_info> find = new Finder<Long,scad_info>(
            Long.class, scad_info.class
    );

}
