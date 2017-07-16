package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelThumbnail extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "thumbnail_filename")
    @Expose
    public String thumbnailFilename;

    @Column(name = "thumbnail_showname")
    @Expose
    public String thumbnailShowname;

    @Column(name = "thumbnail_resize")
    @Expose
    public String thumbnailResize;

    @Column(name = "is_system_created")
    @Expose
    public Boolean isSystemCreated = false;

   @Column(name = "status")
   @Expose
   public int status = 1;

    @ManyToOne
    @JoinColumn(name="model_id")
    public IIIModel iiimodel;

    public static Finder<String,IIIModelThumbnail> find = new Finder<String,IIIModelThumbnail>(
            String.class, IIIModelThumbnail.class
    );
}
