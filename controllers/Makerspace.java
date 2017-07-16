package controllers;

import play.*;
import play.mvc.*;
import play.mvc.Http;
import play.mvc.Http.Session;
import play.data.DynamicForm;
import play.data.Form;

import views.html.*;
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


//CSRFFilter token接收@RequireCSRFCheck使用  必須置入的fn
import play.GlobalSettings;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.csrf.*;

public class Makerspace extends Controller {

//首頁
    public Result new_main() {
    return ok(new_main.render());
    }
//選單
    public Result menu() {
    return ok(menu.render());
    }

	

//新增功能的處理流程
    public Result add(){
    return ok(add.render());
    }

    @RequireCSRFCheck
    public Result add_op(){
	DynamicForm dynamicForm = Form.form().bindFromRequest();
        Logger.info(dynamicForm.get("organization"));
        Logger.info(dynamicForm.get("type"));
        Logger.info(dynamicForm.get("name"));
        Logger.info(dynamicForm.get("address"));
        Logger.info(dynamicForm.get("space"));
        Logger.info(dynamicForm.get("device"));
        Logger.info(dynamicForm.get("sevice"));
        Logger.info(dynamicForm.get("classification"));
        Logger.info(dynamicForm.get("customer"));
        Logger.info(dynamicForm.get("open_time"));
        Logger.info(dynamicForm.get("cooperation"));
        Logger.info(dynamicForm.get("establish"));
        Logger.info(dynamicForm.get("founder"));
        Logger.info(dynamicForm.get("status"));
        Logger.info(dynamicForm.get("work_space"));
        Logger.info(dynamicForm.get("rent_price"));
        Logger.info(dynamicForm.get("rent_rate"));
        Logger.info(dynamicForm.get("age"));
        Logger.info(dynamicForm.get("education"));
        Logger.info(dynamicForm.get("department"));
        Logger.info(dynamicForm.get("Venture"));
        Logger.info(dynamicForm.get("lng"));
        Logger.info(dynamicForm.get("lat"));
       // Logger.info(dynamicForm.get("id"));
	
	Session session = Http.Context.current().session();

        session.put("1",dynamicForm.get("organization"));
        session.put("2",dynamicForm.get("type"));

        session.put("3",dynamicForm.get("name"));
        session.put("4",dynamicForm.get("address"));
        session.put("5",dynamicForm.get("space"));
        session.put("6",dynamicForm.get("device"));
        session.put("7",dynamicForm.get("sevice"));
        session.put("8",dynamicForm.get("classification"));
        session.put("9",dynamicForm.get("customer"));
        session.put("10",dynamicForm.get("open_time"));
        session.put("11",dynamicForm.get("cooperation"));
        session.put("12",dynamicForm.get("establish"));
        session.put("13",dynamicForm.get("founder"));
        session.put("14",dynamicForm.get("status"));
        session.put("15",dynamicForm.get("work_space"));
        session.put("16",dynamicForm.get("rent_price"));
        session.put("17",dynamicForm.get("rent_rate"));
        session.put("18",dynamicForm.get("age"));
        session.put("19",dynamicForm.get("education"));
        session.put("20",dynamicForm.get("department"));
        session.put("21",dynamicForm.get("Venture"));
        session.put("22",dynamicForm.get("lng"));
        session.put("23",dynamicForm.get("lat"));
        //session.put("24",dynamicForm.get("id"));
	
	
	try{
        Connection conn = null;
        ResultSet rset = null;
        conn = DB.getConnection();
        String query = "INSERT INTO markerspace (organization,type,name,address,space,device,sevice,classification,customer,open_time,cooperation,establish,founder,status,work_space,rent_price,rent_rate,age,education,department,Venture,lng,lat)   VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

	PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(1, session.get("1"));
        stmt.setString(2, session.get("2"));
        stmt.setString(3, session.get("3"));
        stmt.setString(4, session.get("4"));
        stmt.setString(5, session.get("5"));
        stmt.setString(6, session.get("6"));
        stmt.setString(7, session.get("7"));
        stmt.setString(8, session.get("8"));
        stmt.setString(9, session.get("9"));
        stmt.setString(10, session.get("10"));
        stmt.setString(11, session.get("11"));
        stmt.setString(12, session.get("12"));
        stmt.setString(13, session.get("13"));
        stmt.setString(14, session.get("14"));
        stmt.setString(15, session.get("15"));
        stmt.setString(16, session.get("16"));
	
        stmt.setString(17, session.get("17"));
        stmt.setString(18, session.get("18"));
        stmt.setString(19, session.get("19"));
        stmt.setString(20, session.get("20"));
        stmt.setString(21, session.get("21"));
        stmt.setString(22, session.get("22"));
        stmt.setString(23, session.get("23"));
       // stmt.setString(24, session.get("24"));
        stmt.executeUpdate();
//      stmt.setString(i,session.get(i));
//      stmt.executeUpdate(query);

	
        stmt.close();
        conn.close();
      }
 	catch(SQLException se){
      	se.printStackTrace();
      	}
	return redirect("/admin/spaceList");
}

