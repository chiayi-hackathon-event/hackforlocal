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
import java.net.*;

import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.csrf.*;

//置入才找的到app/models/內的class
import models.*;

public class Sort extends Controller {


//首頁
//    public Result sort() {
//    return ok(sort.render());
//    }
//選單
    public Result menu() {
    return ok(sort_menu.render());
    }

//新增功能的處理流程
    public Result add(){

    List<Admin_permit> admin = Admin_permit.find.all();	
	
	
    return ok(sort_add.render(admin));
    }

    @RequireCSRFCheck	
    public Result add_op(){

	DynamicForm dynamicForm = Form.form().bindFromRequest();
//        Logger.info(dynamicForm.get("campaign_id"));
        Logger.info(dynamicForm.get("campaign_title"));
        Logger.info(dynamicForm.get("campaign_desc"));
        Logger.info(dynamicForm.get("scampaign_start_date"));
        Logger.info(dynamicForm.get("campaign_end_date"));
        Logger.info(dynamicForm.get("campaign_image"));
        Logger.info(dynamicForm.get("create_time"));
        //Logger.info(dynamicForm.get("member_id"));
		
	Session session = Http.Context.current().session();
	
	String memberid=session.get("memberid");
  //      session.put("1",dynamicForm.get("campaign_id"));
        session.put("1",dynamicForm.get("campaign_title"));
        session.put("2",dynamicForm.get("campaign_desc"));
        session.put("3",dynamicForm.get("campaign_start_date"));
        session.put("4",dynamicForm.get("campaign_end_date"));
        session.put("5",dynamicForm.get("campaign_image"));
        session.put("6",dynamicForm.get("create_time"));
        session.put("7",memberid);
	

	try{
        Connection conn = null;
        ResultSet rset = null;
        conn = DB.getConnection();
	String query = "INSERT INTO campaign (campaign_title,campaign_desc,campaign_start_date,campaign_end_date,campaign_image,create_time,member_id)   VALUES (?,?,?,?,?,?,?)";	
	
	
	PreparedStatement stmt = conn.prepareStatement(query);
	stmt.setString(1, session.get("1"));
        stmt.setString(2, session.get("2"));
        stmt.setString(3, session.get("3"));
        stmt.setString(4, session.get("4"));
        stmt.setString(5, session.get("5"));
        stmt.setString(6, session.get("6"));
        stmt.setString(7, session.get("7"));
//        stmt.setString(8, session.get("8"));
	stmt.executeUpdate();

	stmt.close();
        conn.close();
      }

	catch(SQLException se){
        se.printStackTrace();
        }
        return redirect("/admin/sort_list");
}


	public Result list(){

	ArrayList<Map<Integer,String>>  www = new ArrayList<Map<Integer,String>>();
	String s = null;
	String w =null;
        www = menu_post();
        w =  String.valueOf(www.size());

        return ok(sort_list.render(www,w));
        }


