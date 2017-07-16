package controllers;

import models.Events;
import play.*;
import play.mvc.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by apple on 15/10/24.
 */
public class EventDAO extends Controller {
    public Result getEvents() {
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
            endDate =dateFormat.format(new Date());
        List<Events> events = Events.find.where().gt("show_start_date", startDate).lt("show_end_date",endDate).findList();
        return ok(play.libs.Json.toJson(events));
    }
    public Result getEventById() {
        String eventId = "0";
        Events event =null;
        if ( request().getHeader("event-id")!=null){
            event = Events.find.byId(Long.parseLong(eventId));
        }
        return ok(play.libs.Json.toJson(event));
    }
}
