package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import forms.*;
import models.*;
import objects.*;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F.Promise;
import play.libs.Json;
import play.libs.ws.WSClient;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import views.html.BackendApp.categoryManage;
import views.html.BackendApp.designerManage;
import views.html.BackendApp.eventManage;
import views.html.*;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


//2016.4.11新增lib//
import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import play.mvc.Http.Session;
import play.data.DynamicForm;
import play.data.Form;

import play.db.*;
import java.sql.*;
import java.io.*;
import play.api.libs.json.*;
import org.json.*;
import java.net.*;
import views.html.admin.*;
//2016.6.28新增
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.csrf.*;
import play.mvc.Action;
import java.net.URLDecoder;
import java.net.URLEncoder;
import com.edulify.modules.sitemap.SitemapItem;
import com.redfin.sitemapgenerator.ChangeFreq;


public class Application extends UniTools {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    // webservice parameter
    @Inject
    WSClient ws;
    static final String stlViewerHost = request().host();
    static final String webServiceUrl = "http://127.0.0.1:9000";
    static final String webServiceKey = "a369b3350f8d435588346ba30c1a1cb5";
    static final int wsTimeout = 10000;
    // key of cookies
    static final String memberToken = "memberToken";
    static final String rememberEmail = "rememberEmail";
    // model list parameter
    static final String page = "1";
    static final String pageSize = "20";
    // file path for dev mode
//    static final String workingDir = System.getProperty("user.dir");
    // file path for fastlab server
    static final String workingDir = "/home/fastlab/develop";
  @SitemapItem(changefreq = ChangeFreq.MONTHLY, priority = 0.8)  
    public Result index() {
        List<IIIModel> models = new ArrayList<IIIModel>();
        List<IIIModel> new_models = new ArrayList<IIIModel>();
        List<Integer> showModelIndex = new ArrayList<Integer>();
        List<Member> designers = new ArrayList<Member>();
        Random ran = new Random();
        String jBanners = "";
        try {
            // Get Top 3 Hot Models
            Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                    .setQueryParameter("page", "1")
                    .setQueryParameter("pagesize", "20")
                    .setQueryParameter("orderby", "1")
                    .setQueryParameter("categoryID", "0")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(wsTimeout);
            objRetModels objRetM = Util.transferJSON2Models(jsonNode);
            int modelSize =3;
            int popularSize = objRetM.models.size();
            if (objRetM.models.size()<3){
                modelSize = objRetM.models.size();
            }
            while (showModelIndex.size() < modelSize){
                int i = ran.nextInt(popularSize);
                if (!showModelIndex.contains(i)) {
                    showModelIndex.add(i);
                    models.add( objRetM.models.get(i));
                }
            }

             showModelIndex.clear();
             jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                    .setQueryParameter("page", "1")
                    .setQueryParameter("pagesize", "20")
                    .setQueryParameter("orderby", "0")
                    .setQueryParameter("categoryID", "0")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

             jsonNode = jsonPromise.get(wsTimeout);
             objRetM = Util.transferJSON2Models(jsonNode);
             modelSize =3;
             int newSize = objRetM.models.size();
            if (objRetM.models.size()<3){
                modelSize = objRetM.models.size();
            }
            while (showModelIndex.size() < modelSize){
                int i = ran.nextInt(newSize);
                if (!showModelIndex.contains(i)) {
                    showModelIndex.add(i);
                    new_models.add( objRetM.models.get(i));
                }
            }



            jsonPromise = ws.url(webServiceUrl + "/memberws/listDesigners")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetDesigners objRetDs =Util.transferJSON2Designers(jsonNode);
            int designerSize =6;
            if (objRetDs.members.size()<6){
                designerSize = objRetDs.members.size();
            }
            designers = objRetDs.members.subList(0, designerSize);

        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        try {
            Promise<JsonNode> jsonPromiseBanners = ws.url(webServiceUrl + "/bannerws/getbanners")
                    .get().map(response -> response.asJson());

            JsonNode jsonNode = jsonPromiseBanners.get(wsTimeout);
            jBanners = jsonNode.toString();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ok(index.render("Welcome to Fast Lab.",new_models, models, jBanners,designers));
    }
    
    public Result uploadModel() {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");

        List<IIIModelCategory> categories = new ArrayList<IIIModelCategory>();
        List<IIIModelLicense> licenses = new ArrayList<IIIModelLicense>();
        List<Campaign> campaigns = new ArrayList<Campaign>();
        try {
            Promise<JsonNode> jsonPromise = null;
            JsonNode jsonNode = null;
            jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/getcategorylist")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetCategory objRetC = Util.transferJSON2Categories(jsonNode);
            categories = objRetC.categories;

            jsonPromise = ws.url(webServiceUrl + "/modellicensews/getlicenselist")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetLicense objRetL = Util.transferJSON2Licenses(jsonNode);
            licenses = objRetL.licenses;

            jsonPromise = ws.url(webServiceUrl + "/campaignws/getcampaignlist")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetCampaign objRetCP = Util.transferJSON2Campaigns(jsonNode);
            campaigns = objRetCP.campaigns;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
        return ok(uploadModel.render("Upload model.", categories, licenses, campaigns));
    }

    public Result submitModel(){
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        JsonNode bodyInput = request().body().asJson();
        ((ObjectNode)bodyInput).put("member_token", getCookie(memberToken));

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/model")
                .setHeader("access_key", webServiceKey)
                .setBody(bodyInput)
                .post(bodyInput).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0){
            return ok(objWS.retMessage);
        }else if (objWS.retCode == 20204001){
            removeCookie();
            return ok(objWS.retMessage);
        }else{
            return internalServerError(objWS.retMessage);
        }
    }
    @RequireCSRFCheck
    public Result submitUpdateModel(){
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");

        Form<formNewModel> userForm = Form.form(formNewModel.class);
        formNewModel formModel = userForm.bindFromRequest().get();

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/model")
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id",String.valueOf(formModel.getModelID()))
                .setHeader("member_token", getCookie(memberToken))
                .put(Json.toJson(formModel)).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0) {
            return redirect("/Fast-Lab-3D-Model-Gallery/0?pageIdx=1");
        } else if (objWS.retCode == 20204001){
            removeCookie();
            return ok(objWS.retMessage);
        } else {
            return internalServerError(objWS.retMessage);
        }
    }

    public Result uploadEvent() {
         if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        return ok(uploadEvent.render("Upload Event."));
    }
    @RequireCSRFCheck
    public Result submitEvent(){
        try{
            Http.MultipartFormData body = request().body().asMultipartFormData();
            if(body == null)
            {
                return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
            }
            List<Http.MultipartFormData.FilePart> fileParts = body.getFiles();
            if(fileParts.isEmpty())
            {
                return badRequest("Invalid request, no files have been included in the request.");
            }

            Form<formNewEvent> userForm = Form.form(formNewEvent.class);
            formNewEvent formEvent = userForm.bindFromRequest().get();

            // event image upload
            int i=0;
            for(Http.MultipartFormData.FilePart filePart: fileParts)
            {
                if (i==0){
                    File file = filePart.getFile();
                    uploadFile(file, "eventimage", filePart.getFilename());
                    formEvent.setImages("eventimage/"+filePart.getFilename());
                }
                else if (i==1)
                {
                    File file = filePart.getFile();
                    uploadFile(file, "eventimage", filePart.getFilename());
                    formEvent.setImageThumbnail("eventimage/"+filePart.getFilename());
                }
                i++;
            }

            JsonNode bodyInput = Json.toJson(formEvent);
            ((ObjectNode)bodyInput).put("member_token", getCookie(memberToken));

            Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/eventws/event")
                    .setHeader("access_key", webServiceKey)
                    .setBody(bodyInput)
                    .post(bodyInput).map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(wsTimeout);
            objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
            if (objWS.retCode == 0){
                return redirect("/");
            //    return ok(objWS.retMessage);
            }else{
                return internalServerError(objWS.retMessage);
            }
        }catch (Exception ex){
            System.out.println(ex.toString());
            return badRequest();
        }
    }

    public Result eventManage(Long eventID) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/getevent?eventID="+eventID)
                .get().map(response -> response.asJson());
        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetEvent objRetE = Util.transferJSON2Event(jsonNode);
        Events iiievent = objRetE.event;
        String showStartDate = sdf.format(iiievent.showStartDate);
        String showEndDate = sdf.format(iiievent.showEndDate);
        return ok(eventManage.render(iiievent, eventID, showStartDate, showEndDate));
    }