	public ArrayList<Map<Integer,String>> menu_post(){

        Map<Integer, String>id  = new HashMap<Integer, String>();
        Map<Integer, String>title  = new HashMap<Integer, String>();
        Map<Integer, String>desc  = new HashMap<Integer, String>();
        Map<Integer, String>start_date  = new HashMap<Integer, String>();
        Map<Integer, String>end_date  = new HashMap<Integer, String>();
        Map<Integer, String>image  = new HashMap<Integer, String>();
        Map<Integer, String>create_time  = new HashMap<Integer, String>();
        Map<Integer, String>member_id  = new HashMap<Integer, String>();
	
	//Session session = Http.Context.current().session();
       
	//System.out.println("test");
        //String Memberid = session.get("memberid");
	//System.out.println(Memberid);	
	ArrayList<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
	try{
		Connection conn = null;
    		Statement stmt = null;
    		conn = DB.getConnection();
    		stmt = conn.createStatement();

		Session session = Http.Context.current().session();
        	//檢查有沒有出現正確ID,Debug用
		System.out.println("test");
        	String Memberid = session.get("memberid");
        	System.out.println(Memberid);

		String query = "select * from campaign where member_id="+ Memberid;
    		ResultSet rs = stmt.executeQuery(query);
    		int i=0;

		while (rs.next()){

                String ids =rs.getString("campaign_id");
                String titles =rs.getString("campaign_title");
                String descs =rs.getString("campaign_desc");
                String start_dates =rs.getString("campaign_start_date");
                String end_dates =rs.getString("campaign_end_date");
                String images =rs.getString("campaign_image");
                String create_times =rs.getString("create_time");
                String member_ids =rs.getString("member_id");

                id.put(i,ids);
                title.put(i,titles);
                desc.put(i,descs);
                start_date.put(i,start_dates);
                end_date.put(i,end_dates);
                image.put(i,images);
                create_time.put(i,create_times);
                member_id.put(i,member_ids);

		


		i++;
		}


	list.add(id);
        list.add(title);
        list.add(desc);
        list.add(start_date);
        list.add(end_date);
        list.add(image);
        list.add(create_time);
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

//編輯功能中需要的選擇ID與資料庫比對後更新到前端

	public Result update(){

        DynamicForm dynamicForm = Form.form().bindFromRequest();
        Logger.info(dynamicForm.get("id"));
        Session session = Http.Context.current().session();
        session.put("check_id",dynamicForm.get("id"));
	
        return ok(update.render());

        }

	@RequireCSRFCheck
	public Result update_op()
        {

        DynamicForm dynamicForm = Form.form().bindFromRequest();
        Logger.info(dynamicForm.get("campaign_id"));
        Logger.info(dynamicForm.get("campaign_title"));
        Logger.info(dynamicForm.get("campaign_desc"));
        Logger.info(dynamicForm.get("scampaign_start_date"));
        Logger.info(dynamicForm.get("campaign_end_date"));
        Logger.info(dynamicForm.get("campaign_image"));
        Logger.info(dynamicForm.get("create_time"));
        Logger.info(dynamicForm.get("member_id"));

        Session session = Http.Context.current().session();
	int post_id =  Integer.valueOf(session.get("check_id"));

        session.put("1",dynamicForm.get("campaign_id"));
        session.put("2",dynamicForm.get("campaign_title"));
        session.put("3",dynamicForm.get("campaign_desc"));
        session.put("4",dynamicForm.get("campaign_start_date"));
        session.put("5",dynamicForm.get("campaign_end_date"));
        session.put("6",dynamicForm.get("campaign_image"));
        session.put("7",dynamicForm.get("create_time"));
        session.put("8",dynamicForm.get("member_id"));

	try{
        Connection conn = null;
        //Statement stmt = null;
        conn = DB.getConnection();
	String query = "update campaign set campaign_id=?, campaign_title=?, campaign_desc=?, campaign_start_date=?, campaign_end_date=?, campaign_image=?, create_time=?, member_id=?";

	PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(9, String.valueOf(post_id));
	
	stmt.setString(1, session.get("1"));
        stmt.setString(2, session.get("2"));
        stmt.setString(3, session.get("3"));
        stmt.setString(4, session.get("4"));
        stmt.setString(5, session.get("5"));
        stmt.setString(6, session.get("6"));
        stmt.setString(7, session.get("7"));
        stmt.setString(8, session.get("8"));

        stmt.executeUpdate();
        stmt.close();
        conn.close();
        }
        catch(SQLException se){
        se.printStackTrace();
         }
        return redirect("/admin/sort_list");
	}


	 public Result edit()
  {
        Session session = Http.Context.current().session();
        int id =  Integer.valueOf(session.get("check_id"));
        ArrayList<Map<Integer,String>>  www = new ArrayList<Map<Integer,String>>();
        String w =null;
        try{
	Connection conn = null;
    	Statement stmt = null;
    	conn = DB.getConnection();
    	stmt = conn.createStatement();
    	//String query = "select * from markerspace where lng= 23.9807747";
    	String query = "select * from campaign where campaign_id";
    	ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){

        int idset=Integer.valueOf(rs.getString("campaign_id"));
        System.out.println(idset);
        if(id==idset){
        System.out.println("中了"+id);
        int i=0;
        www =  edit_op(id);
        w =  String.valueOf(www.size());
		}
	}
      rs.close();
      stmt.close();
      conn.close();
        }

        catch(SQLException se){
        se.printStackTrace();
        }
        return ok(edit.render(www,w));
}


	//刪除功能(寫回DB)

	 public Result delete(){

	    DynamicForm dynamicForm = Form.form().bindFromRequest();
        Logger.info(dynamicForm.get("id"));
        Session session = Http.Context.current().session();
        //session.put("check_id",dynamicForm.get("id"));
        String id_row = dynamicForm.get("id");
        String[] id_split = id_row.split(",");
 
        for (int i=0;i<id_split.length;i++)
        {
        System.out.println("split後"+id_split[i]);
        session.put("check_id",id_split[i]);
        System.out.println("Session的check_id:"+session("check_id"));

        int id =  Integer.valueOf(session.get("check_id"));
        System.out.println(session);

	try{
        Connection conn = null;
        Statement stmt = null;
        conn = DB.getConnection();
        stmt = conn.createStatement();
        //String query = "select * from markerspace where lng= 23.9807747";
        String query = "select * from campaign where campaign_id";
        ResultSet rs = stmt.executeQuery(query);

	while (rs.next()){
                //System.out.println(id);
                int idset=Integer.valueOf(rs.getString("campaign_id"));
                System.out.println(idset);
                if(id==idset){
                System.out.println("中了"+id);
                delete_op(id);

                }
         }
	
	 rs.close();
              stmt.close();
              conn.close();
            }
        catch(SQLException se){
              se.printStackTrace();
              }
                }

//**到這**//
        return ok ("OK");

        }

	public void delete_op(int post_id){

        //String id = String.valueOf(id);
        System.out.println(post_id);
        try{
            Connection conn = null;
            //Statement stmt = null;
            conn = DB.getConnection();
            //stmt = conn.createStatement();
            //String query = "select * from markerspace where lng= 23.9807747";
            String query = "delete from campaign where campaign_id=" + post_id;
          //  String query = "delete from markerspace where id= ?";
            PreparedStatement stmt = conn.prepareStatement(query);
          //  stmt.setString(1, String.valueOf(post_id));
            System.out.println(query);

            stmt.executeUpdate();
           // stmt.executeQuery(query);
            stmt.close();
            conn.close();
           }
        catch(SQLException se){
        se.printStackTrace();
        }

        }

	//編輯功能(寫回DB)處理流程
	public  ArrayList<Map<Integer,String>> edit_op(int post_id){	

	System.out.println(post_id);
	
	Map<Integer, String>id  = new HashMap<Integer, String>();
        Map<Integer, String>title  = new HashMap<Integer, String>();
        Map<Integer, String>desc  = new HashMap<Integer, String>();
        Map<Integer, String>start_date  = new HashMap<Integer, String>();
        Map<Integer, String>end_date  = new HashMap<Integer, String>();
        Map<Integer, String>image  = new HashMap<Integer, String>();
        Map<Integer, String>create_time  = new HashMap<Integer, String>();
        Map<Integer, String>member_id  = new HashMap<Integer, String>();

	ArrayList<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();

	try{
    	Connection conn = null;
	   // Statement stmt = null;
	    conn = DB.getConnection();
	   // stmt = conn.createStatement();
	    //String query = "select * from markerspace where lng= 23.9807747";
	    String query = "select * from campaign where campaign_id= ?";
	    PreparedStatement stmt = conn.prepareStatement(query);
	    stmt.setString(1, String.valueOf(post_id));
	    ResultSet rs = stmt.executeQuery();

	int i=0;

	 while (rs.next()){

                String ids =rs.getString("campaign_id");
                String titles =rs.getString("campaign_title");
                String descs =rs.getString("campaign_desc");
                String start_dates =rs.getString("campaign_start_date");
                String end_dates =rs.getString("campaign_end_date");
                String images =rs.getString("campaign_image");
                String create_times =rs.getString("create_time");
                String member_ids =rs.getString("member_id");

                id.put(i,ids);
                title.put(i,titles);
                desc.put(i,descs);
                start_date.put(i,start_dates);
                end_date.put(i,end_dates);
                image.put(i,images);
                create_time.put(i,create_times);
                member_id.put(i,member_ids);


		i++;
		}

		
	list.add(id);
        list.add(title);
        list.add(desc);
        list.add(start_date);
        list.add(end_date);
        list.add(image);
        list.add(create_time);
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


}

