package controllers;

import play.*;
import java.io.*;
import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import models.Events;
import models.IIIBanner;
import objects.objRetBanners;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Date;
import play.api.Logger;
import iiiException.parameterMissingException;
import iiiException.invalidMemberToken;
/**
 * Created by roger.lee on 2016/1/7.
 */
public class BannerWS extends UniTools {
    public Result newBanner(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        objRetWS objWS;
        Ebean.beginTransaction();
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();

             if (!objJson.has("member_token")   || objJson.get("member_token").asText().isEmpty()){
                throw new parameterMissingException("member_token missing");
            }           

      
            Long member_id = convertMemberTokenToMemberID(objJson.get("member_token").asText());
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            // get member_id by member token
            IIIBanner dbBanner = new IIIBanner();
            dbBanner.bannerImage = objJson.get("bannerImage").asText();
            dbBanner.externalUrl = objJson.get("externalUrl").asText();
            dbBanner.showStartDate = sdf.parse(objJson.get("showStartDate").asText());
            dbBanner.showEndDate = sdf.parse(objJson.get("showEndDate").asText());
            dbBanner.memberID = member_id;
            Ebean.save(dbBanner);
            Ebean.commitTransaction();
            objWS = new objRetWS();
            objWS.retCode = 0;
            objWS.retMessage = dbBanner.id.toString();
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result getBanners(){
        objRetBanners objRetB = new objRetBanners();
        try {
          //  List<IIIBanner> banners = IIIBanner.find.all();
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date current = new Date();
            List<IIIBanner> banners = IIIBanner.find.where().gt("showEndDate",sdFormat.format(current)).findList();
    //           List<IIIBanner> banners = IIIBanner.find.where().eq("bannerImage","bannerimage/ad_mokey.jpg").findList();
            objRetB.retCode = 0;
            objRetB.banners = banners;
            objRetB.retMessage = "Get Events Successful.";
        }catch (Exception ee)
        {
            objRetB.retCode = 1;
            objRetB.retMessage = ee.toString();
        }
        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetB)).as("application/json");
    }
    public Result deleteBanner(long bannerID){
        objRetBanners objRetB = new objRetBanners();
        IIIBanner.find.deleteById(bannerID);
        return ok();
    }

}
