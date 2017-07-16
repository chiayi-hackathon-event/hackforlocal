package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.hackforlocal.*;


//新增
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



public class  hackforlocal extends Controller {

	public Result index() {
		 return ok(index.render(""));
    }
	 public Result post() {

		List<ResetData> dbMT = ResetData.find.where().findList();


                 return ok(post.render("",dbMT));
    }
	 public Result about() {
                 return ok(about.render(""));
    }
}

