package controllers;

import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;


//置入才找得到view/adim/...下的網頁//
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



public class Designer extends Controller {


//顯示designer功能
    public Result designerarea(){

        ArrayList<Map<Integer,String>> design = new ArrayList<Map<Integer,String>>();
        ArrayList<Map<Integer,Integer>> design_sum = new ArrayList<Map<Integer,Integer>>();
        Integer sn = null;
        Integer members =null;
        design = design_post();
        design_sum =designsum_post();
        sn =Integer.valueOf(design.size());
	members =membersum_post();
        System.out.println(members);
	return ok(designer.render(design,design_sum,sn,members));
        }
//designner處理流程

    public ArrayList<Map<Integer,String>> design_post(){

        Map<Integer, String> model_id = new HashMap<Integer, String>();
        Map<Integer, String> member_id = new HashMap<Integer, String>();
        Map<Integer, String> model_name = new HashMap<Integer, String>();
        Map<Integer, String> average_score = new HashMap<Integer, String>();
        Map<Integer, String> create_time = new HashMap<Integer, String>();
        Map<Integer, String> description = new HashMap<Integer, String>();
        Map<Integer, String> scenario = new HashMap<Integer, String>();
        Map<Integer, String> instruction = new HashMap<Integer, String>();
        Map<Integer, String> download = new HashMap<Integer, String>();
        Map<Integer, String> viewed_times = new HashMap<Integer, String>();
        Map<Integer, String> collect_count = new HashMap<Integer, String>();
        Map<Integer, String> like_count = new HashMap<Integer, String>();
        Map<Integer, String> category_id = new HashMap<Integer, String>();
        Map<Integer, String> license_id = new HashMap<Integer, String>();
	//作品不重複每個人總數
	Map<Integer, String> total = new HashMap<Integer, String>();
		
	ArrayList<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
        try{
            Connection conn = null;
            Statement stmt = null;
            conn = DB.getConnection();
            stmt = conn.createStatement();
            //String query = "select * from markerspace where lng= 23.9807747";
            String query = "SELECT DISTINCT `member_id`, count(`member_id`) as total FROM `iiimodel` GROUP BY `member_id` HAVING count('member_id')>0";
            String querynum = "SELECT count(*)  FROM `member` WHERE 1";
 
	    ResultSet rs = stmt.executeQuery(query);
            int i=0;
            int count =0;
            while (rs.next()){


            String member_id_s =rs.getString("member_id");
            String total_s =rs.getString("total");
         /* String model_name_s =rs.getString("model_name");
            String average_score_s =rs.getString("average_score");
            String create_time_s =rs.getString("create_time");
            String description_s =rs.getString("description");
            String scenario_s =rs.getString("scenario");
            String instruction_s =rs.getString("instruction");
            String download_s =rs.getString("download");
            String viewed_times_s =rs.getString("viewed_times");
            String collect_count_s =rs.getString("collect_count");
            String like_count_s =rs.getString("like_count");
            String category_id_s =rs.getString("category_id");
            String license_id_s =rs.getString("license_id");
	 */
         // model_id.put(i,model_id_s);
            member_id.put(i,member_id_s);
            total.put(i,total_s);
	 /* model_name.put(i,model_name_s);
            average_score.put(i,average_score_s);
            create_time.put(i,create_time_s);
            description.put(i,description_s);
            scenario.put(i,scenario_s);
            instruction.put(i,instruction_s);

            download.put(i,download_s);
            viewed_times.put(i,viewed_times_s);
            collect_count.put(i,collect_count_s);
            like_count.put(i,like_count_s);
            category_id.put(i,category_id_s);
            license_id.put(i,license_id_s);
	*/
            i++;

            }

                //list.add(model_id);
                list.add(member_id);
                list.add(total);
		//list.add(model_name);
                //list.add(average_score);
                //list.add(create_time);
                //list.add(description);
                //list.add(scenario);
                //list.add(instruction);
                //list.add(download);
                //list.add(viewed_times);
                //list.add(collect_count);
                //list.add(like_count);
                //list.add(category_id);
                //list.add(license_id);

                rs.close();
                stmt.close();
                conn.close();
                }
        catch(SQLException se){
                se.printStackTrace();
                                }
        return (list);
	}

	public ArrayList<Map<Integer,Integer>> designsum_post(){

        Map<Integer, Integer> member_id = new HashMap<Integer, Integer>();

        ArrayList<Map<Integer,Integer>> list = new ArrayList<Map<Integer,Integer>>();
        try{

            Connection conn = null;
            Statement stmt = null;
            conn = DB.getConnection();
            stmt = conn.createStatement();
            String query = "SELECT COUNT( DISTINCT `member_id` ) FROM `iiimodel`";
            ResultSet rs = stmt.executeQuery(query);
            int i=0;
            int count =0;
            //Integer average_score_s =0;
            //Integer download_s =0;
            //Integer viewed_times_s =0;
            //Integer collect_count_s =0;
            //Integer like_count_s =0;
            //Integer category_id_s =0;
            //Integer license_id_s =0;
            while (rs.next()){
                /*
                if((rs.getString("average_score"))==null)
                {
                Integer average_score_s =0;
                }
                else
                {*/
                Integer member_id_s =Integer.valueOf(rs.getString("COUNT( DISTINCT `member_id` )"));
		member_id.put(i,member_id_s);
                i++;
                }

                list.add(member_id);
                rs.close();
                stmt.close();
                conn.close();
                }
        catch(SQLException se){
                se.printStackTrace();
                              }



        return (list);
        }




	 public Integer membersum_post(){

	Integer members = null;
	Integer num= 0;	
	try{
            Connection conn = null;
            Statement stmt = null;
            conn = DB.getConnection();
            stmt = conn.createStatement();
            String query = "SELECT COUNT(*)  FROM `member` WHERE 1";
            ResultSet rs = stmt.executeQuery(query);
	    while (rs.next()){
            Integer member=Integer.valueOf(rs.getString("COUNT(*)"));
	    num=member;
            }
	    members=num; 
            rs.close();
            stmt.close();
            conn.close();
           }
        catch(SQLException se){
                se.printStackTrace();
                                }
        System.out.println(members);

	return (members);
	}
}
