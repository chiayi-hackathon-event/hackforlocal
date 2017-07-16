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
 * Created by kaiming on 16/06/16.
 */
@Entity
public class Admin_permit extends Model{
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "member_id")
	@Expose
	public Long memberID;

	//@COnstraints.Required
	@Column(name = "group")
	@Expose
	public Integer group;


	public static Finder<Long,Admin_permit> find = new Finder<Long,Admin_permit>(
            Long.class, Admin_permit.class
    );
}