    public Result submitUpdateEvent(){
        try{
            Http.MultipartFormData body = request().body().asMultipartFormData();
            List<Http.MultipartFormData.FilePart> fileParts = body.getFiles();
            Form<formUpdateEvent> userForm = Form.form(formUpdateEvent.class);
            formUpdateEvent formEvent = userForm.bindFromRequest().get();

            // event image upload
            int i=0;
            for(Http.MultipartFormData.FilePart filePart: fileParts)
            {
                if (i==0){
                    File file = filePart.getFile();
                    uploadFile(file, "eventimage", filePart.getFilename());
                    formEvent.setImages("eventimage/"+filePart.getFilename());
                }
                else if (i==1)
                {
                    File file = filePart.getFile();
                    uploadFile(file, "eventimage", filePart.getFilename());
                    formEvent.setImageThumbnail("eventimage/"+filePart.getFilename());
                }
                i++;
            }

            Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/eventws/event")
                    .setHeader("access_key", webServiceKey)
                    .put(Json.toJson(formEvent)).map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(wsTimeout);
            objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
            if (objWS.retCode == 0){
                return ok(objWS.retMessage);
            }else{
                return internalServerError(objWS.retMessage);
            }
        }catch (Exception ex){
            System.out.println(ex.toString());
            return badRequest();
        }
    }

    public Result uploadCampaign() {
        return ok(uploadCampaign.render("Upload Campaign."));
    }

    public Result submitCampaign(){
        try{
            Form<formNewCampaign> userForm = Form.form(formNewCampaign.class);
            formNewCampaign formCampaign = userForm.bindFromRequest().get();

            Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/campaignws/campaign")
                    .setHeader("access_key", webServiceKey)
                    .post(Json.toJson(formCampaign)).map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(wsTimeout);
            objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
            if (objWS.retCode == 0){
                return ok(objWS.retMessage);
            }else{
                return internalServerError(objWS.retMessage);
            }
        }catch (Exception ex){
            System.out.println(ex.toString());
            return badRequest();
        }
    }

