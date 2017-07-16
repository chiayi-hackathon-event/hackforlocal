package controllers;

import play.*;
import play.libs.*;
import play.mvc.*;
import play.data.*;
import play.mvc.Http.Session;
import views.html.*;
import views.html.outsite.*;
import forms.*;
import play.data.DynamicForm;
import play.data.Form;
import models.*;
import com.avaje.ebean.Ebean;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.security.MessageDigest;
import javax.xml.bind.DatatypeConverter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import play.libs.mailer.Email;
import play.libs.mailer.MailerClient;
import javax.inject.Inject;
import java.util.Base64;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import javax.imageio.ImageIO;
import java.time.format.DateTimeFormatter;
import  java.time.*;

import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.csrf.*;

import objects.objRetEvent;
import objects.objRetEvents;
import objects.objRetWS;

public class outsite extends Controller {
    @Inject MailerClient mailerClient;

  public Result outsearch() {
        return ok(outsearch.render("The tool detail of seatch ." , "search"));
    }


    public void toolclick(int type,Tool tool)
    {
     switch (type){
                 // view
            case 0:
                tool.viewedTimes = tool.viewedTimes +1;
                break;
            // download
            case 1:
                tool.download = tool.download +1;
                break;
     }
     Ebean.save(tool);

    }  


    public Result toolDetail(String programName) {
        List<Tool> toollist = Tool.find.where().eq("program",programName).findList();
        toolclick(0,toollist.get(0));
        return ok(toolDetail.render("Tool Detail",toollist));
    }