	//顯示DB資料表功能
	public Result spaceList(){

	ArrayList<Map<Integer,String>>  www = new ArrayList<Map<Integer,String>>();
        String s = null;
        String w =null;
        www = menu_post();

        w =  String.valueOf(www.size());

        return ok(spaceList.render(www,w));
  	}

//展開DB資料表功能
    public Result view(){

    ArrayList<Map<Integer,String>>  www = new ArrayList<Map<Integer,String>>();
    String s = null;
    String w =null;
    www = menu_post();

    w =  String.valueOf(www.size());
   return ok(view.render(www,w));
  }

//展開DB資料表的處理流程
 public ArrayList<Map<Integer,String>> menu_post()
{

 Map<Integer, String> organization = new HashMap<Integer, String>();
 Map<Integer, String> type = new HashMap<Integer, String>();
 Map<Integer, String> name = new HashMap<Integer, String>();
 Map<Integer, String> address = new HashMap<Integer, String>();
 Map<Integer, String> space = new HashMap<Integer, String>();
 Map<Integer, String> device = new HashMap<Integer, String>();
 Map<Integer, String> sevice = new HashMap<Integer, String>();
 Map<Integer, String> classification = new HashMap<Integer, String>();
 Map<Integer, String> customer = new HashMap<Integer, String>();
 Map<Integer, String> open_time = new HashMap<Integer, String>();
 Map<Integer, String> cooperation = new HashMap<Integer, String>();
 Map<Integer, String> establish = new HashMap<Integer, String>();
 Map<Integer, String> founder = new HashMap<Integer, String>();
 Map<Integer, String> status = new HashMap<Integer, String>();
 Map<Integer, String> work_space = new HashMap<Integer, String>();
 Map<Integer, String> rent_price = new HashMap<Integer, String>();
 Map<Integer, String> rent_rate = new HashMap<Integer, String>();
 Map<Integer, String> age = new HashMap<Integer, String>();
 Map<Integer, String> education = new HashMap<Integer, String>();
 Map<Integer, String> department = new HashMap<Integer, String>();
 Map<Integer, String> Venture = new HashMap<Integer, String>();
 Map<Integer, String> lng = new HashMap<Integer, String>();
 Map<Integer, String> lat = new HashMap<Integer, String>();
 Map<Integer, String> id = new HashMap<Integer, String>();

 Map<Integer, Map> test = new HashMap<Integer, Map>();
  ArrayList<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
 try{
    Connection conn = null;
    Statement stmt = null;
    conn = DB.getConnection();
    stmt = conn.createStatement();
    //String query = "select * from markerspace where lng= 23.9807747";
      String query = "select * from markerspace";
    ResultSet rs = stmt.executeQuery(query);
    int i=0;

	    while (rs.next()){

    String organizations =rs.getString("organization");
    String types =rs.getString("type");
    String names =rs.getString("name");
    String addresss =rs.getString("address");
    String spaces =rs.getString("space");
    String devices =rs.getString("device");
    String sevices =rs.getString("sevice");
    String classifications =rs.getString("classification");
    String customers =rs.getString("customer");
    String open_times =rs.getString("open_time");
    String cooperations =rs.getString("cooperation");
    String establishs =rs.getString("establish");
    String founders =rs.getString("founder");
    String statuss =rs.getString("status");
    String work_spaces =rs.getString("work_space");
    String rent_prices =rs.getString("rent_price");
    String rent_rates =("rent_rate");
    String ages =rs.getString("age");
    String educations =rs.getString("education");
    String departments =rs.getString("department");
    String Ventures =rs.getString("Venture");
    String lngs =rs.getString("lng");
    String lats =rs.getString("lat");
    String ids =rs.getString("id");


	organization.put(i,organizations);
        type.put(i,types);
        name.put(i,names);
        address.put(i,addresss);
        space.put(i,spaces);
        device.put(i,devices);
        sevice.put(i,sevices);
        classification.put(i,classifications);
        customer.put(i,customers);
        open_time.put(i,open_times);
        cooperation.put(i,cooperations);
        establish.put(i,establishs);
        founder.put(i,founders);
        status.put(i,statuss);
        work_space.put(i,work_spaces);
        rent_price.put(i,rent_prices);
        rent_rate.put(i,rent_rates);
        age.put(i,ages);
        education.put(i,educations);
        department.put(i,departments);
        Venture.put(i,Ventures);
        lng.put(i,lngs);
        lat.put(i,lats);
        id.put(i,ids);


     i++;
    }


	     //for (Integer key : test.keySet()) {
     //list.add(s);
     //list.add(ages);
        list.add(organization);
        list.add(type);
        list.add(name);
        list.add(address);
        list.add(space);
        list.add(device);
        list.add(sevice);
        list.add(classification);
        list.add(customer);
        list.add(open_time);
        list.add(cooperation);
        list.add(establish);
        list.add(founder);
        list.add(status);
        list.add(work_space);
        list.add(rent_price);
        list.add(rent_rate);
        list.add(age);
        list.add(education);
        list.add(department);
        list.add(Venture);
        list.add(lng);
        list.add(lat);
        list.add(id);

	    // test.put(1,(gap));
   //  test.put(2,(name));
     //System.out.println(list);
    // };

    //System.out.println(z);
 //       for (Integer key : gap.keySet()) {/

 //          test.put(key,(gap.get(key)));}
   //Logger.info(String.valueOf(www.size()));
   // Logger.info("result" +s);


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
        Logger.info(dynamicForm.get("organization"));
        Logger.info(dynamicForm.get("type"));
        Logger.info(dynamicForm.get("name"));
        Logger.info(dynamicForm.get("address"));
        Logger.info(dynamicForm.get("space"));
        Logger.info(dynamicForm.get("device"));
        Logger.info(dynamicForm.get("sevice"));
        Logger.info(dynamicForm.get("classification"));
        Logger.info(dynamicForm.get("customer"));
        Logger.info(dynamicForm.get("open_time"));
        Logger.info(dynamicForm.get("cooperation"));
        Logger.info(dynamicForm.get("establish"));
        Logger.info(dynamicForm.get("founder"));
        Logger.info(dynamicForm.get("status"));
        Logger.info(dynamicForm.get("work_space"));
        Logger.info(dynamicForm.get("rent_price"));
        Logger.info(dynamicForm.get("rent_rate"));
        Logger.info(dynamicForm.get("age"));
        Logger.info(dynamicForm.get("education"));
        Logger.info(dynamicForm.get("department"));
        Logger.info(dynamicForm.get("Venture"));
        Logger.info(dynamicForm.get("lng"));
        Logger.info(dynamicForm.get("lat"));
        Logger.info(dynamicForm.get("id"));

	 Session session = Http.Context.current().session();
        int post_id =  Integer.valueOf(session.get("check_id"));

        session.put("1",dynamicForm.get("organization"));
        session.put("2",dynamicForm.get("type"));
        session.put("3",dynamicForm.get("name"));
        session.put("4",dynamicForm.get("address"));
        session.put("5",dynamicForm.get("space"));
        session.put("6",dynamicForm.get("device"));
        session.put("7",dynamicForm.get("sevice"));
        session.put("8",dynamicForm.get("classification"));
        session.put("9",dynamicForm.get("customer"));
        session.put("10",dynamicForm.get("open_time"));
        session.put("11",dynamicForm.get("cooperation"));
        session.put("12",dynamicForm.get("establish"));
        session.put("13",dynamicForm.get("founder"));
        session.put("14",dynamicForm.get("status"));
        session.put("15",dynamicForm.get("work_space"));
        session.put("16",dynamicForm.get("rent_price"));
        session.put("17",dynamicForm.get("rent_rate"));
        session.put("18",dynamicForm.get("age"));
        session.put("19",dynamicForm.get("education"));
        session.put("20",dynamicForm.get("department"));
        session.put("21",dynamicForm.get("Venture"));
        session.put("22",dynamicForm.get("lng"));
        session.put("23",dynamicForm.get("lat"));
        session.put("24",dynamicForm.get("id"));

	try{
        Connection conn = null;
        //Statement stmt = null;
        conn = DB.getConnection();
        //stmt = conn.createStatement();
        //String query = "select * from markerspace where lng= 23.9807747";
        String query = "update markerspace set organization=?, type=?, name=?, address=?, space=?, device=?, sevice=?, classification=?, customer=?, open_time=?, cooperation=?, establish=?, founder=?, status=?, work_space=?, rent_price=?, rent_rate=?, age=?, education=?, department=?, Venture=?, lng=?, lat=?, id=? where id= ?";
        PreparedStatement stmt = conn.prepareStatement(query);
        stmt.setString(25, String.valueOf(post_id));

        stmt.setString(1, session.get("1"));
        stmt.setString(2, session.get("2"));
        stmt.setString(3, session.get("3"));
        stmt.setString(4, session.get("4"));
        stmt.setString(5, session.get("5"));
        stmt.setString(6, session.get("6"));
        stmt.setString(7, session.get("7"));
        stmt.setString(8, session.get("8"));
        stmt.setString(9, session.get("9"));
        stmt.setString(10, session.get("10"));
        stmt.setString(11, session.get("11"));
        stmt.setString(12, session.get("12"));
        stmt.setString(13, session.get("13"));
        stmt.setString(14, session.get("14"));
        stmt.setString(15, session.get("15"));
        stmt.setString(16, session.get("16"));
        stmt.setString(17, session.get("17"));
        stmt.setString(18, session.get("18"));
        stmt.setString(19, session.get("19"));
        stmt.setString(20, session.get("20"));
        stmt.setString(21, session.get("21"));
        stmt.setString(22, session.get("22"));
        stmt.setString(23, session.get("23"));
        stmt.setString(24, session.get("24"));

        stmt.executeUpdate();
        stmt.close();
        conn.close();
        }
        catch(SQLException se){
        se.printStackTrace();
         }
        return redirect("/admin/spaceList");
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
    String query = "select * from markerspace where id";
    ResultSet rs = stmt.executeQuery(query);
        while (rs.next()){

        int idset=Integer.valueOf(rs.getString("id"));
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
        System.out.println("Session的check_id"+session("check_id"));

        int id =  Integer.valueOf(session.get("check_id"));
        System.out.println(session);	

	try{
    	Connection conn = null;
    	Statement stmt = null;
    	conn = DB.getConnection();
    	stmt = conn.createStatement();
    	//String query = "select * from markerspace where lng= 23.9807747";
    	String query = "select * from markerspace where id";
    	ResultSet rs = stmt.executeQuery(query);

		while (rs.next()){
        	//System.out.println(id);
        	int idset=Integer.valueOf(rs.getString("id"));
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
	    String query = "delete from markerspace where id=" + post_id;
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

  //String id = String.valueOf(id);
  System.out.println(post_id);

 Map<Integer, String> organization = new HashMap<Integer, String>();
 Map<Integer, String> type = new HashMap<Integer, String>();
 Map<Integer, String> name = new HashMap<Integer, String>();
 Map<Integer, String> address = new HashMap<Integer, String>();
 Map<Integer, String> space = new HashMap<Integer, String>();
 Map<Integer, String> device = new HashMap<Integer, String>();
 Map<Integer, String> sevice = new HashMap<Integer, String>();
 Map<Integer, String> classification = new HashMap<Integer, String>();
 Map<Integer, String> customer = new HashMap<Integer, String>();
 Map<Integer, String> open_time = new HashMap<Integer, String>();
 Map<Integer, String> cooperation = new HashMap<Integer, String>();
 Map<Integer, String> establish = new HashMap<Integer, String>();
 Map<Integer, String> founder = new HashMap<Integer, String>();
 Map<Integer, String> status = new HashMap<Integer, String>();
 Map<Integer, String> work_space = new HashMap<Integer, String>();
 Map<Integer, String> rent_price = new HashMap<Integer, String>();
 Map<Integer, String> rent_rate = new HashMap<Integer, String>();
 Map<Integer, String> age = new HashMap<Integer, String>();
 Map<Integer, String> education = new HashMap<Integer, String>();
 Map<Integer, String> department = new HashMap<Integer, String>();
 Map<Integer, String> Venture = new HashMap<Integer, String>();
 Map<Integer, String> lng = new HashMap<Integer, String>();
 Map<Integer, String> lat = new HashMap<Integer, String>();
 Map<Integer, String> id = new HashMap<Integer, String>();

// Map<Integer, Map> test = new HashMap<Integer, Map>();
  ArrayList<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();

try{
    Connection conn = null;
   // Statement stmt = null;
    conn = DB.getConnection();
   // stmt = conn.createStatement();
    //String query = "select * from markerspace where lng= 23.9807747";
    String query = "select * from markerspace where id= ?";
    PreparedStatement stmt = conn.prepareStatement(query);
    stmt.setString(1, String.valueOf(post_id));
    ResultSet rs = stmt.executeQuery();
    // stmt.executeQuery(query);
    int i=0;

	while (rs.next()){
        String organizations =rs.getString("organization");
        String types =rs.getString("type");
        String names =rs.getString("name");
        String addresss =rs.getString("address");
        String spaces =rs.getString("space");
        String devices =rs.getString("device");
        String sevices =rs.getString("sevice");
        String classifications =rs.getString("classification");
        String customers =rs.getString("customer");
        String open_times =rs.getString("open_time");
        String cooperations =rs.getString("cooperation");
        String establishs =rs.getString("establish");
        String founders =rs.getString("founder");
        String statuss =rs.getString("status");
        String work_spaces =rs.getString("work_space");
        String rent_prices =rs.getString("rent_price");
        String rent_rates =("rent_rate");
        String ages =rs.getString("age");
        String educations =rs.getString("education");
        String departments =rs.getString("department");
        String Ventures =rs.getString("Venture");
        String lngs =rs.getString("lng");
        String lats =rs.getString("lat");
        String ids =rs.getString("id");


        organization.put(i,organizations);
        type.put(i,types);
        name.put(i,names);
        address.put(i,addresss);
        space.put(i,spaces);
        device.put(i,devices);
        sevice.put(i,sevices);
        classification.put(i,classifications);
        customer.put(i,customers);
        open_time.put(i,open_times);
        cooperation.put(i,cooperations);
        establish.put(i,establishs);
        founder.put(i,founders);
        status.put(i,statuss);
        work_space.put(i,work_spaces);
        rent_price.put(i,rent_prices);
        rent_rate.put(i,rent_rates);
        age.put(i,ages);
        education.put(i,educations);
        department.put(i,departments);
        Venture.put(i,Ventures);
        lng.put(i,lngs);
        lat.put(i,lats);
        id.put(i,ids);

        i++;
        }

        list.add(organization);
        list.add(type);
        list.add(name);
        list.add(address);
        list.add(space);
        list.add(device);
        list.add(sevice);
        list.add(classification);
        list.add(customer);
        list.add(open_time);
        list.add(cooperation);
        list.add(establish);
        list.add(founder);
        list.add(status);
        list.add(work_space);
        list.add(rent_price);
        list.add(rent_rate);
        list.add(age);
        list.add(education);
        list.add(department);
        list.add(Venture);
        list.add(lng);
        list.add(lat);
        list.add(id);


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
