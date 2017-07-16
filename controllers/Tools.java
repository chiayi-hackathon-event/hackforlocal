package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;


//置入才找得到view/adim/...下的.scala.html//
import views.html.admin.*;

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


public class Tools extends Controller {

//顯示rank功能
    public Result toolrank(){

        ArrayList<Map<Integer,String>> rank = new ArrayList<Map<Integer,String>>();
        //ArrayList<Map<Integer,Integer>> rank_sum = new ArrayList<Map<Integer,Integer>>();
        Integer sn = null;
        rank = ranklist_post();
        //rank_sum =ranksum_post();
        sn =Integer.valueOf(rank.size());
        return ok(tool.render(rank,sn));
        }

//rank處理流程

    public ArrayList<Map<Integer,String>> ranklist_post(){

        Map<Integer, String> tool_id = new HashMap<Integer, String>();
        Map<Integer, String> tool_name = new HashMap<Integer, String>();
        Map<Integer, String> download = new HashMap<Integer, String>();
        Map<Integer, String> viewed_times = new HashMap<Integer, String>();


	ArrayList<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
        try{
            Connection conn = null;
            Statement stmt = null;
            conn = DB.getConnection();
            stmt = conn.createStatement();
            //String query = "select * from markerspace where lng= 23.9807747";
            String query = "select * from tool";
            ResultSet rs = stmt.executeQuery(query);
            int i=0;
            int count =0;
            while (rs.next()){

            String tool_id_s =rs.getString("tool_id");
	    String tool_name_s =rs.getString("tool_name");
            String download_s =rs.getString("download");
            String viewed_times_s =rs.getString("viewed_times");

            tool_id.put(i,tool_id_s);
            tool_name.put(i,tool_name_s);
            download.put(i,download_s);
            viewed_times.put(i,viewed_times_s);
            i++;

            }

                list.add(tool_id);
                list.add(tool_name);


                list.add(download);
                list.add(viewed_times);

                rs.close();
                stmt.close();
                conn.close();
                }
        catch(SQLException se){
                se.printStackTrace();
                                }
        return (list);
	}

   public Result fblike() {
    return ok(fblike.render());
    }



}
