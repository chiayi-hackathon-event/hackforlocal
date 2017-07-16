package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.SqlRow;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import iiiException.*;
import library.WSAuthenticator;
import models.*;
import objects.objRetModel;
import objects.objRetModels;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by abow on 15/11/8.
 */
@Security.Authenticated(WSAuthenticator.class)
public class GalleryWS extends UniTools{
    public Result getmodel(){

        objRetModel objRetM;
        try{
            if (request().getHeader("model_id") == null ||
                request().getHeader("model_id").isEmpty() ||
                Long.valueOf(request().getHeader("model_id")) ==0){
                throw new parameterMissingException("input parameter missing");

            }

            Long model_id = Long.valueOf(request().getHeader("model_id"));

            // get model by model_id
            IIIModel dbModel = IIIModel.find.byId(model_id);
            if (dbModel == null){
                throw new noData("can not find available model");
            }else{
                dbModel = modelclick(0,dbModel,null);
                Long member_id = 0L;
                if (request().getHeader("member_token") != null &&
                    !request().getHeader("member_token").isEmpty()){
                    member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                }
                IIIModelMemberAction dbMMA =  modelclickwithmember(0,dbModel,member_id,0);

                // TODO can not solve @ManyToOne lazy issue, so we need print out the value
                // each refresh(), response time 300 ms more
            List<IIIModelAttachment> atModel = IIIModelAttachment.find.where().eq("model_id", dbModel.modelID).eq("status", 1).findList();
            List<IIIModelThumbnail>  thModel = IIIModelThumbnail.find.where().eq("model_id", dbModel.modelID).eq("status",1).findList();
             dbModel.modelAttachments = atModel;
             dbModel.modelThumbnails = thModel;

                dbModel.modelcategory.refresh();
                dbModel.modellicense.refresh();
                dbModel.member.refresh();

                objRetM = new objRetModel();
                objRetM.retCode = 0;
                objRetM.retMessage = "success";
                objRetM.model = dbModel;
                objRetM.modelinteraction = dbMMA;
                objRetM.isCreater = (dbModel.member.memberID.equals(member_id))?true:false;
            }
        }catch(parameterMissingException e){
            objRetM = new objRetModel(GalleryWS.class,"02001",e.toString());
        }catch(noData e){
            objRetM = new objRetModel(GalleryWS.class,"03001",e.toString());
        }catch(Exception ee){
            objRetM = new objRetModel(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetM)).as("application/json");
    }

    public Result getmodelforupdate(){

        objRetModel objRetM;
        try{
            if (request().getHeader("model_id") == null || request().getHeader("model_id").isEmpty() ||
                request().getHeader("member_token") == null || request().getHeader("member_token").isEmpty() ||
                Long.valueOf(request().getHeader("model_id")) ==0){
                throw new parameterMissingException("input parameter missing");

            }

            Long model_id = Long.valueOf(request().getHeader("model_id"));
//            Long model_id = Long.valueOf("201603200145049");
            // get model by model_id
             System.out.println("!@@#$$%%%");
      //       IIIModel dbModel = IIIModel.find.byId(model_id);
         //  IIIModel dbModel = IIIModel.find.byId(Long.valueOf("201603200145049"));
      //    IIIModel dbModel = IIIModel.find.where().select("*").eq("model_id", model_id).eq("modelThumbnails.status",1);
           // List<IIIModel> dbModel = Ebean.find(IIIModel.class).where().select("*");
         //   List<IIIModel>  dbModel1 = Ebean.find(IIIModel.class).select("*").where().eq("model_id", model_id).eq("modelAttachments.status",1).eq("modelThumbnails.status",1).findList();
//   List<IIIModel>  dbModel1 = Ebean.find(IIIModel.class).select("*").where().eq("t0.status",1).eq("t0.model_id", model_id).findList();
 //           IIIModel dbModel = dbModel1.get(0);
             IIIModel dbModel = IIIModel.find.byId(model_id);
            List<IIIModelAttachment> atModel = IIIModelAttachment.find.where().eq("model_id", model_id).eq("status", 1).findList();
            List<IIIModelThumbnail>  thModel = IIIModelThumbnail.find.where().eq("model_id", model_id).eq("status",1).findList();
             dbModel.modelAttachments = atModel;
             dbModel.modelThumbnails = thModel;             

            System.out.println(dbModel.modelAttachments.get(0).fileShowname);
          //  System.out.println(dbModel.modelAttachments.get(1).fileShowname);
            if (dbModel == null){
                throw new noData("can not find available model");
            }else{
                dbModel = modelclick(0,dbModel,null);
                Long member_id = 0L;
                if (request().getHeader("member_token") != null &&
                        !request().getHeader("member_token").isEmpty()){
                    member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                }
                System.out.println(dbModel.member.memberID );
                System.out.println(member_id );
                if (!dbModel.member.memberID.equals(member_id)){
                    throw new notCreator("not model creater");
                }

                // TODO can not solve @ManyToOne lazy issue, so we need print out the value
                // each refresh(), response time 300 ms more
                dbModel.modelcategory.refresh();
                dbModel.member.refresh();

                objRetM = new objRetModel();
                objRetM.retCode = 0;
                objRetM.retMessage = "success";
                objRetM.model = dbModel;
            }
        }catch(parameterMissingException e){
            objRetM = new objRetModel(GalleryWS.class,"02001",e.toString());
        }catch(noData e){
            objRetM = new objRetModel(GalleryWS.class,"03001",e.toString());
        }catch(notCreator e){
            objRetM = new objRetModel(GalleryWS.class,"05001",e.toString());
        }catch(Exception ee){
            objRetM = new objRetModel(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetM)).as("application/json");
    }