    public Boolean uploadFile(File file, String folder, String fileName) {
        checkFolder(folder);
        Boolean success = file.renameTo(new File(workingDir + "/public/" + folder + "/" + fileName));
        return success;
    }

    public Result event() {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/getAllEvents")
                .get().map(response -> response.asJson());
        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetEvents objRetE = Util.transferJSON2Events(jsonNode);
        List<Events> events = objRetE.events;
        return ok(event.render(events));

    }

    public Result eventDetail(String eventName) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/getevent?eventID="+eventName)
                .get().map(response -> response.asJson());
        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetEvent objRetE = Util.transferJSON2Event(jsonNode);
        Events iiievent = objRetE.event;
    	return ok(eventDetail.render(iiievent));
    }

    public Result resource() {
        return ok(resource.render("The resource of Fast Lab."));
    }

    public Result resourceDetail(String resourceName) {
        return ok(resourceDetail.render("The resource detail of " + resourceName + ".", resourceName));
    }

    public Result toolLab() {
        int number,index = 0;
        ArrayList<Integer>    contain = new ArrayList<Integer>();
        contain.add(99);
        contain.add(98);
        contain.add(97);
        contain.add(96); 
       
        number =  (int)((Math.random()*100)%4);
        List<Tool> tool = Tool.find.orderBy("toolID desc").findList();
     //   List<Tool> tool = Tool.find.all();
        List<Tool> del = new ArrayList<Tool>();  
  
        for (Tool entry : tool)
        {
          if(   contain.contains( entry.toolID.intValue()) &&  entry.toolID.intValue() != contain.get(number)   )          
         {
                  del.add(entry);
         }
        }
        tool.removeAll(del);
        return ok(toolLab.render("The tool detail of tool " , tool));
      //  return ok(toolLab.render("The tools of Fast Lab."));
    }

    public Result toolLabDetail(String toolName) {
        return ok(toolLabDetail.render("The tool detail of " + toolName + ".", toolName));
    }


    public Result listModel(String orderby,String pageIdx,String category) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                .setQueryParameter("page", pageIdx)
                .setQueryParameter("pagesize", pageSize)
                .setQueryParameter("orderby", orderby)
                .setQueryParameter("categoryID", category)
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetModels objRetM =Util.transferJSON2Models(jsonNode);

        jsonPromise = ws.url(webServiceUrl + "/memberws/listDesigners")
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        jsonNode = jsonPromise.get(wsTimeout);
        objRetDesigners objRetDs =Util.transferJSON2Designers(jsonNode);

        jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/getcategorylist")
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        jsonNode = jsonPromise.get(wsTimeout);
        objRetCategory objRetC = Util.transferJSON2Categories(jsonNode);

        String categoryName = "作品分類";
        if (!category.equals("0")){

            categoryName = objRetC.categories.get(Integer.valueOf(category)-1).categoryName;
        }

        if (objRetM.retCode == 0) {
            Double a = Math.ceil(Double.valueOf(String.valueOf(objRetM.totalCount))/Double.valueOf(pageSize));
            int maxPage = a.intValue();
            return ok(listModel.render(objRetM.models,maxPage,pageIdx,orderby, objRetDs.members, objRetC.categories,category,categoryName));
        } else if (objRetM.retCode == 20204001){
            removeCookie();
            return ok(objRetM.retMessage);
        } else {
            return internalServerError(objRetM.retMessage);
        }
    }

