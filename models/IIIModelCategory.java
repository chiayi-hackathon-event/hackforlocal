package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;
import java.util.List;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelCategory extends Model {
    @Id
    @Column(name = "category_id")
    @Expose
    public int categoryID;

    @Column(name = "category_name")
    @Expose
    public String categoryName;

    @Column(name = "status")
    @Expose
    public int status = 1;


    @OneToMany(mappedBy = "modelcategory")
    public List<IIIModel> iiimodels;

    public static Finder<Integer,IIIModelCategory> find = new Finder<Integer,IIIModelCategory>(
            Integer.class, IIIModelCategory.class
    );
}
