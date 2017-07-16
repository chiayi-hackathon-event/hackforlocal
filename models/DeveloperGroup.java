package models;

import com.avaje.ebean.Model;
import play.data.format.Formats;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by abow on 15/11/22.
 */
@Entity
public class DeveloperGroup extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "group_id")
    public String groupID;

    @Column(name = "group_name")
    public String groupName;


    @Formats.DateTime(pattern="yyyy/MM/dd HH:mm:ss")
    @Column(name = "create_time")
    public Date createTime = new Date();

    @Column(name = "active")
    public boolean active = true;

    @OneToMany(mappedBy = "devGroup", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    public List<DeveloperGroupAuthorization> devGroupAuthorizations;

    @OneToMany(mappedBy = "devGroup")
    public List<DeveloperRegister> developers;

    public static Finder<String,DeveloperGroup> find = new Finder<String,DeveloperGroup>(
            String.class, DeveloperGroup.class
    );
}
