package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by abow on 15/11/22.
 */
@Entity
public class DeveloperRegister extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "access_key")
    public String accessKey;

    @ManyToOne
    @JoinColumn(name="member_id")
    public Member member;

    @ManyToOne
    @JoinColumn(name="group_id")
    public DeveloperGroup devGroup;

    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Column(name = "create_time")
    public Date createTime = new Date();

    @Column(name = "active")
    public boolean active = false;

    @Column(name = "is_admin")
    public boolean isAdmin = false;

    public static Finder<String,DeveloperRegister> find = new Finder<String,DeveloperRegister>(
            String.class, DeveloperRegister.class
    );
}
