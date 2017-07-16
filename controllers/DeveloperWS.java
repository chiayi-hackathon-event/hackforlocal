package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import iiiException.generateIDException;
import iiiException.invalidMemberToken;
import iiiException.noData;
import iiiException.parameterMissingException;
import models.DeveloperGroup;
import models.DeveloperGroupAuthorization;
import models.DeveloperRegister;
import models.Member;

import objects.objRetWS;
import play.libs.Json;
import play.mvc.Result;

import java.util.Date;

/**
 * Created by abow on 15/11/22.
 */
public class DeveloperWS extends UniTools{
    public Result developerRegister(){

        objRetWS objWS;
        try{
            if (request().getHeader("member_token") == null || request().getHeader("member_token").isEmpty()){
                throw new parameterMissingException("parameter missing");
            }

            JsonNode objJson = request().body().asJson();
            if (!objJson.has("group_id") || objJson.get("group_id").asText().isEmpty()){
                throw new parameterMissingException("parameter missing");
            }

            Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            Member dbMember = Member.find.byId(member_id);
            if (dbMember == null){
                throw new noData("can not find available member");
            }

            DeveloperGroup dbDG = DeveloperGroup.find.byId(objJson.get("group_id").asText());
            if (dbDG == null){
                throw new noData("invalid dev group id");
            }

            String strAccessKey = "";
            Boolean refreshAccessKey = true;
            int i = 0;
            do{
                i++;
                strAccessKey = generateAccessKey("");
                DeveloperRegister dbDR = DeveloperRegister.find.byId(strAccessKey);
                if (dbDR == null) refreshAccessKey = false;
            }while (refreshAccessKey && i <5);

            if (refreshAccessKey || strAccessKey.isEmpty()){
                throw new generateIDException("can not generate available dev access key, please try it later");
            }
            DeveloperRegister dbDR = new DeveloperRegister();
            dbDR.accessKey = strAccessKey;
            dbDR.devGroup = dbDG;
            dbDR.member = dbMember;
            dbDR.createTime = new Date();
            dbDR.isAdmin = false;
            dbDR.active = true;

            Ebean.save(dbDR);

            objWS = new objRetWS(0,dbDR.accessKey);

            objWS.retCode = 0;
            objWS.retMessage = dbDR.accessKey;


        }catch (parameterMissingException e){
            objWS = new objRetWS(DeveloperWS.class,"02001",e.toString());
        }catch(noData e){
            objWS = new objRetWS(DeveloperWS.class,"03001",e.toString());
        }catch(invalidMemberToken e){
            objWS = new objRetWS(DeveloperWS.class,"04001",e.toString());
        }catch(generateIDException e){
            objWS = new objRetWS(DeveloperWS.class,"06002",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }

        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result groupRegister(){

        objRetWS objWS = new  objRetWS();
        try{

            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("group_name") || objJson.get("group_name").asText().isEmpty() ||
                !objJson.has("resources")  || !objJson.get("resources").isArray() || !objJson.get("resources").elements().hasNext()){
                throw new parameterMissingException("parameter missing");
            }

            String strGroupID = "";
            Boolean refreshUUID = true;
            int i = 0;
            do{
                i++;
                strGroupID = generateGroupID();
                DeveloperGroup dbDG = DeveloperGroup.find.byId(strGroupID);
                if (dbDG == null) refreshUUID = false;
            }while (refreshUUID && i <5);

            if (refreshUUID || strGroupID.isEmpty()){
                throw new generateIDException("can not generate available dev group id, please try it later");
            }

            DeveloperGroup dbDG = new DeveloperGroup();
            dbDG.groupID = strGroupID;
            dbDG.groupName = objJson.get("group_name").asText();
            dbDG.createTime = new Date();

            for (JsonNode objContact : objJson.get("resources")) {
                DeveloperGroupAuthorization dbDGA = new DeveloperGroupAuthorization();
                dbDGA.resource = objContact.get("resource").asText();
                dbDG.devGroupAuthorizations.add(dbDGA);
            }

            Ebean.save(dbDG);

            objWS = new objRetWS(0,dbDG.groupID);

        }catch (parameterMissingException e){
            objWS = new objRetWS(DeveloperWS.class,"02001",e.toString());
        }catch(generateIDException e){
            objWS = new objRetWS(DeveloperWS.class,"06001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objWS)).as("application/json");
    }
}
