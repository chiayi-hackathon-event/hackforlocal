package objects;

import com.google.gson.annotations.Expose;

/**
 * Created by abow on 15/10/24.
 *
 * retCode definition
 * ========================================
 * 0            success
 * 1            fetal error , ex: try catch exception
 * ========================================
 * #-##-##-###
 * A:Project
 * BB:Web service category
 * CC:Error type
 * DDD:Error detail
 * ========================================
 * A
 * 2 : IIIGA
 * ========================================
 * BB
 * 01 : MemberWS
 * 02 : GalleryWS
 * 03 : EventWS
 * 04 : CountryWS
 * 05 : ModelCategoryWS
 * 06 : DeveloperWS
 * 07 : CampaignWS
 * ========================================
 * CC-DDD
 * 01001 : DB selete error
 * 01002 : DB insert error
 * 01003 : DB update error
 * 01004 : DB delete error
 * 02001 : http request , input parameter missing
 * 03001 : can not find data
 * 03002 : duplicate data
 * 04001 : member token , invalid
 * 04002 : member password is incorrect
 * 04003 : member is inactive
 * 04004 : member is active
 * 05001 : not model creator
 * 06001 : can not generate available dev group id, please try it later
 * 06002 : can not generate available dev access key, please try it later
 * 06003 : invalid dev group id
 */
public class objRetWS {
    @Expose
    public int retCode;
    @Expose
    public String retMessage;

    public objRetWS(){
        super();
    }

    public objRetWS(int retCode,String retMessage){

        this.retCode = retCode;
        this.retMessage = retMessage;
    }

    public objRetWS(Class c,String retCode,String retMessage){
        String strRetCode = "";
        switch (c.getName()){
            case "controllers.MemberWS":
                strRetCode = "201" + retCode;
                break;
            case "controllers.GalleryWS":
                strRetCode = "202" + retCode;
                break;
            case "controllers.CountryWS":
                strRetCode = "204" + retCode;
                break;
            case "controllers.ModelCategoryWS":
                strRetCode = "205" + retCode;
                break;
            case "controllers.DeveloperWS":
                strRetCode = "206" + retCode;
                break;
            case "controllers.CampaignWS":
                strRetCode = "207" + retCode;
                break;
            default:
                strRetCode = retCode;
                break;

        }
        this.retCode = Integer.valueOf(strRetCode);
        this.retMessage = retMessage;
    }
}
