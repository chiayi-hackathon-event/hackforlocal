package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import library.WSAuthenticator;
import models.IIIModelAttachment;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Iterator;

/**
 * Created by Saber on 2015/11/23.
 */
@Security.Authenticated(WSAuthenticator.class)
public class StlWS extends UniTools{

    public Result newStl(){
        objRetWS objWS = new  objRetWS();
        Ebean.beginTransaction();
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("fileName")       || objJson.get("fileName").asText().isEmpty()){
                objWS.retCode = 20102001;
                objWS.retMessage = "parameter missing";
                return ok(Json.toJson(objWS)).as("application/json");
            }

            IIIModelAttachment dbModelAttachment = new IIIModelAttachment();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "fileName":
                        dbModelAttachment.fileName = objJson.get(key).asText();
                        break;
                    case "fileShowname":
                        dbModelAttachment.fileShowname = objJson.get(key).asText();
                        break;
                    case "fileSize":
                        dbModelAttachment.fileSize = objJson.get(key).floatValue();
                        break;
                    case "fileSizeUnit":
                        dbModelAttachment.fileSizeUnit = objJson.get(key).asText();
                        break;
                    case "fileThumbnail":
                        dbModelAttachment.fileThumbnail = objJson.get(key).asText();
                        break;
                    case "fileType":
                        dbModelAttachment.fileType = objJson.get(key).asText();
                        break;
                }
            }
            Ebean.save(dbModelAttachment);

            Ebean.commitTransaction();

            objWS.retCode = 0;
            objWS.retMessage = dbModelAttachment.fileName;

        }catch (Exception ee){
            objWS.retCode = 1;
            objWS.retMessage = ee.toString();
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }
}
