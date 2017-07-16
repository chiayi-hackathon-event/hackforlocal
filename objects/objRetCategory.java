package objects;

import com.google.gson.annotations.Expose;
import models.Country;
import models.IIIModelCategory;

import java.util.List;

/**
 * Created by abow on 15/11/8.
 */
public class objRetCategory extends objRetWS{
    @Expose
    public List<IIIModelCategory> categories;

    public objRetCategory(){super();}

    public objRetCategory(int retCode,String retMessage){super(retCode,retMessage);}

    public objRetCategory(Class c,String retCode,String retMessage){
        super(c,retCode,retMessage);
    }
}
