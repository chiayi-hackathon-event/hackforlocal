package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import iiiException.noData;
import iiiException.parameterMissingException;
import library.WSAuthenticator;
import models.Campaign;
import models.IIIModel;
import objects.objRetCampaign;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abow on 15/11/1.
 */

@Security.Authenticated(WSAuthenticator.class)
public class CampaignWS extends Controller {

    public Result getCampaignList() {
        objRetCampaign objRetC;
        try{
            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String now = sdf.format(new Date());
            List<Campaign> dbCampaigns = Ebean.find(Campaign.class).where().betweenProperties("campaignStartDate", "campaignEndDate", now).findList();
            if (dbCampaigns.isEmpty()){
                throw new noData("there is no campaign data");
            }else{
                objRetC =  new objRetCampaign(0,"success");
                objRetC.campaigns = dbCampaigns;
            }
        }catch(noData e){
            objRetC = new objRetCampaign(CampaignWS.class,"03001",e.toString());
        }catch(Exception ee){
            objRetC =  new objRetCampaign(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetC)).as("application/json");
    }


    public Result getCampaign(long campaignID) {
        objRetCampaign objRetC;
        try{
            Campaign dbC =  Campaign.find.byId(campaignID);
            if (dbC == null) throw new noData("there is no campaign data");

            List <Campaign> dbCs = new ArrayList<Campaign>();
            dbCs.add(dbC);
            objRetC =  new objRetCampaign(0,"success");
            objRetC.campaigns = dbCs;

        }catch(noData e){
            objRetC = new objRetCampaign(CampaignWS.class,"03001",e.toString());
        }catch(Exception ee){
            objRetC =  new objRetCampaign(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetC)).as("application/json");
    }

    public Result newCampaign() {
        objRetWS objWS;
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("campaignTitle")       || objJson.get("campaignTitle").asText().isEmpty()  ||
                !objJson.has("campaignStartDate")   ||
                !objJson.has("campaignEndDate")){
                throw new parameterMissingException("parameter missing");
            }

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Campaign dbC = new Campaign();
            dbC.campaignTitle = objJson.get("campaignTitle").asText();
//            dbC.campaignDesc = objJson.get("campaignDesc").asText();
            dbC.campaignStartDate = sdf.parse(objJson.get("campaignStartDate").asText());
            dbC.campaignEndDate = sdf.parse(objJson.get("campaignEndDate").asText());
//            dbC.campaignImage = objJson.has("campaignImage")?objJson.get("campaignImage").asText():"";
            Ebean.save(dbC);

            objWS = new objRetWS(0,String.valueOf(dbC.campaignID));
        }catch(parameterMissingException e){
            objWS = new objRetWS(CampaignWS.class,"02001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result updateCampaign(long campaignID) {
        objRetWS objWS;
        try{
            Campaign dbC = Campaign.find.byId(campaignID);
            if (dbC == null){
                throw new noData("there is no campaign data");
            }

            DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            JsonNode objJson = request().body().asJson();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "campaignTitle":
                        dbC.campaignTitle = objJson.get(key).asText();
                        break;
                    case "campaignDesc":
                        dbC.campaignDesc = objJson.get(key).asText();
                        break;
                    case "campaignStartDate":
                        dbC.campaignStartDate = sdf.parse(objJson.get(key).asText());
                        break;
                    case "campaignEndDate":
                        dbC.campaignEndDate = sdf.parse(objJson.get(key).asText());
                        break;
                    case "campaignImage":
                        dbC.campaignImage = objJson.get(key).asText();
                        break;
                }
            }

            Ebean.save(dbC);

            objWS = new objRetWS(0,"update campaign success");

        }catch(noData e){
            objWS = new objRetWS(CampaignWS.class,"03001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result delCampaign(long campaignID) {
        objRetWS objWS;
        try{
            Campaign.find.byId(campaignID).delete();
            objWS = new objRetWS(0,"delete campaigin by id " + String.valueOf(campaignID));
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result assignModel2Campaign(long campaignID, long modelID) {
        objRetWS objWS;
        try{
            Campaign dbC =  Campaign.find.byId(campaignID);
            if (dbC == null) throw new noData("there is no campaign data");

            IIIModel dbM = IIIModel.find.byId(modelID);
            if (dbC == null) throw new noData("there is no model data");

            dbC.entitiesB.add(dbM);

            Ebean.save(dbC);
            objWS = new objRetWS(0,"assign model to campagin success");
        }catch(noData e){
            objWS = new objRetCampaign(CampaignWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS =  new objRetCampaign(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objWS)).as("application/json");
    }
}