public Result test()
    {
     return ok(stlViewerHost);
    }

    public Result modelDetail(Long modelid) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/model")
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id", String.valueOf(modelid))
                .setHeader("member_token", getCookie(memberToken))
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetModel objRetM =Util.transferJSON2Model(jsonNode);
	String t =String.valueOf(objRetM.model.create_time);
        String createTime = sdf.format(objRetM.model.create_time);
        if (objRetM.retCode == 0){
            return ok(modelDetail.render(objRetM.model, objRetM.modelinteraction, stlViewerHost, objRetM.isCreater, createTime));
        }else{
            return internalServerError(objRetM.retMessage);
        }

    }

    public Result updateModel(Long modelid) {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");

        try {
            Promise<JsonNode> jsonPromise = null;
            JsonNode jsonNode = null;
            jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/getcategorylist")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetCategory objRetC = Util.transferJSON2Categories(jsonNode);

            jsonPromise = ws.url(webServiceUrl + "/modellicensews/getlicenselist")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetLicense objRetL = Util.transferJSON2Licenses(jsonNode);

            jsonPromise = ws.url(webServiceUrl + "/galleryws/modelforupdate")
                    .setHeader("access_key", webServiceKey)
                    .setHeader("model_id", String.valueOf(modelid))
                    .setHeader("member_token", getCookie(memberToken))
                    .get().map(response -> response.asJson());


            jsonNode = jsonPromise.get(wsTimeout);
            objRetModel objRetM =Util.transferJSON2Model(jsonNode);

            jsonPromise = ws.url(webServiceUrl + "/campaignws/getcampaignlist")
                    .setHeader("access_key", webServiceKey)
                    .get().map(response -> response.asJson());

            jsonNode = jsonPromise.get(wsTimeout);
            objRetCampaign objRetCP = Util.transferJSON2Campaigns(jsonNode);
            if (objRetM.retCode == 0){
                return ok(updateModel.render("Update model.", objRetC.categories, objRetL.licenses,objRetCP.campaigns,objRetM.model,modelid));
            }else{
                return internalServerError(objRetM.retMessage);
            }

        }catch (Exception e){
            System.out.println(e.getMessage());
            return internalServerError(e.toString());
        }



    }

    public Result modeldownloadclick() {
        JsonNode bodyInput = request().body().asJson();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/downloadclick")
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id", bodyInput.get("modelID").asText())
                .setHeader("member_token", getCookie(memberToken))
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0){
            return ok(objWS.retMessage);
        }else{
            return internalServerError(objWS.retMessage);
        }
    }

    public Result modelratingclick() {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        JsonNode bodyInput = request().body().asJson();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/scoreclick")
                .setQueryParameter("score", bodyInput.get("score").asText())
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id", bodyInput.get("modelID").asText())
                .setHeader("member_token", getCookie(memberToken))
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0){
            return ok(objWS.retMessage);
        } else if (objWS.retCode == 20204001){
            removeCookie();
            return ok(objWS.retMessage);
        }else{
            return internalServerError(objWS.retMessage);
        }
    }

    public Result modelcollectclick() {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        JsonNode bodyInput = request().body().asJson();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/collectionclick")
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id", bodyInput.get("modelID").asText())
                .setHeader("member_token", getCookie(memberToken))
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0){
            return ok(objWS.retMessage);
        }else if (objWS.retCode == 20204001){
            removeCookie();
            return ok(objWS.retMessage);
        }else{
            return internalServerError(objWS.retMessage);
        }
    }

    public Result modellikeclick() {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        JsonNode bodyInput = request().body().asJson();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/likeclick")
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id", bodyInput.get("modelID").asText())
                .setHeader("member_token", getCookie(memberToken))
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0){
            return ok(objWS.retMessage);
        }else if (objWS.retCode == 20204001){
            removeCookie();
            return ok(objWS.retMessage);
        }else{
            return internalServerError(objWS.retMessage);
        }
    }

    public Result postComment() {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        JsonNode bodyInput = request().body().asJson();
        ((ObjectNode)bodyInput).put("member_token", getCookie(memberToken));

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modelcomment")
                .setHeader("access_key", webServiceKey)
                .setHeader("model_id", bodyInput.get("modelID").asText())
                .setBody(bodyInput)
                .post(bodyInput).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0){
            return ok(objWS.retMessage);
        }else if (objWS.retCode == 20204001){
            removeCookie();
            return ok(objWS.retMessage);
        }else{
            return internalServerError(objWS.retMessage);
        }
    }

    public Result explore(int categoryId) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modelcategorylist")
                .setQueryParameter("page", page)
                .setQueryParameter("pagesize", pageSize)
                .setQueryParameter("categoryId", Integer.toString(categoryId))
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetModels objRetM =Util.transferJSON2Models(jsonNode);

        jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/getcategorylist")
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        jsonNode = jsonPromise.get(wsTimeout);
        objRetCategory objRetC = Util.transferJSON2Categories(jsonNode);
        return ok(explore.render(objRetM.models,objRetC.categories));
    }
    public Result signIn() {
   return ok(signIn.render("The sign-in page of Fast Lab."));
    }

    public Result loginForm() {
       System.out.println("XXXXXXXXOOOOO");
        DynamicForm df = play.data.Form.form().bindFromRequest();

        try{
            if (!verify(df.data().get("g-recaptcha-response"))){
                return ok("reCAPTCHA verify fail");
            }
        }catch (IOException e){
            return internalServerError(e.toString());
        }



        String email = df.data().get("email");
        String password = df.data().get("password");
        if(email.length() == 0 || password.length() == 0 ){
            return ok("please input email and password");
        }
        if(df.data().get("remember_email") != null){
            response().setCookie(rememberEmail, email);
        }else{
            response().discardCookie(rememberEmail);
        }
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/logincheck")
                .setHeader("access_key", webServiceKey).setHeader("email", email)
                .setHeader("password", password)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0) {
            setCookies(objWS.retMessage);
            return redirect("/");
        } else {
            return internalServerError(objWS.retMessage);
        }
    }
    @RequireCSRFCheck
    public Result FBloginForm() {
        String Name_utf8;
        JsonNode bodyInput = request().body().asJson();
        System.out.println(bodyInput.get("FBID").asText());
        System.out.println(bodyInput.get("FBAccessToken").asText());
        System.out.println(bodyInput.get("country_id").asText());
        System.out.println(bodyInput.get("NAME").asText());
        System.out.println(bodyInput.get("EMAIL").asText());
        ((ObjectNode)bodyInput).put("ip",request().remoteAddress() );
     

       try{
           Name_utf8 = URLEncoder.encode( bodyInput.get("NAME").asText(),"UTF-8");
        }catch (IOException e){
            return ok("FB name utf8 error");
        }





        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/logincheckFB")
                .setHeader("access_key", webServiceKey)
                .setHeader("FBID", bodyInput.get("FBID").asText())
                .setHeader("FBAccessToken", bodyInput.get("FBAccessToken").asText())
                .setHeader("country_id", bodyInput.get("country_id").asText())
           //     .setHeader("NAME", bodyInput.get("NAME").asText())
                .setHeader("NAME", Name_utf8) 
         //       .setHeader("NAME",URLEncoder.encode( bodyInput.get("NAME").asText(),"UTF-8"))
                .setHeader("EMAIL", bodyInput.get("EMAIL").asText())
                .setHeader("ip", bodyInput.get("ip").asText())
                .get().map(response -> response.asJson());
	
        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
         

	 if (objWS.retCode == 0) {
            setCookies(objWS.retMessage);
		
            //System.out.println("***************");    
	    //Long MemberID = convertMemberTokenToMemberID(objWS.retMessage);
	    //verifyID(MemberID); 
 	 
            return redirect("/");
        } else {
            return internalServerError(objWS.retMessage);
        }

    }

    public Result signUp() {
        Session session = Http.Context.current().session();
	
	
	
	if(session.get("id")==null){
	return redirect("/");
	}else {	
        int key =  Integer.valueOf(session.get("id"));
	return ok(signUp.render("The sign-in page of Fast Lab.",key));
	}
	//	return redirect("/");
	

    }
   
    public Result signUp_op() {
   	DynamicForm dynamicForm = Form.form().bindFromRequest();
        //Logger.info(dynamicForm.get("id"));
        Session session = Http.Context.current().session();
        session.put("id",dynamicForm.get("id"));
   	 return ok(signUp_op.render());

    }

    public Result signUpForm() {
        DynamicForm df = play.data.Form.form().bindFromRequest();
        formNewMember objMember = new formNewMember();
        objMember.setEmail(df.data().get("email"));
        objMember.setPassword(df.data().get("password"));
        objMember.setNick_name(df.data().get("user_name"));
        objMember.setCountry_id(1);

        JsonNode bodyInput = Json.toJson(objMember);
        ((ObjectNode)bodyInput).put("ip",request().remoteAddress() );


        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/memberregister")
                .setHeader("access_key", webServiceKey)
                .post(bodyInput).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0) {
            return redirect("/Fast-Lab/sign-in");
        } else {
            return internalServerError(objWS.retMessage);
        }
    }
	@RequireCSRFCheck	
    public Result resetPasswordSubmit() {
        Form<formResetPassword> userForm = Form.form(formResetPassword.class);
        formResetPassword formResetPassword = userForm.bindFromRequest().get();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/changepassword")
                .setHeader("access_key", webServiceKey)
                .put(Json.toJson(formResetPassword)).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0) {
            return redirect("/");
        } else {
            return internalServerError(objWS.retMessage);
        }

    }

    public Result logout(){
        removeCookie();
        return redirect("/");
    }

    public Result resetpassword(String email,String token){
        return ok(resetPassword.render("Reset password", email, token));
    }
   @RequireCSRFCheck
    public Result sendResetPassword(){
      
        Form<formForgetPassword> userForm = Form.form(formForgetPassword.class);
        formForgetPassword formForgetPassword = userForm.bindFromRequest().get();
        String email = formForgetPassword.getEmail();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/resetpassword")
                .setHeader("access_key", webServiceKey)
                .setQueryParameter("email", email)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0) {
            return redirect("/");
        } else {
            return internalServerError(objWS.retMessage);
        }
          // return ok();
    }

    public Result uploadBanner() {
        if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        return ok(uploadBanner.render("Upload Event."));
    }


    @RequireCSRFCheck
    public Result submitBanner(){
        try{
            Http.MultipartFormData body = request().body().asMultipartFormData();
            if(body == null)
            {
                return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
            }
            List<Http.MultipartFormData.FilePart> fileParts = body.getFiles();
            if(fileParts.isEmpty())
            {
                return badRequest("Invalid request, no files have been included in the request.");
            }

            Form<formNewBanner> userForm = Form.form(formNewBanner.class);
            formNewBanner formBanner = userForm.bindFromRequest().get();

            // event image upload
            for(Http.MultipartFormData.FilePart filePart: fileParts)
            {
                File file = filePart.getFile();
                uploadFile(file, "bannerimage", filePart.getFilename());
                formBanner.setBannerImage("bannerimage/"+filePart.getFilename());
            }

            JsonNode bodyInput = Json.toJson(formBanner);
            ((ObjectNode)bodyInput).put("member_token", getCookie(memberToken));

            Promise<JsonNode> jsonPromise = null;
            jsonPromise = ws.url(webServiceUrl + "/bannerws/banner")
                    .setHeader("access_key", webServiceKey)
              //      .post(Json.toJson(formBanner)).map(response -> response.asJson());
                    .setBody(bodyInput)
                    .post(bodyInput).map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(wsTimeout);
            objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
            if (objWS.retCode == 0) {
          //      return redirect("/Upload-Banner");
                 return redirect("/");
            } else {
                return internalServerError(objWS.retMessage);
            }
        }catch (Exception ex){
            System.out.println(ex.toString());
            return badRequest();
        }
    }

    public Result myProfile() {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/member")
                .setHeader("access_key", webServiceKey)
                .setHeader("member_token", getCookie(memberToken))
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetMember objRetM =Util.transferJSON2Member(jsonNode);
        if (objRetM.retCode == 0){
            return ok(myProfile.render("Edit Your Own Profile.",objRetM.member));
        }else{
            return internalServerError(objRetM.retMessage);
        }

    }

    public Result submitProfile(){
        DynamicForm df = play.data.Form.form().bindFromRequest();
        formNewMember objMember = new formNewMember();
        objMember.setEmail(df.data().get("email"));
        objMember.setNick_name(df.data().get("name"));
        objMember.setDescription(df.data().get("description"));
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/member")
                .setHeader("access_key", webServiceKey)
                .setHeader("member_token", df.data().get("memberToken"))
                .put(Json.toJson(objMember)).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS = Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode == 0) {
            return ok(objWS.retMessage);
        } else if (objWS.retCode == 20104001){
            removeCookie();
            return ok(objWS.retMessage);
        } else {
            return internalServerError(objWS.retMessage);
        }
    }

    public Result membergallery(String orderby) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                .setQueryParameter("page", page)
                .setQueryParameter("pagesize", pageSize)
                .setQueryParameter("orderby", orderby)
                .setQueryParameter("categoryID", "0")
                .setHeader("member_token", getCookie(memberToken))
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetModels objRetM =Util.transferJSON2Models(jsonNode);
        if (objRetM.retCode == 0) {
            return ok(membergallery.render(objRetM.models));
        } else if (objRetM.retCode == 20204001){
            removeCookie();
            return ok(objRetM.retMessage);
        } else {
            return internalServerError(objRetM.retMessage);
        }
    }

    public Result myCollection(String sort) {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/mycollectlist")
                .setQueryParameter("page", page)
                .setQueryParameter("pagesize", pageSize)
                .setQueryParameter("sort", sort)
                .setHeader("member_token", getCookie(memberToken))
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetModels objRetM =Util.transferJSON2Models(jsonNode);
        if (objRetM.retCode == 0) {
            return ok(myCollection.render(objRetM.models));
        } else if (objRetM.retCode == 20204001){
            removeCookie();
            return ok(objRetM.retMessage);
        } else {
            return internalServerError(objRetM.retMessage);
        }
    }

    public Result classifiedModel() {
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                .setQueryParameter("page", page)
                .setQueryParameter("pagesize", pageSize)
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetModels objRetM =Util.transferJSON2Models(jsonNode);
        return ok(classifiedModel.render(objRetM.models));
    }
    
    public Result aboutUs() {
        return ok(aboutUs.render("Who we are."));
    }
    public Result privacyPolicy() {
        return ok(privacyPolicy.render("Fast Lab Privacy Policy."));
    }

    public Result imageAt(String folder, String fileName) throws FileNotFoundException {
        File imageFile = new File(workingDir + "/public/" + folder + "/" + fileName);
        if (imageFile.exists()) {
            String resourceType = "image+"+fileName.substring(fileName.length()-3);
            return ok(new FileInputStream(imageFile)).as(resourceType);
        } else {
            return notFound(imageFile.getAbsoluteFile());
        }
    }

    public Result attachmentAt(String folder, String fileName, String modelId) throws FileNotFoundException {
        File imageFile = new File(workingDir + "/public/" + folder + "/" + fileName);
        if (imageFile.exists()) {
            JsonNode bodyInput = request().body().asJson();
            Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/galleryws/downloadclick")
                    .setHeader("access_key", webServiceKey)
                    .setHeader("model_id", modelId)
                    .setHeader("member_token", getCookie(memberToken))
                    .get().map(response -> response.asJson());

            JsonNode jsonNode = jsonPromise.get(wsTimeout);
            objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
            if (objWS.retCode == 0){
                String resourceType = "application/octet-stream";
                return ok(new FileInputStream(imageFile)).as(resourceType);
            }else{
                return internalServerError(objWS.retMessage);
            }
        } else {
            return notFound(imageFile.getAbsoluteFile());
        }
    }

    public Result stlAtForViewer(String fileName) throws FileNotFoundException {
        File imageFile = new File(workingDir + "/public/stl/" + fileName);
        if (imageFile.exists()) {
            String resourceType = "application/octet-stream";
            return ok(new FileInputStream(imageFile)).as(resourceType);
        } else {
            return notFound(imageFile.getAbsoluteFile());
        }
    }