   public Result activegenFigure()
   {
    DynamicForm dynamicForm = Form.form().bindFromRequest();
    Session session = Http.Context.current().session();
    int tool_id =   Integer.valueOf(dynamicForm.get("figure"));
    Tool tool = Tool.find.byId(new Long(tool_id));
    toolclick(1,tool);
    List<scad_info> scad = scad_info.find.where().eq("tool_id",tool_id).findList();
//List<Scad_info> scad =  Scad_info.find.all();
//     String test = dynamicForm.toString();
 //   Map<String,String> data = dynamicForm.data();
 //     int size = data.size();
    //  String testtest = String.valueOf(tool_id); 


    String viewpoint = scad.get(0).viewpoint;
    String mail_subject = scad.get(0).Subject; 
    String output ="";
    String mail = dynamicForm.get("email");
    try {
     output = DatatypeConverter.printHexBinary(
           MessageDigest.getInstance("MD5").digest(mail.getBytes("UTF-8")));
     } catch (Exception e) {
             e.printStackTrace();
     }

     session.put("email",dynamicForm.get("email"));
     session.put("MD5",output);  

    int  i,j=0;
    String setup = scad.get(0).Default ;  
    String[] parameter = setup.split(",");
    String fieldname = "";
    String content = "";
    String[] type = scad.get(0).Type.split(",");
    String tmpString[] = null;
    String usrString[] = null;
    int tmpString_size = 0;
    int usrString_size = 0;
  //  content =   Integer.toString(scad.get(0).Number);
    for(i = 0 ; i < scad.get(0).Number ;i ++ )
    {
      fieldname = "field" + (i+1);
  

   
      if(type[i].equals("1") )
      {
     Logger.info(" tmpString_size: " + parameter[i].length()  );
     Logger.info(" usrString_size : " + dynamicForm.get(fieldname).length() );

       tmpString_size = parameter[i].length();
       usrString_size = dynamicForm.get(fieldname).length();
       usrString = dynamicForm.get(fieldname).split("");
       content = content + fieldname + "=[";
        if(usrString_size == 0)
        {
         tmpString = parameter[i].split("");
         for(j = 0 ;j < tmpString_size ; j ++ )
          {
           if(j!= tmpString_size -1 )
            content = content + "\"" + tmpString[j] + "\","; 
           else
            content = content + "\"" + tmpString[j] + "\"];";

          } 
         
        }
        else
        {
     
          if (usrString_size > tmpString_size)
            usrString_size = tmpString_size; 

         for(j = 0 ;  j < tmpString_size ; j ++)
          {
           if(j < usrString_size   )
           {
             if(j == tmpString_size -1 )
             content = content + "\"" + usrString[j] + "\"];";
             else
             content = content + "\"" + usrString[j] + "\",";
           } 
           else
            {
                if(j!= tmpString_size -1 )
                {
                 content = content + "\"\",";
                }
                else
                {
                 content = content + "\"\"];";
                }
            }
          }
        }
 
      }
     else
     {
      if(dynamicForm.get(fieldname) !="")
      {
       content = content + fieldname  + "=" + "\"" + dynamicForm.get(fieldname) + "\";";     
      }
      else
      {
          content = content + fieldname  + "=" + "\"" + parameter[i] +"\";";  
      }


     }
 
   
    }


int p=0;

  if(dynamicForm.get("active_code").toLowerCase().matches("cp.*")   )
 {
  p=1;
  System.out.println("*****CPCPCPCP");
 }


  if(dynamicForm.get("active_code").toLowerCase().matches("ch.*")   )
  {
  p=2;
  System.out.println("*****CHCHCH");
  }

// System.out.println("*****"+dynamicForm.get("active_code").toLowerCase().matches("cp.*")+"***");

  if(dynamicForm.get("active_code").equals(""))
  {
   p=0;
  }

 System.out.println("*****"+dynamicForm.get("active_code")+"***" + p);
   String log_content = "";
   if( p > 0)
   {
    log_content = content +  "active_code=" + "\"" + dynamicForm.get("active_code") + "\""   ;

   }
   else
   {
    log_content = content;
   }






   Logger.info(" activegenFigure content is : " + content);
   activeDb(mail,output,log_content,tool_id);

   content += scad.get(0).Scad;
   StringToFile(content,output + ".scad" );
   genPng(output + ".scad" ,"../images/outsite/" + output  ,viewpoint);

   content = FiletoBase64(output + ".png","png");

   String[] mail_name = mail.split("@");
  
  String body ="";
  Email email = new Email();
  email.addTo("<"+mail+">");
  
  switch (p){
                 // view
            case 0:
                body = "歡迎您參與http://fastlab.tw網站\r客制化工具體驗，\r新客製化工具與體驗項目將陸續上架，\r歡迎有興趣之民眾持續關注Fastlab.tw官網、\r粉絲專頁。";
                break;
            // download
            case 1:
                body="歡迎您參與http://fastlab.tw網站\r客制化工具體驗活動，本活動(代碼cp)\r由FastLab創用中心主辦，\r新藝文化有限公司協辦，\r指導單位：經濟部工業局。"+"\n\n"+"新的客製化工具與體驗項目將陸續上架，\r歡迎有興趣之民眾持續關注Fastlab.tw官網、\r粉絲專頁。";
                email.addCc("Ctrl P Service <ctrlp.0002@gmail.com>");
                mail_subject =  mail_subject +":" + dynamicForm.get("active_code");
                body = body + "\n\n" +"收件資訊:"  + dynamicForm.get("memo") + "\n\n";
                break;
            case 2:
                body="歡迎您參與http://fastlab.tw網站\r客制化工具體驗活動，本活動(代碼ch)\r由FastLab創用中心主辦，\r騏驥坊創客教育中心協辦，\r指導單位：經濟部工業局。"+"\n\n"+"新的客製化工具與體驗項目將陸續上架，\r歡迎有興趣之民眾持續關注Fastlab.tw官網、\r粉絲專頁。";
                email.addCc("CHIGI Service <service@chi-gi.com>");
                mail_subject =  mail_subject +":" + dynamicForm.get("active_code");
                body = body + "\n\n" +"收件資訊:"  + dynamicForm.get("memo") + "\n\n";
                break;
     }


   email.setFrom("FastLab Service <fastlab.tw@gmail.com>");
   email.addBcc("FastLab Service <fastlab.tw@gmail.com>");
   email.setSubject(mail_subject);
   email.setBodyText(body);
   LocalDateTime now = LocalDateTime.now();
   DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
   String formattedDateTime = now.format(formatter);
   email.addAttachment(formattedDateTime+"_"+mail_name[0] + ".stl", new File("public/images/outsite/" + output + ".stl"  ));
//   mailerClient.send(email);

  System.out.println(mail_subject);
    System.out.println(body);


    return ok(content);
   }


