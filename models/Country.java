package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**
 * Created by abow on 15/10/31.
 */
@Entity
public class Country  extends Model {
    @Id
    @Column(name = "country_id")
    @Expose
    public int countryID;

    @Constraints.Required
    @Column(name = "country_name")
    @Expose
    public String countryName;

    @Column(name = "country_code")
    @Expose
    public String countryCode;

    @Column(name = "country_iso_alpha2")
    @Expose
    public String countryIsoAlpha2;

    @Column(name = "country_iso_alpha3")
    @Expose
    public String countryIsoAlpha3;

    @Column(name = "region")
    @Expose
    public String region;

    @OneToMany(mappedBy = "country")
    public List<Member> members;

    public static Finder<Integer,Country> find = new Finder<Integer,Country>(
            Integer.class, Country.class
    );
}
