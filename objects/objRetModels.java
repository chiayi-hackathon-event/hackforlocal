package objects;

import com.google.gson.annotations.Expose;
import models.IIIModel;

import java.util.List;

/**
 * Created by abow on 15/11/15.
 */
public class objRetModels extends objRetWS{
    @Expose
    public List<IIIModel> models;
    @Expose
    public int totalCount;

    public objRetModels(){
        super();
    }

    public objRetModels(int retCode,String retMessage){super(retCode,retMessage);}

    public objRetModels(Class c,String retCode,String retMessage){
        super(c,retCode,retMessage);
    }
}
