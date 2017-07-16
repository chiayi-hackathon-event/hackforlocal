package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/22.
 */
@Entity
public class DeveloperGroupAuthorization extends Model {
    @Id
    @Column(name = "id",nullable = false)
    @Expose
    public Long id;

    @ManyToOne
    @JoinColumn(name="group_id")
    public DeveloperGroup devGroup;

    @Column(name = "resource")
    public String resource;
}
