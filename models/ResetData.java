package models;
import models.*;

import com.avaje.ebean.Model;
import com.google.gson.annotations.Expose;
import play.data.format.Formats;
import play.data.validation.Constraints;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

/**


 * Created by kaiming on 17/07/15.
 */
@Entity
public class ResetData extends Model{
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id")
        @Expose
        public Integer ID;

        //@COnstraints.Required
        @Column(name = "start")
        @Expose
        public String Start;

	@Column(name = "end")
        @Expose
        public String End;

	@Column(name = "route")
        @Expose
        public String Route;

	@Column(name = "url")
        @Expose
        public String Url;

	@Column(name = "description")
        @Expose
        public String Dec;

        public static Finder<Long,ResetData> find = new Finder<Long,ResetData>(
            Long.class, ResetData.class
    );
}

