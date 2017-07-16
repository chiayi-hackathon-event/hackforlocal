package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import play.*;
import play.mvc.*;
import play.mvc.Http;
import play.mvc.Http.Session;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.F.Promise;

import views.html.*;
//置入才找得到view/adim/admin_index.scala.html//
import views.html.account.*;

import java.util.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.SortedMap;
import java.util.TreeMap;

import play.db.*;
import java.sql.*;
import java.io.*;
import play.api.libs.json.*;
import org.json.*;
import java.net.*;

import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.csrf.*;

import com.avaje.ebean.Ebean;

import forms.*;
import models.*;
import objects.*;

public class Account extends Application {

        public Result account_index(){

	if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");

                String Member_token = getCookie(memberToken);
                List<MemberToken> dbMT = MemberToken.find.where().eq("memberToken",Member_token).findList();
                Long MemberID = dbMT.get(0).member.memberID;
                List<Admin_permit> record  = Admin_permit.find.where().eq("member_id",MemberID).findList();
                //List<Admin_permit> admin = Admin_permit.find.all();

                VerifyToken();
		//Delete();	
                return ok(account_index.render("Your new application is ready.",record));


	}

	public static String Delete(){

        String Member_token = getCookie(memberToken);
        List<MemberToken> dbMT = MemberToken.find.where().eq("memberToken",Member_token).findList();
        Long MemberID = dbMT.get(0).member.memberID;
	
	List<Member> dbMembers = Member.find.where().eq("member_id",MemberID).findList();
	
	boolean active = dbMembers.get(0).active;
	System.out.println(active);
	if(active==true){
	Member dbMember = Member.find.byId(MemberID);
	if(dbMember == null){
	return("can not find available member");
	}else{
	dbMember.active = false;
	Ebean.save(dbMember);
	
	}
	}
	return ("");
	}

	public Result delete(){	

	Delete();		

	
	 return ok ("You already removing account");
	}


    public Result reinst(){

	return ok(reinst.render(""));
    }



	@RequireCSRFCheck
    public Result sendResetAccount(){

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
         //  return ok();
    }


}
