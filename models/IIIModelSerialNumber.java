package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelSerialNumber extends Model {
    @Id
    @Column(name = "id")
    @Expose
    public Long id;
}
