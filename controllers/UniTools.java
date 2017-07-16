package controllers;
import com.avaje.ebean.Ebean;
import models.*;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import play.mvc.Controller;
//import play.api.Logger;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import play.mvc.Http;

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
//2016.6.28
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * Created by abow on 15/10/31.
 */			
public class UniTools extends Controller {

	
	
	static final String memberToken = "memberToken";

    
    //   TODO convert member_token to member_id
    public Long convertMemberTokenToMemberID(String memberToken){
        if (memberToken == null || memberToken.isEmpty()){
            return 0L;
        }else{
            MemberToken dbMT = MemberToken.find.byId(memberToken);
            if (dbMT == null) return 0L;  // no record
            if (!isTokenAvailable(dbMT.startDateTime,dbMT.timeInterval)) return 0L;    // out of available date
            return dbMT.member.memberID;
        }
    }

    public String generateMemberToken(Member dbM) throws Exception{
        try{
            List<MemberToken> dbMTs = Ebean.find(MemberToken.class).where().in("member_id",dbM.memberID).findList();
            for (MemberToken dbMT : dbMTs){
                dbMT.delete();
            }

            MemberToken dbMT = new MemberToken();
            dbMT.member = dbM;
            dbMT.memberToken = createTokenString();
            dbMT.startDateTime = new Date();
            dbMT.timeInterval = 720;

            Ebean.save(dbMT);

            return dbMT.memberToken;
        }catch(Exception ee){
            throw  ee;
        }
    }

    public String assignFBAccessToken2MemberToken(Member dbM,String FBAccessToken) throws Exception{
        try{
            List<MemberToken> dbMTs = Ebean.find(MemberToken.class).where().in("member_id",dbM.memberID).findList();
            for (MemberToken dbMT : dbMTs){
                dbMT.delete();
            }

            MemberToken dbMT = new MemberToken();
            dbMT.member = dbM;
            dbMT.memberToken = FBAccessToken;
            dbMT.startDateTime = new Date();
            dbMT.timeInterval = 720;

            Ebean.save(dbMT);

            return dbMT.memberToken;
        }catch(Exception ee){
            throw  ee;
        }
    }

    public String generateGroupID() throws Exception{
        return UUID.randomUUID().toString().replace("-","");
    }

    public String generateAccessKey(String prefix) throws Exception{
        return prefix + UUID.randomUUID().toString().replace("-","");
    }

    private String createTokenString() throws Exception{
        String strUUID = UUID.randomUUID().toString().replace("-","");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd:HH:mm:ss");
        String strDate = sdf.format(new Date());
        String []splitDate = strDate.split(":");
        return new StringBuilder(strUUID)
                .insert(0, splitDate[0])
                .insert(6, splitDate[1])
                .insert(10,splitDate[2])
                .insert(14,splitDate[3])
                .insert(18,splitDate[4])
                .insert(22,splitDate[5]).toString();
    }

    private boolean isTokenAvailable(Date startDate,int interval){
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.HOUR,interval);
        Date endDate = cal.getTime();
        return !(new Date().after(endDate));
    }

    // TODO generateAvailableMemberID
    public Long generateMemberID(String prefix)throws Exception{

        try{
            IIISerialNumber modelCount = new IIISerialNumber();
            Ebean.save(modelCount);
            if (modelCount.id > 100000){
                execTruncate("iiiserial_number");
                modelCount = new IIISerialNumber();
                Ebean.save(modelCount);
            }
            String serialNumber = String.format("%05d",modelCount.id);

            return generateID(serialNumber,prefix);
        }catch(Exception ee){
            throw  ee;
        }
    }

    // TODO generateAvailableMemberID
    public Long generateModelID(String prefix) throws Exception{
        try{
            IIISerialNumber modelCount = new IIISerialNumber();
            Ebean.save(modelCount);
            if (modelCount.id > 100000){
                execTruncate("iiiserial_number");
                modelCount = new IIISerialNumber();
                Ebean.save(modelCount);

            }
            String serialNumber = String.format("%05d",modelCount.id);

            return generateID(serialNumber,prefix);
        }catch(Exception ee){
            throw  ee;
        }
    }

    private Long generateID(String serialNumber,String prefix) throws Exception{
        DateTime taipeiDatetime = new DateTime(DateTimeZone.forID("Asia/Taipei"));
        DateTimeFormatter timeFormat = DateTimeFormat.forPattern("yyyyMMdd");
        String currentTime = timeFormat.print(taipeiDatetime);
        String originalId = currentTime + serialNumber;

        int remainder = 0;
        String encodeRaw = originalId + prefix;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] shabytes = md.digest(encodeRaw.getBytes("UTF-8"));
        int value = 0;
        for (int i = 0 ; i < 4 ; i ++){
            int shift = (4-1-i)*8;
            value +=(shabytes[i] & 0x000000FF) << shift;
        }
        remainder = Math.abs(value) % 100;

        String shaID = originalId + String.format("%02d",remainder);
        return Long.valueOf(shaID);
    }

    private void execTruncate(String strTable) throws Exception{
//        Ebean.beginTransaction();
        Ebean.execute(Ebean.createSqlUpdate("TRUNCATE TABLE " + strTable));

//        Ebean.commitTransaction();

    }

//由此新增 2016.06.28
/*	 public static void verifyID(Long MemberID){

	String.valueOf(MemberID);

	List<Admin_permit> record  = Admin_permit.find.where().eq("member_id",MemberID).findList();
	response().setCookie("rec",(String.valueOf(record.get(0).group)));

            
        
    }

*/


    public static String getEmail (){
        String email;
        String token;

        if(request().cookies().get(memberToken) == null)
         return "";
        else
         token = request().cookies().get(memberToken).value().toString();

        MemberToken dbMT = MemberToken.find.byId(token);
        email = dbMT.member.email;
        return email;
    }



    






}


