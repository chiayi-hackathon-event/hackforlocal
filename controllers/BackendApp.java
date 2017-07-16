package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import models.Events;
import models.IIIBanner;
import models.IIIModel;
import objects.objRetBanners;
import objects.objRetEvents;
import objects.objRetModels;
import objects.objRetWS;
import play.libs.EventSource;
import play.libs.F;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import views.html.BackendApp.removeBanners;
import views.html.BackendApp.removeEvents;
import views.html.BackendApp.removeModels;


import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by roger.lee on 2016/1/21.
 */
public class BackendApp  extends Controller {
    @Inject
    WSClient ws;
    static final String workingDir = System.getProperty("user.dir");
    // for fastlab server
    // static final String workingDir = "/home/fastlab/fastlab-git/fastlab";
    static final String webServiceUrl = "http://127.0.0.1:9000";
    static final String webServiceKey = "a369b3350f8d435588346ba30c1a1cb5";
    static final String page = "1";
    static final String pageSize = "20";
    static final String memberToken = "memberToken";
    public Result removeBanners(){
    if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        List<IIIBanner> banners = new ArrayList<IIIBanner>();
        try {
            F.Promise<JsonNode> jsonPromiseBanners = ws.url(webServiceUrl + "/bannerws/getbanners").get().map(response ->
                    response.asJson()
            );
            JsonNode jsonNode = jsonPromiseBanners.get(20000);
            objRetBanners retBanners = Util.transferJSON2Banners(jsonNode);
            banners = retBanners.banners;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ok(removeBanners.render("",banners));
    }
    public Result removemModels(){
    if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        List<IIIModel> models = new ArrayList<IIIModel>();
        try {
            F.Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                    .setQueryParameter("page", page)
                    .setQueryParameter("pagesize", pageSize)
                    .setQueryParameter("orderby", "0")
                    .setQueryParameter("categoryID", "0")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(20000);
            objRetModels objRetM =Util.transferJSON2Models(jsonNode);
            models =objRetM.models;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ok(removeModels.render("",models));
    }
    public Result removeEvents(){
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        List<Events> events = new ArrayList<Events>();
        try {
            F.Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/getAllEvents")
                    .get().map(response -> response.asJson());
            JsonNode jsonNode = jsonPromise.get(20000);
            objRetEvents objRetE = Util.transferJSON2Events(jsonNode);
            events = objRetE.events;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ok(removeEvents.render("",events));
    }

    public Result deleteBanners(Long bannerID){
        IIIBanner.find.deleteById(bannerID);
        return Results.redirect(" /Remove-Banner ");
    }
    public Result deleteModels(Long modelID){
    if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        try {

            IIIModel dbModel = IIIModel.find.byId(modelID);
        //    dbModel.member = null;
              dbModel.status =0;
            Ebean.save(dbModel);

            Ebean.commitTransaction();
        }catch (Exception ee){
            System.out.println(ee.getMessage());
        }finally {
            Ebean.endTransaction();
        }
        return Results.redirect(" /Remove-Model");
    }
    public Result deleteEvents(Long eventID){
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        Events.find.deleteById(eventID);
        return Results.redirect("/Remove-Event");
    }
    public static String getCookie(String key){
        if(request().cookies().get(key) != null) {
            return request().cookies().get(key).value().toString();
        }else {
            return "";
        }
    }

}
