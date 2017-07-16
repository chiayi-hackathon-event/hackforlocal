package objects;

import com.google.gson.annotations.Expose;
import models.IIIModelLicense;

import java.util.List;

/**
 * Created by Saber on 2016/1/24.
 */
public class objRetLicense extends objRetWS{
    @Expose
    public List<IIIModelLicense> licenses;

    public objRetLicense(){super();}

    public objRetLicense(int retCode, String retMessage){super(retCode,retMessage);}

    public objRetLicense(Class c, String retCode, String retMessage){
        super(c,retCode,retMessage);
    }
}
