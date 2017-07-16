package objects;


import com.google.gson.annotations.Expose;
import models.Member;

/**
 * Created by abow on 15/10/24.
 */
public class objRetMember extends objRetWS{
    @Expose
    public Member member;

    public objRetMember(){super();}

    public objRetMember(int retCode,String retMessage){super(retCode,retMessage);}

    public objRetMember(Class c,String retCode,String retMessage){super(c,retCode,retMessage);}
}