    public void StringToFile(String content, String filename)
    {
      try {
      String result_file="public/scad/" + filename;
      File scadFile = new File(result_file);
      FileWriter fw = new FileWriter(scadFile);
       fw.write(content);
       fw.close();
      }catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void  genPng(String sou,String des,String viewpoint)
     {
       String realpath = System.getProperty("user.dir") + "/public/scad" ;
       String tmpFile = des;
       try {

             tmpFile = des + ".stl";
     String[]     command = new String[]{"bash","-c","cd " + realpath + "; xvfb-run -s  \"-screen 0 640x480x24\" openscad --render " + viewpoint   +  " --imgsize=300,300 -o " + tmpFile  + " " +sou};
        Process p2 = Runtime.getRuntime().exec(command);
       int result1 = p2.waitFor ();
       Logger.info(" command is "+ Arrays.toString(command));
       Logger.info(" genPng result1 is "+ result1);

        tmpFile = des + ".png";
        command = new String[]{"bash","-c","cd "+ realpath  +"; xvfb-run -s \"-screen 0 640x480x24\" openscad --preview " + viewpoint   +  " --imgsize=300,300 -o " + tmpFile  + " " +sou};
        Process p1 = Runtime.getRuntime().exec(command);
      int result =  p1.waitFor ();
         Logger.info(" command is "+ Arrays.toString(command));
        Logger.info(" genPng result is "+ result);

       }catch (Exception e) {
            System.out.println("Excepcion");
            e.printStackTrace();
        }
     }

    public String FiletoBase64(String imgFile,String fileType)
    {
     final Base64.Encoder encoder = Base64.getEncoder();
     String imageString = null;
     BufferedImage img = null;
     ByteArrayOutputStream imgStream = new ByteArrayOutputStream();
   //  imgFile =  "/public/images/outsite/" + imgFile;
     imgFile = System.getProperty("user.dir") + "/public/images/outsite/" + imgFile;
     Logger.info("imgFile :" + imgFile);

     try {
     img = ImageIO.read(new File(imgFile));
     ImageIO.write(img,fileType ,imgStream );
     byte[] imageBytes=imgStream.toByteArray();
     imageString = encoder.encodeToString(imageBytes);
     } catch (IOException e) {
      e.printStackTrace();
     }

    imageString = "data:image/png;base64," + imageString;
     return (imageString);
    }

   public void  activeDb(String email,String md5,String content,int type)
    {
      objRetWS objWS;
      Ebean.beginTransaction();
      try{
      Active_info dbActive = new Active_info();
      dbActive.email = email;
      dbActive.md5 = md5;
      dbActive.content = content;
      dbActive.tag_type = type;
      Ebean.save(dbActive);
      Ebean.commitTransaction();
      objWS = new objRetWS();
      objWS.retCode = 0;
      objWS.retMessage = dbActive.email;

        }catch(Exception ee){
            objWS = new objRetWS(1,ee.toString());
        }finally {
            Ebean.endTransaction();
        }

    }
   @RequireCSRFCheck 
   public Result download()
   {
      Session session = Http.Context.current().session();
      String email = session.get("email");
      String[] mail_name = email.split("@");
      LocalDateTime now = LocalDateTime.now();
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
      String formattedDateTime = now.format(formatter);
      String download = formattedDateTime +"_"+mail_name[0] + ".stl";
      String md5Fiile = session.get("MD5") + ".stl";
      Logger.info("download : " + md5Fiile + download);
      String realpath = System.getProperty("user.dir") + "/public/images/outsite" ;

      try {
           String[]     command = new String[]{"bash","-c","cd "+ realpath + "; cp " + md5Fiile  + " " + download};
          Process p3 = Runtime.getRuntime().exec(command);
          int result3 = p3.waitFor ();

       }catch (Exception e) {
            System.out.println("Excepcion");
            e.printStackTrace();
        }


       return ok(new java.io.File("public/images/outsite/" + download  ));
   }



}

