package models;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;

import javax.persistence.*;

/**
 * Created by abow on 15/11/7.
 */
@Entity
public class IIIModelAttachment extends Model {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "file_name")
    @Expose
    public String fileName;

    @Column(name = "file_showname")
    @Expose
    public String fileShowname;

    @Column(name = "file_size")
    @Expose
    public float fileSize;

    @Column(name = "file_size_unit")
    @Expose
    public String fileSizeUnit;

    @Column(name = "file_thumbnail")
    @Expose
    public String fileThumbnail;

    @Column(name = "file_type")
    @Expose
    public String fileType;

   @Column(name = "status")
   @Expose
   public int status = 1;

    @ManyToOne
    @JoinColumn(name="model_id")
    public IIIModel iiimodel;

    public static Finder<String,IIIModelAttachment> find = new Finder<String,IIIModelAttachment>(
            String.class, IIIModelAttachment.class
    );
}
