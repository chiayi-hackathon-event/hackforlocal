package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http;
import play.mvc.Http.Session;
import play.data.DynamicForm;
import play.data.Form;

import views.html.*;
//置入才找得到view/adim/admin_index.scala.html//
import views.html.admin.*;

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

import models.*;

public class Admin extends Application {

	public Result admin_index(){
		
		if (getCookie(memberToken).length() == 0) return redirect("/Fast-Lab/sign-in");

		 	String Member_token = getCookie(memberToken);
			List<MemberToken> dbMT = MemberToken.find.where().eq("memberToken",Member_token).findList();
			Long MemberID = dbMT.get(0).member.memberID;
			List<Admin_permit> record  = Admin_permit.find.where().eq("member_id",MemberID).findList();
			//List<Admin_permit> admin = Admin_permit.find.all();		
		
			VerifyToken();
		
			Session session = Http.Context.current().session();
			session.put("memberid",String.valueOf(record.get(0).memberID));

  		return ok(admin_index.render("Your new application is ready.",record));
    }


}

