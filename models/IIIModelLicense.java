package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Created by Saber on 2016/1/24.
 */
@Entity
public class IIIModelLicense extends Model {
    @Id
    @Column(name = "license_id")
    @Expose
    public int licenseID;

    @Column(name = "license_name")
    @Expose
    public String licenseName;

    @Column(name = "license_url")
    @Expose
    public String licenseUrl;

    @Column(name = "license_image")
    @Expose
    public String licenseImg;


    @OneToMany(mappedBy = "modellicense")
    public List<IIIModel> iiimodels;

    public static Model.Finder<Integer, IIIModelLicense> find = new Model.Finder<Integer, IIIModelLicense>(
            Integer.class, IIIModelLicense.class
    );
}
