package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.GsonBuilder;
import iiiException.duplicateData;
import iiiException.noData;
import iiiException.parameterMissingException;
import library.PBKDF2Lib;
import library.WSAuthenticator;
import models.Country;
import models.Member;
import objects.objRetCategory;
import objects.objRetCountry;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Iterator;
import java.util.List;

/**
 * Created by abow on 15/11/1.
 */

@Security.Authenticated(WSAuthenticator.class)
public class CountryWS extends Controller {

    public Result getCountryList() {
        objRetCountry objRetC;
        try{
            List<Country> dbCountrys = Country.find.findList();
            if (dbCountrys.size() == 0){
                throw new noData("there is no country data");
            }else{
                objRetC =  new objRetCountry();
                objRetC.retCode = 0;
                objRetC.retMessage = "success";
                objRetC.countrys = dbCountrys;
            }
        }catch(noData e){
            objRetC = new objRetCountry(CountryWS.class,"03001",e.toString());
        }catch(Exception ee){
            objRetC =  new objRetCountry(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetC)).as("application/json");
    }

    public Result newCountry() {
        objRetWS objWS;
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("countryName")             || objJson.get("countryName").asText().isEmpty()        ||
                    !objJson.has("countryCode")         ||
                    !objJson.has("countryIsoAlpha2")    ||
                    !objJson.has("countryIsoAlpha3")    ||
                    !objJson.has("region")){
                throw new parameterMissingException("parameter missing");
            }

            String countryName = objJson.get("countryName").asText();
            String countryCode = objJson.get("countryCode").asText();
            String countryIsoAlpha2 = objJson.get("countryIsoAlpha2").asText();
            String countryIsoAlpha3 = objJson.get("countryIsoAlpha3").asText();
            String region = objJson.get("region").asText();

            // check country is available
            List<Country> dbCountrys = Ebean.find(Country.class).where().eq("countryName",countryName).findList();
            if (dbCountrys.size() >0){
                throw new duplicateData("country is already exist");
            }

            Country dbCountry = new Country();
            dbCountry.countryName = countryName;
            dbCountry.countryCode = countryCode;
            dbCountry.countryIsoAlpha2 = countryIsoAlpha2;
            dbCountry.countryIsoAlpha3 = countryIsoAlpha3;
            dbCountry.region = region;
            Ebean.save(dbCountry);

            objWS = new objRetWS(0,String.valueOf(dbCountry.countryID));
        }catch(parameterMissingException e){
            objWS = new objRetWS(CountryWS.class,"02001",e.toString());
        }catch(duplicateData e){
            objWS = new objRetWS(CountryWS.class,"03002",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result updateCountry(int countryID) {
        objRetWS objWS;
        try{
            Country dbCountry = Country.find.byId(countryID);
            if (dbCountry == null){
                throw new noData("there is no country data");
            }

            JsonNode objJson = request().body().asJson();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "countryName":
                        dbCountry.countryName = objJson.get(key).asText();
                        break;
                    case "countryCode":
                        dbCountry.countryCode = objJson.get(key).asText();
                        break;
                    case "countryIsoAlpha2":
                        dbCountry.countryIsoAlpha2 = objJson.get(key).asText();
                        break;
                    case "countryIsoAlpha3":
                        dbCountry.countryIsoAlpha3 = objJson.get(key).asText();
                        break;
                    case "region":
                        dbCountry.region = objJson.get(key).asText();
                        break;
                }
            }

            Ebean.save(dbCountry);

            objWS = new objRetWS(0,"update country success");

        }catch(noData e){
            objWS = new objRetWS(CountryWS.class,"03001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result delCountry(int countryID) {
        objRetWS objWS;
        try{
            Country.find.byId(countryID).delete();
            objWS = new objRetWS(0,"delete country by id " + String.valueOf(countryID));
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

}
