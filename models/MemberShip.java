package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/4.
 */
@Entity
public class MemberShip extends Model {
    @Id
    @Column(name = "id")
    @Expose
    public Long id;

    @ManyToOne
    @JoinColumn(name="member_id")
    public Member member;

    @Column(name = "member_identifier")
    @Expose
    public Integer memberIdentifier = 0;

    @Column(name = "identifier_explain")
    @Expose
    public String identifierExplain;

    @Column(name = "membership_active",nullable = false)
    @Expose
    public boolean membershipActive = true;
}
