package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelTag extends Model {
    @Id
    @Column(name = "id")
    @Expose
    public Long id;

    @Column(name = "tag_name")
    @Expose
    public String tagName;

    @ManyToOne
    @JoinColumn(name="model_id")
    public IIIModel iiimodel;


}