    public Result newmodel(){

        objRetWS objWS;
        Ebean.beginTransaction();
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("member_token")       || objJson.get("member_token").asText().isEmpty()    ||
                !objJson.has("modelName")          || objJson.get("modelName").asText().isEmpty()       ||
                !objJson.has("category")         || objJson.get("category").asInt() ==0                 ||
                !objJson.has("license")         || objJson.get("license").asInt() ==0             ){
                throw new parameterMissingException("input parameter missing");
            }

            // get member_id by member token
            Long member_id = convertMemberTokenToMemberID(objJson.get("member_token").asText());
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }
            Member dbMember = Member.find.byId(member_id);
            if (dbMember == null){
                throw new noData("can not find available member");
            }

            // get IIIModelCategory
            Integer categoryID = objJson.get("category").asInt();
            IIIModelCategory dbCategory = IIIModelCategory.find.byId(categoryID);
            if (dbCategory == null){
                throw new noData("can not find category");
            }

            // get IIIModelLicense
            Integer licenseID = objJson.get("license").asInt();
            IIIModelLicense dbLicense = IIIModelLicense.find.byId(licenseID);
            if (dbLicense == null){
                throw new noData("can not find license");
            }


            IIIModel dbModel = new IIIModel();
            long modelId = generateModelID(objJson.get("modelName").asText());
            dbModel.modelID = modelId;
            dbModel.averageScore = 0.0f;
            dbModel.create_time = new Date();
            dbModel.download = 0;
            dbModel.member = dbMember;
            dbModel.modelcategory = dbCategory;
            dbModel.modellicense = dbLicense;
            dbModel.viewedTimes = 0;

//            dbModel.modelThumbnails = null;
//            dbModel.modelAttachments = null;
//            dbModel.modelTags = null;


            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "modelName":
                        dbModel.modelName = objJson.get(key).asText();
                        break;
                    case "description":
                        dbModel.description = objJson.get(key).asText();
                        break;
                    case "scenario":
                        dbModel.scenario = objJson.get(key).asText();
                        break;
                    case "instruction":
                        dbModel.instruction = objJson.get(key).asText();
                        break;
                    case "stl":
                        String[] stls = objJson.get(key).asText().split(";");
                        if (stls.length >0){
                            for (String strStl : stls) {
                                IIIModelAttachment dbMA = IIIModelAttachment.find.byId(strStl);
                                if (dbMA == null)
                                    throw new noData("can not find attachemnt");
                                dbMA.iiimodel = dbModel;
                                dbModel.modelAttachments.add(dbMA);
                            }
                        }
                        break;
                    case "image":
                        String[] images = objJson.get(key).asText().split(";");
                        if (images.length >0){
                            for (String strImage : images) {
                                IIIModelThumbnail dbMT = IIIModelThumbnail.find.byId(strImage);
                                if (dbMT == null)
                                    throw new noData("can not find thumbnail");
                                dbMT.iiimodel = dbModel;
                                dbModel.modelThumbnails.add(dbMT);
                            }
                        }
                        break;
                    // for campaign
