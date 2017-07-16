package controllers;

import com.avaje.ebean.Ebean;
import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.GsonBuilder;
import iiiException.duplicateData;
import iiiException.invalidMemberToken;
import iiiException.noData;
import iiiException.parameterMissingException;
import library.PBKDF2Lib;
import library.WSAuthenticator;
import models.*;
import objects.objRetDesigners;
import objects.objRetMember;
import objects.objRetWS;
import play.libs.Json;
import play.mvc.Result;
import play.mvc.Security;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;
import java.net.URLDecoder;
import java.net.URLEncoder;
import play.*;
import play.mvc.*;
import play.mvc.Http;
import play.mvc.Http.Session;
import play.data.DynamicForm;
import play.data.Form;

/**
 * Created by abow on 15/10/24.
 */

@Security.Authenticated(WSAuthenticator.class)
public class MemberWS extends UniTools {


    public Result getmember(){

        objRetMember objRetM;
        try{
            // get member_id by member token
            Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }
            // get member by member_id
            Member dbMember = Member.find.byId(member_id);
            // TODO can not solve @ManyToOne lazy issue, so we need print out the value
            dbMember.country.refresh();
            if (dbMember == null){
                throw new noData("can not find available member");
            }else{
                objRetM = new objRetMember();
                objRetM.retCode = 0;
                objRetM.retMessage = "success";
                objRetM.member = dbMember;
            }
        }catch(invalidMemberToken e){
            objRetM = new objRetMember(MemberWS.class,"04001",e.toString());
        }catch(noData e){
            objRetM = new objRetMember(MemberWS.class,"03001",e.toString());
        }catch(Exception ee){
            objRetM = new objRetMember(1,ee.toString());

        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRetM)).as("application/json");
    }

    public Result memberregister(){
        objRetWS objWS;
        try{
            // check the necessary input parameter
            JsonNode objJson = request().body().asJson();
            if (!objJson.has("email")       || objJson.get("email").asText().isEmpty()      ||
                !objJson.has("password")    || objJson.get("password").asText().isEmpty()   ||
                !objJson.has("nick_name")   || objJson.get("nick_name").asText().isEmpty()  ||
                !objJson.has("country_id")  || objJson.get("country_id").asInt() ==0){
                throw new parameterMissingException("parameter missing");
            }

            String email = objJson.get("email").asText();
            String password = objJson.get("password").asText();
            String nickName = objJson.get("nick_name").asText();
            Integer country_id = objJson.get("country_id").asInt();
            String ip = objJson.get("ip").asText();

            // double check email is available
            // TODO try to solve service call service issue
            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).findList();
            if (dbMembers.size() >0){
                throw new duplicateData("email is already in use");
            }

            Country dbCountry = Country.find.byId(country_id);
            if (dbCountry == null){
                throw new noData("can not find country");
            }

            Member dbMember = new Member();
            dbMember.memberID =generateMemberID(nickName);
            dbMember.email = email;
            PBKDF2Lib pbkdf2 = new PBKDF2Lib();
            dbMember.password = pbkdf2.createPassword(password);
            String password_check = null;
	    password_check = dbMember.password;
	    Logger.info((password_check));
	    dbMember.nickName = nickName;
            dbMember.create_time = new Date();
            dbMember.subscribe = true;
            dbMember.gender = 0;
            dbMember.active = true;
//            dbMember.active = false;
            dbMember.country = dbCountry;
            dbMember.ip = ip;
//            MemberShip dbMS = new MemberShip();
//            dbMS.memberIdentifier = 0;
//            dbMS.identifierExplain = "common member";
//            dbMS.membershipActive = true;
//            dbMS.member = dbMember;
//            dbMember.memberShips.add(dbMS);
            Ebean.save(dbMember);

            objWS = new objRetWS(0,dbMember.memberID.toString());

        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (duplicateData e){
            objWS = new objRetWS(MemberWS.class,"03002",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result updatemember(){
        objRetWS objWS;
        try{
            // get member_id by member token
            Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            Member dbMember = Member.find.byId(member_id);
            if (dbMember == null){
                throw new noData("can not find available member");
            }

            JsonNode objJson = request().body().asJson();
            Iterator<String> keys =objJson.fieldNames();
            while( keys.hasNext() ) {
                String key = keys.next();
                switch (key){
                    case "first_name":
                        dbMember.firstName = objJson.get(key).asText();
                        break;
                    case "last_name":
                        dbMember.lastName = objJson.get(key).asText();
                        break;
                    case "email":
                        dbMember.email = objJson.get(key).asText();
                        break;
                    case "nick_name":
                        dbMember.nickName = objJson.get(key).asText();
                        break;
                    case "birthday":
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                        dbMember.brithday = sdf.parse(objJson.get(key).asText());
                        break;
                    case "gender":
                        dbMember.gender = objJson.get(key).asInt();
                        break;
                    case "subscribe":
                        dbMember.subscribe = objJson.get(key).asBoolean();
                        break;

//                    case "country_id":
//                        Country dbCountry = Country.find.byId(objJson.get(key).asInt());
//                        if (dbCountry == null){
//                            throw new noData("can not find country");
//                        }
//                        dbMember.country = dbCountry;
//                        break;
                    case "member_contacts":
                        dbMember.memberContacts.clear();
                        if (objJson.get(key).isArray()){
                            for (JsonNode objContact : objJson.get(key)) {
                                MemberContact dbMC = new MemberContact();
                                dbMC.contactType = objContact.get("contact_type").asInt();
                                dbMC.contactValue = objContact.get("contact_value").asText();
                                dbMember.memberContacts.add(dbMC);
                            }
                        }
                        break;
                    case "banner_image_name":
                        dbMember.bannerImageName = objJson.get(key).asText();
//                        String[] imageName = objJson.get(key).asText().split(";");
                        break;
                    case "description":
                        dbMember.description = objJson.get(key).asText();
                        break;
                }
            }

            Ebean.save(dbMember);

            objWS = new objRetWS(0,"update member success");
        }catch(invalidMemberToken e){
            objWS = new objRetWS(MemberWS.class,"04001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch (Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }


    public Result deletemember(){
        objRetWS objWS;
        try{
            // check the necessary input parameter
            String member_token = request().getHeader("member_token");
            String password = request().getHeader("password");
            if (member_token == null    || member_token.isEmpty() ||
                password == null        || password.isEmpty()){
                throw new parameterMissingException("parameter missing");

            }

            // get member_id by member token
            Long member_id = convertMemberTokenToMemberID(request().getHeader("member_token"));
            if (member_id == 0){
                throw new invalidMemberToken("invalid member token");
            }

            Member dbMember = Member.find.byId(member_id);
            if (dbMember == null){
                throw new noData("can not find available member");
            }else{
                PBKDF2Lib pbkdf2 = new PBKDF2Lib();
                if (pbkdf2.isValidPassword(request().getHeader("password"),dbMember.password)){
                    Ebean.delete(dbMember);
                    objWS = new objRetWS(0,"delete member success");
                }else{
                    objWS = new objRetWS(20104002,"password incorrect");
                }
            }
        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (invalidMemberToken e){
            objWS = new objRetWS(MemberWS.class,"04001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }

        return ok(Json.toJson(objWS)).as("application/json");
    }


    public Result logincheck(){

        objRetWS objWS;
        try{
            // check the necessary input parameter
            String email = request().getHeader("email");
            String password = request().getHeader("password");

            if (email == null       || email.isEmpty() ||
                password == null    || password.isEmpty()){
                throw new parameterMissingException("parameter missing");
            }

            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).eq("active",1).findList();

            if (dbMembers.size() == 0){
                throw new noData("can not find available member");
            }else if(dbMembers.size() >1){
                throw new duplicateData("email is used in multiple available account");
            }else{
                Member dbMmember = dbMembers.get(0);
                // check member active
                PBKDF2Lib pbkdf2 = new PBKDF2Lib();
                if (pbkdf2.isValidPassword(password,dbMmember.password)){
                    objWS = new objRetWS(0,generateMemberToken(dbMmember));

                }else{
                    objWS = new objRetWS(20104002,"password incorrect");
                }
            }

        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (duplicateData e){
            objWS = new objRetWS(MemberWS.class,"03002",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }


        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result logincheckFB(){

        objRetWS objWS;
        try{
            // check the necessary input parameter
            String FBID = request().getHeader("FBID");
            String FBAccessToken = request().getHeader("FBAccessToken");
            String countryCode = request().getHeader("country_id").split("_")[1];

            if (FBID == null       || FBID.isEmpty() ||
                    FBAccessToken == null    || FBAccessToken.isEmpty()){
                throw new parameterMissingException("parameter missing");
            }

            List<Member> dbMembers = Ebean.find(Member.class).where().eq("fbID",FBID).eq("active",1).findList();

            if (dbMembers.size() == 0){

                List<Country> dbCountrys = Ebean.find(Country.class).where().eq("countryIsoAlpha2",countryCode).findList();
                if (dbCountrys == null || dbCountrys.isEmpty()){
                    throw new noData("can not find country");
                }
                String FBName = request().getHeader("NAME");
                System.out.println("****" +URLDecoder.decode(FBName,"UTF8") );
                String ip = request().getHeader("ip");
                Member dbMember = new Member();
                dbMember.memberID =generateMemberID(FBName);
                dbMember.email = request().getHeader("EMAIL");
                dbMember.nickName = URLDecoder.decode(FBName,"UTF8");
                dbMember.create_time = new Date();
                dbMember.subscribe = true;
                dbMember.gender = 0;
                dbMember.active = true;
                dbMember.country = dbCountrys.get(0);
                dbMember.fbID = FBID;
                dbMember.ip = ip;
                Ebean.save(dbMember);

                objWS = new objRetWS(0,assignFBAccessToken2MemberToken(dbMember, FBAccessToken));
            }else if(dbMembers.size() >1){
                throw new duplicateData("FBID is used in multiple available account");
            }else{
                Member dbMember = dbMembers.get(0);
                // update Token
                objWS = new objRetWS(0,assignFBAccessToken2MemberToken(dbMember, FBAccessToken));
            }

        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (duplicateData e){
            objWS = new objRetWS(MemberWS.class,"03002",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }


        return ok(Json.toJson(objWS)).as("application/json");
    }


    public Result checkmail(String email){

        objRetWS objWS;
        try{
            // check the necessary input parameter
            if (email == null || email.isEmpty() ){
                throw new parameterMissingException("parameter missing");
            }

            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).findList();
            if (dbMembers.size() >0){
                throw new duplicateData("email is already in use");
            }else{
                objWS = new objRetWS(0,"email is available");
            }
        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (duplicateData e){
            objWS = new objRetWS(MemberWS.class,"03002",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }

        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result checkactive(Long member_id){

        objRetWS objWS;
        try{
            // check the necessary input parameter
            if (member_id ==0 ){
                throw new parameterMissingException("parameter missing");
            }

            Member dbMember = Member.find.byId(member_id);
            // can not find member by id
            if (dbMember == null){
                throw new noData("can not find available member");
            }else{
                // check member active
                if (!dbMember.active){
                    objWS = new objRetWS(20104003,"member is inactive");
                }else{
                    objWS = new objRetWS(20104004,"member is active");
                }
            }
        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result setactive(Long member_id){

        objRetWS objWS;
        try{
            // check the necessary input parameter
            if (member_id ==0 ){
                throw new parameterMissingException("parameter missing");
            }

            Member dbMember = Member.find.byId(member_id);
            // can not find member by id
            if (dbMember == null){
                throw new noData("can not find available member");
            }else{
                dbMember.active = true;
                Ebean.save(dbMember);
                objWS = new objRetWS(0,"member ("+member_id+") active");
            }
        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result resetpassword(String email){
        objRetWS objWS;
        try{
            // check the necessary input parameter
            if (email.isEmpty() ){
                throw new parameterMissingException("parameter missing");
            }

            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).eq("active",1).findList();

            if (dbMembers.size() == 0){
                throw new noData("can not find available member");
            }else if(dbMembers.size() >1){
                throw new duplicateData("email is used in multiple available account");
            }else{
                String strToken = UUID.randomUUID().toString().replace("-","");

                sendMail sm = new sendMail();
                sm.sendResetPasswordRequest(email,strToken);

                ResetPassword dbRP = new ResetPassword();
                dbRP.email = email;
                dbRP.token = strToken;

                Ebean.save(dbRP);
                objWS = new objRetWS(0,"send confirm mail");

//                Member dbMember = dbMembers.get(0);
                // check member active
//                PBKDF2Lib pbkdf2 = new PBKDF2Lib();
//                String randomPassword = UUID.randomUUID().toString().replace("-","").substring(0,10);
//                String encryptPassword  = pbkdf2.createPassword(randomPassword);
//                dbMember.password = encryptPassword;
//                Ebean.save(dbMember);

//                objWS = new objRetWS(0,"send to user new password ("+randomPassword+")");
            }

        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch (duplicateData e){
            objWS = new objRetWS(MemberWS.class,"03002",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }
        return ok(Json.toJson(objWS)).as("application/json");
    }

    public Result changepassword(){
        objRetWS objWS = new  objRetWS();

        try{
            JsonNode objJson = request().body().asJson();

            if (!objJson.has("email")       || objJson.get("email").asText().isEmpty()          ||
                !objJson.has("password")    || objJson.get("password").asText().isEmpty()   ||
                !objJson.has("token")       || objJson.get("token").asText().isEmpty()){
                throw new parameterMissingException("parameter missing");
            }

            String email = objJson.get("email").asText();

            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).eq("active",1).findList();

            if (dbMembers.size() == 0){
                throw new noData("can not find available member");
            }else if(dbMembers.size() >1){
                throw new duplicateData("email is used in multiple available account");
            }else{
                Member dbMember = dbMembers.get(0);
                // check token available
                List<ResetPassword> dbRP = Ebean.find(ResetPassword.class).where().eq("email",email).eq("token", objJson.get("token").asText()).findList();
                if (dbRP.size() == 0)
                    throw new noData("not available change password token");
                Long tmp = (new Date().getTime() -  dbRP.get(0).startDateTime.getTime())/1000;
                if ((new Date().getTime() -  dbRP.get(0).startDateTime.getTime())/1000 > dbRP.get(0).timeInterval*60)
                    throw new noData("token expire");

                PBKDF2Lib pbkdf2 = new PBKDF2Lib();
                String encryptPassword  = pbkdf2.createPassword(objJson.get("password").asText());
                dbMember.password = encryptPassword;
                Ebean.save(dbMember);
                objWS = new objRetWS(0, "update password success");

            }

        }catch (parameterMissingException e){
            objWS = new objRetWS(MemberWS.class,"02001",e.toString());
        }catch (noData e){
            objWS = new objRetWS(MemberWS.class,"03001",e.toString());
        }catch (duplicateData e){
            objWS = new objRetWS(MemberWS.class,"03002",e.toString());
        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }

        return ok(Json.toJson(objWS)).as("application/json");
    }


    public Result listDesigners(){
        objRetDesigners objRD;

        try{
            List<Member> dbMembers = null;
            if (request().getHeader("member_id") != null &&
                    !request().getHeader("member_id").isEmpty()){
                dbMembers = Ebean.find(Member.class).where().eq("memberID",request().getHeader("member_id")).eq("active",1).findList();
            }else{
                dbMembers = Ebean.find(Member.class).where().eq("memberShips.memberIdentifier",1).eq("active",1).findList();
            }

            objRD = new objRetDesigners(0, "get designer list success");
            objRD.members = dbMembers;

        }catch(Exception ee){
            objRD = new objRetDesigners(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRD)).as("application/json");
    }

    public Result setMemberAsDesigner(String email){
        objRetDesigners objRD;

        try{
            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).eq("active",1).findList();

            if (dbMembers.size() == 0){
                throw new noData("can not find available member");
            }else if(dbMembers.size() >1){
                throw new duplicateData("email is used in multiple available account");
            }else{
                List<Member> dbDesigners = Ebean.find(Member.class).where().eq("memberShips.memberIdentifier",1).eq("email",email).findList();
                if (dbDesigners.size() == 0)
                {
                    MemberShip dbMS = new MemberShip();
                    dbMS.identifierExplain = "designer";
                    dbMS.membershipActive = true;
                    dbMS.member = dbMembers.get(0);
                    dbMS.memberIdentifier = 1;
                    Ebean.save(dbMS);
                }
                dbMembers = Ebean.find(Member.class).where().eq("memberShips.memberIdentifier",1).eq("active",1).findList();
                objRD = new objRetDesigners(0, "set member as designer success");
                objRD.members = dbMembers;
            }
        }catch (noData e){
            objRD = new objRetDesigners(MemberWS.class,"03001",e.toString());
        }catch (duplicateData e){
            objRD = new objRetDesigners(MemberWS.class,"03002",e.toString());
        }catch(Exception ee){
            objRD = new objRetDesigners(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRD)).as("application/json");
    }

    public Result removeDesignerIdentifier(String email){
        objRetDesigners objRD;

        try{
            List<Member> dbMembers = Ebean.find(Member.class).where().eq("email",email).eq("active",1).findList();

            if (dbMembers.size() == 0){
                throw new noData("can not find available member");
            }else if(dbMembers.size() >1){
                throw new duplicateData("email is used in multiple available account");
            }else{
                List<MemberShip> dbMSs = Ebean.find(MemberShip.class).where().eq("member",dbMembers.get(0)).eq("memberIdentifier",1).findList();
                for (MemberShip dbMS : dbMSs){
                    Ebean.delete(dbMS);
                }
                dbMembers = Ebean.find(Member.class).where().eq("memberShips.memberIdentifier",1).eq("active",1).findList();
                objRD = new objRetDesigners(0, "remove designer identifier success");
                objRD.members = dbMembers;
            }
        }catch (noData e){
            objRD = new objRetDesigners(MemberWS.class,"03001",e.toString());
        }catch (duplicateData e){
            objRD = new objRetDesigners(MemberWS.class,"03002",e.toString());
        }catch(Exception ee){
            objRD = new objRetDesigners(1,ee.toString());
        }

        return ok(new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create().toJson(objRD)).as("application/json");
    }
}
