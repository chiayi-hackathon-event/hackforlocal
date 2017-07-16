package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/4.
 */
@Entity
public class MemberContact extends Model {
    @Id
    @Column(name = "contact_id")
    @Expose
    public Long contactID;

    @ManyToOne
    @JoinColumn(name="member_id")
    public Member member;

    /**
     * 0 : mobile
     * 1 : company telphone
     * 2 : home telphone
     * 3 : company address
     * 4 : home telphone
     * 5 : email
     */
    @Column(name = "contact_type")
    @Expose
    public Integer contactType = 0;

    @Column(name = "contact_value")
    @Expose
    public String contactValue;



}
