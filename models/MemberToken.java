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
public class MemberToken extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_token")
    public String memberToken;

    @ManyToOne
    @JoinColumn(name="member_id")
    public Member member;

    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Column(name = "start_datetime")
    public Date startDateTime = new Date();

    @Column(name = "time_interval")
    @Expose
    public int timeInterval;

    public static Finder<String,MemberToken> find = new Finder<String,MemberToken>(
            String.class, MemberToken.class
    );
}
