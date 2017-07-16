package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import iiiException.duplicateData;
import iiiException.noData;
import iiiException.parameterMissingException;
import library.WSAuthenticator;
import models.Country;
import models.IIIModelCategory;
import objects.objRetCategory;
import objects.objRetCountry;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

import java.util.Iterator;
import java.util.List;

/**
 * Created by abow on 15/11/8.
 */
@Security.Authenticated(WSAuthenticator.class)
public class ModelCategoryWS extends Controller {

    public Result getCategoryList() {

        objRetCategory objRetC;
        try{
          //  List<IIIModelCategory> dbCategories = IIIModelCategory.find.findList();
            List<IIIModelCategory> dbCategories = IIIModelCategory.find.where().eq("status", 1).findList();
            if (dbCategories.isEmpty()){
                throw new noData("there is no model category data");
            }else{
                objRetC = new objRetCategory();
                objRetC.retCode = 0;
                objRetC.retMessage = "success";
                objRetC.categories = dbCategories;
            }
        }catch(noData e){
            objRetC = new objRetCategory(ModelCategoryWS.class,"03001",e.toString());
        }catch(Exception ee){
            objRetC = new objRetCategory(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetC)).as("application/json");
    }

    public Result newCategory() {
        objRetWS objWS;
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("categoryName") || objJson.get("categoryName").asText().isEmpty()){
                throw new parameterMissingException("parameter missing");
            }
            String categoryName = objJson.get("categoryName").asText();

            // check country is available
            List<IIIModelCategory> dbCategories = Ebean.find(IIIModelCategory.class).where().eq("categoryName",categoryName).findList();
            if (dbCategories.size() >0){
                throw new duplicateData("category is already exist");
            }

            IIIModelCategory dbCategory = new IIIModelCategory();
            dbCategory.categoryName = categoryName;
            Ebean.save(dbCategory);

            objWS = new objRetWS(0,String.valueOf(dbCategory.categoryID));

        }catch(parameterMissingException e){
            objWS = new objRetWS(ModelCategoryWS.class,"02001",e.toString());
        }catch(duplicateData e){
            objWS = new objRetWS(ModelCategoryWS.class,"03002",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result updateCategory(int categoryID) {
        objRetWS objWS;
        try{
            IIIModelCategory dbCategory = IIIModelCategory.find.byId(categoryID);
            if (dbCategory == null){
                throw new noData("there is no model category data");
            }

            JsonNode objJson = request().body().asJson();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "categoryName":
                        dbCategory.categoryName = objJson.get(key).asText();
                        break;
                }
            }

            Ebean.save(dbCategory);

            objWS = new objRetWS(0,"update model category success");

        }catch(noData e){
            objWS = new objRetWS(ModelCategoryWS.class,"03001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result delCategory(int categoryID) {
        objRetWS objWS;
        try{
            IIIModelCategory.find.byId(categoryID).delete();
            objWS = new objRetWS(0,"delete model category by category_id " + String.valueOf(categoryID));
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }
}
