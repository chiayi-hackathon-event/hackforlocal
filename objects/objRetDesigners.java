package objects;


import com.google.gson.annotations.Expose;
import models.Member;

import java.util.List;

/**
 * Created by abow on 15/10/24.
 */
public class objRetDesigners extends objRetWS{
    @Expose
    public List<Member> members;

    public objRetDesigners(){super();}

    public objRetDesigners(int retCode, String retMessage){super(retCode,retMessage);}

    public objRetDesigners(Class c, String retCode, String retMessage){super(c,retCode,retMessage);}
}
