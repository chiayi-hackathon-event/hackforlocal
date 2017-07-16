package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import models.Events;
import objects.objRetEvent;
import objects.objRetEvents;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import iiiException.parameterMissingException;
import iiiException.invalidMemberToken;

/**
 * Created by apple on 2015/11/18.
 */
public class EventWS extends UniTools {
    public Result getEvents() {
        objRetEvents objRetE = new objRetEvents();
        try{

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDate = "";
            String endDate = "";
            if ( request().getHeader("start-date")!=null)
                startDate = request().getHeader("start-date");
            else
                startDate = "2015-01-01";
            if ( request().getHeader("end-date")!=null)
                endDate = request().getHeader("end-date");
            else
                endDate = "5015-01-01";
//            List<Events> events = Events.find.where().gt("show_start_date", startDate).lt("show_end_date",endDate).findList();
            List<Events> events = Events.find.where().lt("show_end_date",endDate).gt("show_start_date", startDate).orderBy("show_end_date desc").findList();           
            objRetE.events = events;
            objRetE.retMessage = "Get Events Successful.";

        }
        catch(Exception ee){
            objRetE.retCode = 1;
            objRetE.retMessage = ee.toString();
        }
        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetE)).as("application/json");
    }
    public Result getEventById(String eventID) {
        objRetEvent objRetE = new objRetEvent();
        try {
            Events event =null;
            event = Events.find.byId(Long.parseLong(eventID));
            objRetE.event = event;
            objRetE.retMessage = "Get Event By ID Successful.";
            objRetE.retCode = 0;
        }
        catch(Exception ee){
            objRetE.retCode = 1;
            objRetE.retMessage = ee.toString();
        }
        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetE)).as("application/json");
    }

    public Result newEvents(){
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
            Events dbEvent = new Events();
            dbEvent.memberID = member_id;
            dbEvent.title = objJson.get("title").asText();
            dbEvent.organizer = objJson.get("organizer").asText();
            dbEvent.coOrganizer = objJson.get("coOrganizer").asText();
            dbEvent.showStartDate = sdf.parse(objJson.get("showStartDate").asText());
            dbEvent.showEndDate = sdf.parse(objJson.get("showEndDate").asText());
            dbEvent.showTime = objJson.get("showTime").asText();
            dbEvent.location = objJson.get("location").asText();
            dbEvent.description = objJson.get("description").asText();
            dbEvent.images = objJson.get("images").asText();
            dbEvent.imageThumbnail = objJson.get("imageThumbnail").asText();
            dbEvent.externalUrls = objJson.get("externalUrls").asText();
            Ebean.save(dbEvent);
            Ebean.commitTransaction();
            objWS = new objRetWS();
            objWS.retCode = 0;
            objWS.retMessage = dbEvent.id.toString();

        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result updateEvents(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        objRetWS objWS;
        Ebean.beginTransaction();
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();


            // get member_id by member token
            Events dbEvent = Events.find.byId(objJson.get("eventId").asLong());

            dbEvent.title = objJson.get("title").asText();
            dbEvent.organizer = objJson.get("organizer").asText();
            dbEvent.coOrganizer = objJson.get("coOrganizer").asText();
            dbEvent.showStartDate = sdf.parse(objJson.get("showStartDate").asText());
            dbEvent.showEndDate = sdf.parse(objJson.get("showEndDate").asText());
            dbEvent.showTime = objJson.get("showTime").asText();
            dbEvent.location = objJson.get("location").asText();
            dbEvent.description = objJson.get("description").asText();
            dbEvent.images = objJson.get("images").asText();
            dbEvent.imageThumbnail = objJson.get("imageThumbnail").asText();
            dbEvent.externalUrls = objJson.get("externalUrls").asText();
            Ebean.save(dbEvent);
            Ebean.commitTransaction();
            objWS = new objRetWS();
            objWS.retCode = 0;
            objWS.retMessage = dbEvent.id.toString();

        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }
}
