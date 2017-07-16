package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by abow on 16/11/09.
 */
@Entity
public class ResetAccount extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    public Long id;

    @Column(name = "email")
    public String email;

    @Column(name = "ip")
    @Expose
    public String ip;

    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Column(name = "start_datetime")
    public Date startDateTime = new Date();


    public static Finder<String, ResetAccount> find = new Finder<String, ResetAccount>(
            String.class, ResetAccount.class
    );
}
