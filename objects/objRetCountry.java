package objects;

import com.google.gson.annotations.Expose;
import models.Country;

import java.util.List;

/**
 * Created by abow on 15/11/1.
 */
public class objRetCountry extends objRetWS{
    @Expose
    public List<Country> countrys;

    public objRetCountry(){super();}

    public objRetCountry(int retCode,String retMessage){super(retCode,retMessage);}

    public objRetCountry(Class c,String retCode,String retMessage){
        super(c,retCode,retMessage);
    }
}
