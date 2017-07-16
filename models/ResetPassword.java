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
public class ResetPassword extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "email")
    public String email;

    @Column(name = "token")
    public String token;

    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Column(name = "start_datetime")
    public Date startDateTime = new Date();

    @Column(name = "time_interval")
    public int timeInterval =  10;

    public static Finder<String, ResetPassword> find = new Finder<String, ResetPassword>(
            String.class, ResetPassword.class
    );
}