/*
    public Result objAtForViewer(String fileName) throws FileNotFoundException {
        File imageFile = new File(workingDir + "/public/stl/" + fileName);
        if (imageFile.exists()) {
            String resourceType = "application/octet-stream";
            return ok(new FileInputStream(imageFile)).as(resourceType);
        } else {
            return notFound(imageFile.getAbsoluteFile());
        }
    }
*/

    public Result uploadFileByForm() {
        Http.MultipartFormData body = request().body().asMultipartFormData();
        if(body == null)
        {
            return badRequest("Invalid request, required is POST with enctype=multipart/form-data.");
        }
        List<Http.MultipartFormData.FilePart> fileParts = body.getFiles();
        if(fileParts.isEmpty())
        {
            return badRequest("Invalid request, no files have been included in the request.");
        }

        List<objFileUpload> objFUList = new ArrayList<objFileUpload>();
        for(Http.MultipartFormData.FilePart filePart: fileParts)
        {
            File file = filePart.getFile();
            Long filesize = file.length();
            int file_type =0;

            if (filesize > 31457280) return ok("{\"retCode\":\"1\",\"retMessage\":\"File > 30MB\"}");
            String fileName = modifyFileName(filePart.getFilename(), getCookie(memberToken));
          String fileShowName = filePart.getFilename();
            String subName = filePart.getFilename().substring(filePart.getFilename().lastIndexOf(".")+1);
            if(filePart.getKey().equals("photo")){
                String folder = "myProfile";
                checkFolder(folder);
                file.renameTo(new File(workingDir + "/public/" + folder + "/" + fileName));

                formUpdateMember objMember = new formUpdateMember();
                objMember.setBanner_image_name(fileName);
                Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/member")
                        .setHeader("access_key", webServiceKey)
                        .setHeader("member_token", getCookie(memberToken))
                        .put(Json.toJson(objMember)).map(response -> response.asJson());
            }//上傳後可接受格式
           else if(subName.equals("stl")||subName.equals("STL")||subName.equals("obj")||subName.equals("OBJ")){
                String folder = "stl";
                checkFolder(folder);
                file.renameTo(new File(workingDir + "/public/" + folder + "/" + fileName));

                 switch (subName){
                   case "stl":
                       file_type =0;
                   break;
                   case "STL":
                       file_type =0;
                   break;
                   case "obj":
                       file_type =1;
                   break;
                   case "OBJ":
                       file_type =1;
                   break;
                    case "stp":
                       file_type =2;
                   break;
                   case "step":
                       file_type =3;
                   break;
                   case "sat":
                       file_type =4;
                   break;
                   case "pdf":
                       file_type =101;
                   break;
                   case "PDF":
                       file_type =101;
                   break;
                   case "mtl":
                       file_type =101;
                   break;
                   case "7z":
                       file_type =102;
                   break;
		   case "zip":
                       file_type =104;
                   break;
                 }


                     




                objModelAttachments objMA= new objModelAttachments();
                objMA.setFileName(fileName);
                objMA.setFileShowname(fileShowName);
                objMA.setFileSize(filesize);
                objMA.setFileType(String.valueOf(file_type));
                Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/stlws/file")
                        .setHeader("access_key", webServiceKey)
                        .post(Json.toJson(objMA)).map(response -> response.asJson());
            }else{
                String folder = "thumbnail";
                checkFolder(folder);
                file.renameTo(new File(workingDir + "/public/" + folder + "/" + fileName));

                objModelThumbnails objMT = new objModelThumbnails();
                objMT.setThumbnailFilename(fileName);
                objMT.setThumbnailShowname(fileShowName);
                objMT.setThumbnailResize(filesize);
                objMT.setIsSystemCreated(false);
                Promise<JsonNode>  jsonPromise = ws.url(webServiceUrl + "/thumbnailws/file")
                        .setHeader("access_key", webServiceKey)
                        .post(Json.toJson(objMT)).map(response -> response.asJson());
            }
            objFileUpload objFU = new objFileUpload();
            objFU.fileName = fileName;
            objFU.fileShowname = fileShowName;
            objFUList.add(objFU);
        }
        objRetFileUpload objRFU = new objRetFileUpload();
        objRFU.files = objFUList;
        objRFU.retCode = 0;
        objRFU.retMessage = "Upload files success";
        Gson gson = new Gson();
        String result = gson.toJson(objRFU);
        return ok(result);
    }

    public static String modifyFileName(String originalName, String memberToken){
        String newFileName = "";
        if(originalName.length() > 0 ){
            String subName = originalName.substring(originalName.lastIndexOf(".")+1);
            Long time = new Date().getTime();
            newFileName = memberToken + "_" + time + "." + subName;
        }
        return newFileName;
    }

    public static void checkFolder(String directoryName) {
        File theDir = new File(workingDir + "/public/" + directoryName);
        // if the directory does not exist, create it
        if (!theDir.exists()) {
            System.out.println("creating directory: " + directoryName);
            boolean result = false;

            try {
                theDir.mkdir();
                result = true;
            } catch (SecurityException se) {
                //handle it
            }
            if (result) {
                System.out.println("DIR created");
            }
        }
    }

    public static String getCookie(String key){
        if(request().cookies().get(key) != null) {
            return request().cookies().get(key).value().toString();
        }else {
            return "";
        }
    }

	
	public static String VerifyToken(){
	
	String Member_token = getCookie(memberToken);
	System.out.println(Member_token);
	List<MemberToken> dbMT = MemberToken.find.where().eq("memberToken",Member_token).findList();
	if (!dbMT.isEmpty()){			
        System.out.println(dbMT.get(0).member.memberID);
	Long MemberID = dbMT.get(0).member.memberID;	
	System.out.println(MemberID);
	
	//List<Admin_permit> record=null;
	List<Admin_permit> record = Admin_permit.find.where().eq("member_id",MemberID).findList();
	if (record.isEmpty()){
	 return ("");
	}else{
        response().setCookie("rec",(String.valueOf(record.get(0).group)));
	System.out.println(record.get(0).group);
	return ("true");
	}
	}return ("");

	    }

    private static void setCookies(String token) {
        response().setCookie(memberToken, token);
    }

    private static void removeCookie() {
        response().discardCookie(memberToken);
    }

    public Result termofUse(){
        return ok(termofUse.render(""));
    }

    public Result signUpDeclare(){
        return ok(signUpDeclare.render(""));
    }


    private boolean verify(String gRecaptchaResponse) throws IOException {

        String url = "https://www.google.com/recaptcha/api/siteverify";
        String secret = "6LeG-h0TAAAAAGHZyezP9fnpWFNDNBpKP7F9l505";


        try{
            URL obj = new URL(url);
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // add reuqest header
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            String postParams = "secret=" + secret + "&response="
                    + gRecaptchaResponse;

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            System.out.println("\nSending 'POST' request to URL : " + url);
            System.out.println("Post parameters : " + postParams);
            System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // print result
            System.out.println(response.toString());

            //parse JSON response and return 'success' value
//            JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
//            JsonObject jsonObject = jsonReader.readObject();
//            jsonReader.close();

//            return jsonObject.getBoolean("success");
            JsonObject jo = new JsonParser().parse(response.toString()).getAsJsonObject();
            return jo.get("success").getAsBoolean();
        }catch(Exception e){
            e.printStackTrace();
            return false;
        }
    }

    public Result designerDetail(Long memberId) {
 
    //    List<Campaign> campaign = Campaign.find.where().eq("member_id",memberId).findList();
         List<Campaign> campaign = Campaign.find.where().eq("member_id",memberId).orderBy("campaignID desc").findList();
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/listDesigners")
                .setHeader("access_key", webServiceKey)
                .setHeader("member_id", memberId.toString())
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetDesigners objRetDs =Util.transferJSON2Designers(jsonNode);

        jsonPromise = ws.url(webServiceUrl + "/galleryws/modellist")
                .setQueryParameter("page", page)
                .setQueryParameter("pagesize", pageSize)
                .setQueryParameter("orderby", "0")
                .setQueryParameter("categoryID", "0")
                .setHeader("member_id", memberId.toString())
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        jsonNode = jsonPromise.get(wsTimeout);
        objRetModels objRetM =Util.transferJSON2Models(jsonNode);
        if (objRetDs.retCode == 0) {
            return ok(designerDetail.render("Designer Detail.",objRetDs.members.get(0), objRetM.models, campaign));
        } else {
            return internalServerError(objRetDs.retMessage);
        }
    }

    public Result designerManage() {
       if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/listDesigners")
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetDesigners objRetDs =Util.transferJSON2Designers(jsonNode);
        if (objRetDs.retCode == 0) {

            return ok(designerManage.render("Designer Manage.",objRetDs.members));

        } else {
            return internalServerError(objRetDs.retMessage);
        }
    }

    public Result newDesigner() {

        JsonNode bodyInput = request().body().asJson();

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/setMemberAsDesigner")
                .setHeader("access_key", webServiceKey)
                .setQueryParameter("email", bodyInput.get("email").asText())
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode != 0){
            return internalServerError(objWS.retMessage);
        }
        return ok(objWS.retMessage);
    }

    public Result removeDesigner() {

        JsonNode bodyInput = request().body().asJson();

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/memberws/removeDesignerIdentifier")
                .setHeader("access_key", webServiceKey)
                .setQueryParameter("email", bodyInput.get("email").asText())
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode != 0){
            return internalServerError(objWS.retMessage);
        }
        return ok(objWS.retMessage);
    }

    public Result categoryManage() {
       if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");
        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/getcategorylist")
                .setHeader("access_key", webServiceKey)
                .get().map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetCategory objRetCs =Util.transferJSON2Categories(jsonNode);
        if (objRetCs.retCode == 0) {

            return ok(categoryManage.render("category Manage.", objRetCs.categories));

        } else {
            return internalServerError(objRetCs.retMessage);
        }
    }

    public Result newCategory() {

        JsonNode bodyInput = request().body().asJson();

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/category")
                .setHeader("access_key", webServiceKey)
                .post(bodyInput).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode != 0){
            return internalServerError(objWS.retMessage);
        }
        return ok(objWS.retMessage);
    }

    public Result updateCategory() {

        JsonNode bodyInput = request().body().asJson();

        Promise<JsonNode> jsonPromise = ws.url(webServiceUrl + "/modelcategoryws/category")
                .setHeader("access_key", webServiceKey)
                .setQueryParameter("category_id", bodyInput.get("category_id").asText())
                .put(bodyInput).map(response -> response.asJson());

        JsonNode jsonNode = jsonPromise.get(wsTimeout);
        objRetWS objWS =Util.transferJSON2CommonResponse(jsonNode);
        if (objWS.retCode != 0){
            return internalServerError(objWS.retMessage);
        }
        return ok(objWS.retMessage);
    }
    public Result forgetPassword(){
  //     Optional<CSRF.Token> token = CSRF.getToken(request());
     //  CSRF.getToken(request()).map(t -> t.value()).orElse("no token");
      //  return ok(forgetPassword.render("",token.map(CSRF.Token::value).orElse("")));
          return ok(forgetPassword.render(""));
    }






}
