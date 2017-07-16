package objects;

import com.google.gson.annotations.Expose;
import models.IIIModel;
import models.IIIModelMemberAction;

/**
 * Created by abow on 15/11/8.
 */
public class objRetModel extends objRetWS{
    @Expose
    public IIIModel model;

    @Expose
    public IIIModelMemberAction modelinteraction;

    @Expose
    public Boolean isCreater;

    public objRetModel(){super();}

    public objRetModel(int retCode,String retMessage){super(retCode,retMessage);}

    public objRetModel(Class c,String retCode,String retMessage){super(c,retCode,retMessage);}
}