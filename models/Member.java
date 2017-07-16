package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by rog on 15/10/22.
 */
@Entity
public class Member extends  Model{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    @Expose
    public Long memberID;

    @Constraints.Required
    @Column(name = "email")
    @Expose
    public String email;

    @Constraints.Required
    @Column(name = "password")
    public String password;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Column(name = "create_time")
    @Expose
    public Date create_time = new Date();

    @Column(name = "first_name")
    @Expose
    public String firstName;

    @Column(name = "last_name")
    @Expose
    public String lastName;

    @Column(name = "nick_name")
    @Expose
    public String nickName;

    @Formats.DateTime(pattern="dd/MM/yyyy")
    @Column(name = "birthday")
    @Expose
    public Date brithday;

    /**
     * 0 : undefined , 1 : male , 2 : female
     */
    @Column(name = "gender",nullable = false)
    @Expose
    public Integer gender = 0;

    @Column(name = "subscribe",nullable = false)
    @Expose
    public boolean subscribe = true;

    @ManyToOne(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinColumn(name="country_id", referencedColumnName="country_id")
    @Expose
    public Country country;


    @Column(name = "active",nullable = false)
    @Expose
    public boolean active = false;

    @OneToMany(mappedBy = "member", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Expose
    public List<MemberContact> memberContacts;

    @OneToMany(mappedBy = "member", fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @Expose
    public List<MemberShip> memberShips;

    @OneToMany(mappedBy = "member")
    public List<IIIModel> iiiModels;

    @OneToMany(mappedBy = "member")
    public List<DeveloperRegister> developerRegisters;

    @Column(name = "fb_id")
    @Expose
    public String fbID;

    @Column(name = "banner_image_name")
    @Expose
    public String bannerImageName = "ic_user_l.png";

    @Column(name = "description")
    @Expose
    public String description;

    @Column(name = "private_version")
    @Expose
    public float private_version =2;
    //版本號2:P-V4x-IDEAS
    //版本號1:2015

    @Column(name = "ip")
    @Expose
    public String ip;

    public static Finder<Long,Member> find = new Finder<Long,Member>(
            Long.class, Member.class
    );
}

