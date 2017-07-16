package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import iiiException.duplicateData;
import iiiException.noData;
import iiiException.parameterMissingException;
import library.WSAuthenticator;
import models.IIIModelLicense;
import objects.objRetLicense;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Saber on 2016/1/24.
 */
@Security.Authenticated(WSAuthenticator.class)
public class ModelLicenseWS extends Controller {

    public Result getLicenseList() {

        objRetLicense objRetL;
        try{
            List<IIIModelLicense> dblicenses = IIIModelLicense.find.findList();
            if (dblicenses.isEmpty()){
                throw new noData("there is no model license data");
            }else{
                objRetL = new objRetLicense();
                objRetL.retCode = 0;
                objRetL.retMessage = "success";
                objRetL.licenses = dblicenses;
            }
        }catch(noData e){
            objRetL = new objRetLicense(ModelLicenseWS.class, "03001", e.toString());
        }catch(Exception ee){
            objRetL = new objRetLicense(1, ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetL)).as("application/json");
    }

    public Result newLicense() {
        objRetWS objWS;
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("licenseName") || objJson.get("licenseName").asText().isEmpty()){
                throw new parameterMissingException("parameter missing");
            }
            String licenseName = objJson.get("licenseName").asText();

            // check available
            List<IIIModelLicense> dbLicenses = Ebean.find(IIIModelLicense.class).where().eq("licenseName", licenseName).findList();
            if (dbLicenses.size() >0){
                throw new duplicateData("license is already exist");
            }

            IIIModelLicense dbLicense = new IIIModelLicense();
            dbLicense.licenseName = licenseName;
            Ebean.save(dbLicense);

            objWS = new objRetWS(0,String.valueOf(dbLicense.licenseID));

        }catch(parameterMissingException e){
            objWS = new objRetWS(ModelLicenseWS.class, "02001", e.toString());
        }catch(duplicateData e){
            objWS = new objRetWS(ModelLicenseWS.class, "03002", e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result updateLicense(int licenseID) {
        objRetWS objWS;
        try{
            IIIModelLicense dbLicense = IIIModelLicense.find.byId(licenseID);
            if (dbLicense == null){
                throw new noData("there is no model license data");
            }

            JsonNode objJson = request().body().asJson();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "licenseName":
                        dbLicense.licenseName = objJson.get(key).asText();
                        break;
                }
            }

            Ebean.save(dbLicense);

            objWS = new objRetWS(0,"update model license success");

        }catch(noData e){
            objWS = new objRetWS(ModelLicenseWS.class, "03001", e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result delLicense(int licenseID) {
        objRetWS objWS;
        try{
            IIIModelLicense.find.byId(licenseID).delete();
            objWS = new objRetWS(0,"delete model license by license_id " + String.valueOf(licenseID));
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }
}