//                    case "modelTags":
//                        String[] campaigns = objJson.get(key).asText().split(";");
//                        if (campaigns.length > 0){
//                            for(String tag : campaigns){
//                                CampaignWS.assignModel2Campaign(Long.valueOf(tag), modelId);
//                            }
//                        }
//                        break;

                }
            }

            Ebean.save(dbModel);
            Ebean.save(dbModel.modelAttachments);
            Ebean.commitTransaction();
            if (objJson.has("modelTags") && objJson.get("modelTags").asInt() != 0){
                String[] campaigns = objJson.get("modelTags").asText().split(";");
                if (campaigns.length > 0){
                    for(String campaignID : campaigns){
                        Campaign dbC =  Campaign.find.byId(Long.valueOf(campaignID));
                        if (dbC == null) throw new noData("there is no campaign data");

                        IIIModel dbM = IIIModel.find.byId(modelId);
                        if (dbC == null) throw new noData("there is no model data");

                        dbC.entitiesB.add(dbM);

                        Ebean.save(dbC);
                        objWS = new objRetWS(0,"assign model to campagin success");
                    }
                }
            }
            objWS = new objRetWS(0,dbModel.modelID.toString());

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch(invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch(noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    private String convertorderby(int orderby){
        switch (orderby){
            case 0:
                return "create_time";
            case 1:
                return "download";
            case 2:
                return "viewedTimes";
            case 3:
                return "likeCount";
            case 4:
                return "collectCount";
            default:
                return "create_time";
        }


    }
    public Result getmodellist(int page, int pagesize, int orderby,int categoryID){
        objRetModels objRetMs = new  objRetModels();
        try{
            page = (page ==0)?1:page;
            pagesize = (pagesize ==0)?20:pagesize;

            List<IIIModel> dbModels = null;
            List<IIIModelAttachment> atModel = null;
            List<IIIModelThumbnail>  thModel = null;
            int totalCount = 0;

            if (request().getHeader("member_token") != null &&
                    !request().getHeader("member_token").isEmpty()){
                // get member_id by member token
                Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                if (member_id == 0){
                    throw new invalidMemberToken("invalid member token");
                }
                dbModels = Ebean.find(IIIModel.class).where().eq("status",1).eq("member_id", member_id).setFirstRow((page - 1) * pagesize).setMaxRows(pagesize).orderBy(convertorderby(orderby) + " desc").findList();
                totalCount = Ebean.find(IIIModel.class).where().eq("status",1).gt("member_id", member_id).findRowCount();
            }else if(request().getHeader("member_id") != null &&
                    !request().getHeader("member_id").isEmpty()) {
                dbModels = Ebean.find(IIIModel.class).where().eq("status",1).eq("member_id", request().getHeader("member_id")).setFirstRow((page - 1) * pagesize).setMaxRows(pagesize).orderBy(convertorderby(orderby) + " desc").findList();
                totalCount = Ebean.find(IIIModel.class).where().eq("status",1).gt("member_id", request().getHeader("member_id")).findRowCount();
            }else {
                if (categoryID !=0){
                    dbModels = Ebean.find(IIIModel.class).where().eq("status",1).gt("member_id", 0).eq("category_id", categoryID).setFirstRow((page - 1) * pagesize).setMaxRows(pagesize).orderBy(convertorderby(orderby) + " desc").findList();
                    totalCount = Ebean.find(IIIModel.class).where().eq("status",1).gt("member_id", 0).eq("category_id", categoryID).findRowCount();
                }else{
                    dbModels = Ebean.find(IIIModel.class).where().eq("status",1).gt("member_id", 0).setFirstRow((page - 1) * pagesize).setMaxRows(pagesize).orderBy(convertorderby(orderby) + " desc").findList();
                    totalCount = Ebean.find(IIIModel.class).where().eq("status",1).gt("member_id", 0).findRowCount();
                }
            }

            for (IIIModel dbM : dbModels){
      atModel = IIIModelAttachment.find.where().eq("model_id", dbM.modelID).eq("status", 1).findList();          
      thModel = IIIModelThumbnail.find.where().eq("model_id",dbM.modelID).eq("status",1).findList();
      dbM.modelAttachments = atModel;
      dbM.modelThumbnails = thModel;
                dbM.member.refresh();
            }
            objRetMs.retCode = 0;
            objRetMs.retMessage = "get model list success";
            objRetMs.models = dbModels;
            objRetMs.totalCount =totalCount;
        }catch(invalidMemberToken e){
            objRetMs.retCode = 20204001;
            objRetMs.retMessage = e.toString();
        }catch(Exception ee){
            objRetMs.retCode = 1;
            objRetMs.retMessage = ee.toString();
        }
        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetMs)).as("application/json");

    }
    public Result getmodellistbycategory(int page,int pagesize,int  categoryId){
        objRetModels objRetMs= new  objRetModels();
        try{
            page = (page ==0)?1:page;
            pagesize = (pagesize ==0)?20:pagesize;
            List<IIIModel> dbModels = Ebean.find(IIIModel.class).where().gt("member_id",0).setFirstRow((page-1)*pagesize).setMaxRows(pagesize).findList();
            objRetMs.retCode = 0;
            objRetMs.retMessage = "get model list success";
            objRetMs.models = dbModels;
        }catch(Exception ee){
            objRetMs.retCode = 1;
            objRetMs.retMessage = ee.toString();
        }
        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetMs)).as("application/json");

    }

    public Result updatemodel(){
        objRetWS objWS;
        Ebean.beginTransaction();
        try{

            if (request().getHeader("model_id") == null     || request().getHeader("model_id").isEmpty()   ||
                request().getHeader("member_token") == null || request().getHeader("member_token").isEmpty()){
                throw new parameterMissingException("input parameter missing");
            }

            Long model_id = Long.valueOf(request().getHeader("model_id"));
            // get model by model_id
            IIIModel dbModel = IIIModel.find.byId(model_id);
           System.out.println("1234567");
            if (dbModel == null){
                throw new noData("can not find available model");
            }

            // get member_id by member token
            Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            // double check model creator
            if (dbModel.member.memberID != member_id.longValue()){
                throw new notCreator("not model creator");
            }

            JsonNode objJson = request().body().asJson();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "modelName":
                        dbModel.modelName = objJson.get(key).asText();
                        break;
                    case "category":
                        Integer categoryID = objJson.get(key).asInt();
                        IIIModelCategory dbCategory = IIIModelCategory.find.byId(categoryID);
                        if (dbCategory == null){
                            throw new noData("can not find category");
                        }
                        dbModel.modelcategory = dbCategory;
                        break;
                    case "license":
                        Integer licenseID = objJson.get(key).asInt();
                        IIIModelLicense dbLicense = IIIModelLicense.find.byId(licenseID);
                        if (dbLicense == null){
                            throw new noData("can not find license");
                        }
                        dbModel.modellicense = dbLicense;
                        break;
                    case "description":
                        dbModel.description = objJson.get(key).asText();
                        break;
                    case "scenario":
                        dbModel.scenario = objJson.get(key).asText();
                        break;
                    case "instruction":
                        dbModel.instruction = objJson.get(key).asText();
                        break;
                    case "stl":
                 //   case "STL":
                //    case "obj":
                //    case "OBJ":
                //    case "pdf":
                //    case "PDF":
                //    case "mtl":
                        String[] stls = objJson.get(key).asText().split(";");
                        System.out.println("*********");
                        System.out.println(key);
                        System.out.println(objJson.get(key).asText());
                        if (stls.length >0) {
                            // remove old attachment relationship
                         //   Ebean.createSqlUpdate("update iiimodel_attachment set model_id = null WHERE model_id = "+model_id+"").execute();
                            Ebean.createSqlUpdate("update iiimodel_attachment set status = 0 WHERE model_id = "+model_id+"").execute();
                            for (String strStl : stls) {
                                IIIModelAttachment dbMA = IIIModelAttachment.find.byId(strStl);
                                if (dbMA == null)
                                    throw new noData("can not find attachemnt");
                                dbMA.status =1 ;
                                dbMA.iiimodel = dbModel;
                                dbModel.modelAttachments.add(dbMA);
                            }
                        }

                        break;
                    case "image":
                        String[] images = objJson.get(key).asText().split(";");
                        if (images.length >0){
                            // remove old thumbnail relationship
                     //       Ebean.createSqlUpdate("update iiimodel_thumbnail set model_id = null WHERE model_id = "+model_id+"").execute();
                            Ebean.createSqlUpdate("update iiimodel_thumbnail set status = 0 WHERE model_id = "+model_id+"").execute();
                            for (String strImage : images) {
                                IIIModelThumbnail dbMT = IIIModelThumbnail.find.byId(strImage);
                                if (dbMT == null)
                                    throw new noData("can not find thumbnail");
                                dbMT.status =1 ;
                                dbMT.iiimodel = dbModel;
                                dbModel.modelThumbnails.add(dbMT);
                            }
                        }

                        break;
//                    case "modelTags":
//                        // remove old tag relationship
//                        dbModel.modelTags.clear();;
//                        // create new tag relationship
//                        if (objJson.get(key).isArray()){
//                            for (JsonNode objTag : objJson.get(key)) {
//                                IIIModelTag dbMT = new IIIModelTag();
//                                dbMT.iiimodel = dbModel;
//                                dbMT.tagName =  objTag.get("tagName").asText();
//                                dbModel.modelTags.add(dbMT);
//                            }
//                        }
//                        break;
                }
            }

            Ebean.save(dbModel);

            Ebean.commitTransaction();
            if (objJson.has("modelTags") && objJson.get("modelTags").asInt() != 0){
                String[] campaigns = objJson.get("modelTags").asText().split(";");
                if (campaigns.length > 0){
                    for(String campaignID : campaigns){
                        Campaign dbC =  Campaign.find.byId(Long.valueOf(campaignID));
                        if (dbC == null) throw new noData("there is no campaign data");

                        IIIModel dbM = IIIModel.find.byId(model_id);
                        if (dbC == null) throw new noData("there is no model data");

                        dbC.entitiesB.add(dbM);

                        Ebean.save(dbC);
                        objWS = new objRetWS(0,"assign model to campagin success");
                    }
                }
            }
            objWS = new objRetWS(0,"model update success");
        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch (notCreator e){
            objWS = new objRetWS(GalleryWS.class,"05001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result newmodelcomment(){
        objRetWS objWS;
        Ebean.beginTransaction();
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("member_token")       || objJson.get("member_token").asText().isEmpty()    ||
                !objJson.has("message")            || objJson.get("message").asText().isEmpty()         ||
                request().getHeader("model_id") == null     || request().getHeader("model_id").isEmpty()    ){
                throw new parameterMissingException("input parameter missing");
            }

            Long model_id = Long.valueOf(request().getHeader("model_id"));

            // get model by model_id
            IIIModel dbModel = IIIModel.find.byId(model_id);
            if (dbModel == null){
                throw new noData("can not find available model");
            }

            // get member_id by member token
            Long member_id = convertMemberTokenToMemberID(objJson.get("member_token").asText());
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            Member dbMember = Member.find.byId(member_id);
            if (dbMember == null){
                throw new noData("can not find available member");
            }

            IIIModelComment dbMC = new IIIModelComment();
            dbMC.memberID = dbMember.memberID;
            dbMC.memberNickname = dbMember.nickName;
            dbMC.iiimodel = dbModel;
            dbMC.create_time = new Date();


            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "message":
                        dbMC.message = objJson.get(key).asText();
                        break;
                    case "parent_id":
                        dbMC.parentID = objJson.get(key).asLong();
                        break;
                }
            }

            Ebean.save(dbMC);

            Ebean.commitTransaction();

            objWS = new objRetWS(0,dbMC.memberNickname + ","+dbMC.message);

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result downloadclick(){
        objRetWS objWS;
        try{
            // check input parameter
            if (request().getHeader("model_id") == null ||
                request().getHeader("model_id").isEmpty() ||
                Long.valueOf(request().getHeader("model_id")) ==0){
                throw new parameterMissingException("input parameter missing");
            }

            // get model by model_id
            Long model_id = Long.valueOf(request().getHeader("model_id"));
            IIIModel dbModel = IIIModel.find.byId(model_id);

            if (dbModel == null){
                throw new noData("can not find available model");
            }else {
                // update model download time value
                dbModel = modelclick(1,dbModel,null);
                Long member_id = 0L;
                if (request().getHeader("member_token") != null &&
                    !request().getHeader("member_token").isEmpty()){
                    member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                }
                // update model interaction
                modelclickwithmember(1,dbModel,member_id,0);

                objWS = new objRetWS(0,String.valueOf(dbModel.download));

            }

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result likeclick(){
        objRetWS objWS;
        try{
            // check input parameter
            if (request().getHeader("model_id") == null || request().getHeader("model_id").isEmpty() || Long.valueOf(request().getHeader("model_id")) ==0 ||
                request().getHeader("member_token") == null || request().getHeader("member_token").isEmpty() ){
                throw new parameterMissingException("input parameter missing");
            }

            // get model by model_id
            Long model_id = Long.valueOf(request().getHeader("model_id"));
            IIIModel dbModel = IIIModel.find.byId(model_id);

            if (dbModel == null){
                throw new noData("can not find available model");
            }else {

                Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                if (member_id == 0){
                    throw new invalidMemberToken("invalid member token");
                }
                // update model interaction
                IIIModelMemberAction dbMMA = modelclickwithmember(5, dbModel, member_id, 0);
                dbModel = modelclick(5,dbModel,dbMMA);

                objWS = new  objRetWS(0,String.valueOf(dbModel.likeCount));

            }

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch(Exception ee){
            objWS = new  objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result scoreclick(int score){
        objRetWS objWS;
        try{
            // check input parameter
            if (request().getHeader("model_id") == null ||
                    request().getHeader("model_id").isEmpty() ||
                    request().getHeader("member_token") == null ||
                    request().getHeader("member_token").isEmpty() ||
                    Long.valueOf(request().getHeader("model_id")) ==0){
                throw new parameterMissingException("input parameter missing");
            }

            // get model by model_id
            Long model_id = Long.valueOf(request().getHeader("model_id"));
            IIIModel dbModel = IIIModel.find.byId(model_id);

            if (dbModel == null){
                throw new noData("can not find available model");
            }else {

                Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                if (member_id == 0){
                    throw new invalidMemberToken("invalid member token");
                }
                // update model interaction
                modelclickwithmember(2,dbModel,member_id,score);
                dbModel = modelclick(2,dbModel,null);

                objWS = new  objRetWS(0,String.valueOf(dbModel.averageScore));

            }

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch(Exception ee){
            objWS = new  objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result shareclick(){
        objRetWS objWS;
        try{
            // check input parameter
            if (request().getHeader("model_id") == null ||
                    request().getHeader("model_id").isEmpty() ||
                    request().getHeader("member_token") == null ||
                    request().getHeader("member_token").isEmpty() ||
                    Long.valueOf(request().getHeader("model_id")) ==0){
                throw new parameterMissingException("input parameter missing");
            }

            // get model by model_id
            Long model_id = Long.valueOf(request().getHeader("model_id"));
            IIIModel dbModel = IIIModel.find.byId(model_id);

            if (dbModel == null){
                throw new noData("can not find available model");
            }else {

                Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                if (member_id == 0){
                    throw new invalidMemberToken("invalid member token");
                }
                // update model interaction
                modelclickwithmember(3,dbModel,member_id,0);
                objWS = new  objRetWS(0,"share success");
            }

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch(Exception ee){
            objWS = new  objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result collectclick(){
        objRetWS objWS;
        try{
            // check input parameter
            if (request().getHeader("model_id") == null     || request().getHeader("model_id").isEmpty()    || Long.valueOf(request().getHeader("model_id")) ==0 ||
                request().getHeader("member_token") == null || request().getHeader("member_token").isEmpty()){
                throw new parameterMissingException("input parameter missing");
            }

            // get model by model_id
            Long model_id = Long.valueOf(request().getHeader("model_id"));
            IIIModel dbModel = IIIModel.find.byId(model_id);

            if (dbModel == null){
                throw new noData("can not find available model");
            }else {

                Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
                if (member_id == 0){
                    throw new invalidMemberToken("invalid member token");
                }
                // update model interaction
                IIIModelMemberAction dbMMA = modelclickwithmember(4, dbModel, member_id, 0);
                dbModel = modelclick(4,dbModel,dbMMA);
                objWS = new  objRetWS(0,String.valueOf(dbMMA.isCollect) +","+String.valueOf(dbModel.collectCount));

            }

        }catch(parameterMissingException e){
            objWS = new objRetWS(GalleryWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(GalleryWS.class,"03001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(GalleryWS.class,"04001",e.toString());
        }catch(Exception ee){
            objWS = new  objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result mycollectlist(int page, int pagesize, String sort){
        objRetModels objRetMs;
        try{
            page = (page ==0)?1:page;
            pagesize = (pagesize ==0)?20:pagesize;

            if (sort == null || sort.isEmpty() ||
                    request().getHeader("member_token") == null ||
                    request().getHeader("member_token").isEmpty()){
                throw new parameterMissingException("input parameter missing");
            }
            Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            String sql = "select model_id from iiimodel_member_action where member_id =:memberID and [sort] = 1";
            List<SqlRow> sqlRows =  Ebean.createSqlQuery(sql.replace("[sort]", sort)).setParameter("memberID", member_id).setFirstRow((page-1)*pagesize).setMaxRows(pagesize).findList();
            List<Long> modelList = new ArrayList<Long>();
            for(SqlRow row : sqlRows) {
                modelList.add(row.getLong("model_id"));
            }
            List<IIIModel> dbModels = Ebean.find(IIIModel.class).where().in("model_id",modelList).findList();
            objRetMs = new objRetModels();
            objRetMs.retCode = 0;
            objRetMs.retMessage = "get my collection list success";
            objRetMs.models = dbModels;
        }catch(parameterMissingException e){
            objRetMs = new objRetModels(GalleryWS.class,"02001",e.toString());
        }catch (invalidMemberToken e){
            objRetMs = new objRetModels(GalleryWS.class,"04001",e.toString());
        }catch(Exception ee){
            objRetMs = new objRetModels(1,ee.toString());
        }
        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetMs)).as("application/json");

    }

    private IIIModel modelclick(int type,IIIModel dbModel,IIIModelMemberAction dbMMA) throws  Exception{
        switch (type){
            // view
            case 0:
                dbModel.viewedTimes = dbModel.viewedTimes +1;
                break;
            // download
            case 1:
                dbModel.download = dbModel.download +1;
                break;
            // score
            case 2:
                List<SqlRow> sqlRows =Ebean.createSqlQuery("SELECT SUM(i.rating)/COUNT(i.member_id) as avg from iiimodel_member_action i where i.model_id =:modelID").setParameter("modelID",dbModel.modelID).findList();
                SqlRow row = sqlRows.get(0);
                Float avg = row.getFloat("avg");
                dbModel.averageScore = avg;

                break;
            // share,
            case 3:
                break;
            // collection ,
            case 4:

                dbModel.collectCount =(dbMMA.isCollect)?dbModel.collectCount+1:dbModel.collectCount-1;
                break;
            // like ,
            case 5:
                dbModel.likeCount = (dbMMA.isLiked)?dbModel.likeCount+1:dbModel.likeCount-1;
                break;
        }

        Ebean.save(dbModel);
        return dbModel;
    }

    private IIIModelMemberAction modelclickwithmember(int type,IIIModel dbModel,Long member_id,int rating) throws Exception{
        if (member_id == 0){
            return null;
        }
        IIIModelMemberActionPK pk = new IIIModelMemberActionPK();
        pk.memberID = member_id;
        pk.modelID = dbModel.modelID;
        IIIModelMemberAction dbMMA = IIIModelMemberAction.find.byId(pk);
        if (dbMMA == null){
            dbMMA = new IIIModelMemberAction();
            dbMMA.downloadTimes = 0;
            dbMMA.rating = 0f;
            dbMMA.sharedTimes = 0;
            dbMMA.viewedTimes = 0;
            dbMMA.isCollect = false;
            dbMMA.isLiked = false;
            dbMMA.isDownload = false;
            dbMMA.pk = pk;
        }
        switch (type){
            // view
            case 0:
                dbMMA.viewedTimes = dbMMA.viewedTimes +1;
                break;
            // download
            case 1:
                dbMMA.downloadTimes = dbMMA.downloadTimes +1;
                dbMMA.isDownload = true;
                break;
            // score
            case 2:
                dbMMA.rating = rating;
                break;
            // sahre
            case 3:
                dbMMA.sharedTimes = dbMMA.sharedTimes +1;
                break;
            // collection
            case 4:
                dbMMA.isCollect = !dbMMA.isCollect;
                break;
            // like
            case 5:
                dbMMA.isLiked = !dbMMA.isLiked;
                break;
        }

        Ebean.save(dbMMA);
        return dbMMA;
    }
}
