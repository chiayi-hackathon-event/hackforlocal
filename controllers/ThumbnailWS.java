package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import library.WSAuthenticator;
import models.IIIModelThumbnail;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Iterator;

/**
 * Created by Saber on 2015/11/23.
 */
@Security.Authenticated(WSAuthenticator.class)
public class ThumbnailWS extends UniTools{

    public Result newThumbnail(){
        objRetWS objWS = new  objRetWS();
        Ebean.beginTransaction();
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("thumbnailFilename")       || objJson.get("thumbnailFilename").asText().isEmpty()){
                objWS.retCode = 20102001;
                objWS.retMessage = "parameter missing";
                return ok(Json.toJson(objWS)).as("application/json");
            }

            IIIModelThumbnail dbModelThumbnail = new IIIModelThumbnail();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "thumbnailFilename":
                        dbModelThumbnail.thumbnailFilename = objJson.get(key).asText();
                        break;
                    case "thumbnailShowname":
                        dbModelThumbnail.thumbnailShowname = objJson.get(key).asText();
                        break;
                    case "thumbnailResize":
                        dbModelThumbnail.thumbnailResize = objJson.get(key).asText();
                        break;
                    case "isSystemCreated":
                        dbModelThumbnail.isSystemCreated = objJson.get(key).asBoolean();
                        break;
                }
            }
            Ebean.save(dbModelThumbnail);

            Ebean.commitTransaction();

            objWS.retCode = 0;
            objWS.retMessage = dbModelThumbnail.thumbnailFilename;

        }catch (Exception ee){
            objWS.retCode = 1;
            objWS.retMessage = ee.toString();
        }finally {
            Ebean.endTransaction();
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }
}
