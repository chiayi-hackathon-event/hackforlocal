package controllers;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.Gson;
import objects.*;

/**
 * Created by apple on 2015/11/18.
 */
public class Util {
    public static objRetEvents transferJSON2Events(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetEvents retEvents = gson.fromJson(jsonString,objRetEvents.class);
        return retEvents;
    }

    public static objRetEvent transferJSON2Event(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetEvent retEvent = gson.fromJson(jsonString,objRetEvent.class);
        return retEvent;
    }

    public static objRetModels transferJSON2Models(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetModels retModels = gson.fromJson(jsonString,objRetModels.class);
        return retModels;
    }

    public static objRetModel transferJSON2Model(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetModel retModel = gson.fromJson(jsonString,objRetModel.class);
        return retModel;
    }

    public static objRetMember transferJSON2Member(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetMember retMember = gson.fromJson(jsonString,objRetMember.class);
        return retMember;
    }

    public static objRetDesigners transferJSON2Designers(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetDesigners retDesigners = gson.fromJson(jsonString,objRetDesigners.class);
        return retDesigners;
    }

    public static objRetWS transferJSON2CommonResponse(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetWS objWS = gson.fromJson(jsonString,objRetWS.class);
        return objWS;
    }
    public static objRetBanners transferJSON2Banners(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetBanners retBanners = gson.fromJson(jsonString,objRetBanners.class);
        return retBanners;
    }
    public static objRetCategory transferJSON2Categories(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetCategory retCategory = gson.fromJson(jsonString,objRetCategory.class);
        return retCategory;
    }

    public static objRetLicense transferJSON2Licenses(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetLicense retLicense = gson.fromJson(jsonString, objRetLicense.class);
        return retLicense;
    }

    public static objRetCampaign transferJSON2Campaigns(JsonNode jsonNode){
        Gson gson = new Gson();
        String jsonString = jsonNode.toString();
        objRetCampaign retCampaign = gson.fromJson(jsonString, objRetCampaign.class);
        return retCampaign;
    }

}
