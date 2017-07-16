package controllers;

import library.WSAuthenticator;
import play.mvc.Result;
import play.mvc.Security;
import play.*;


import java.io.File;

/**
 * Created by Saber on 2016/1/26.
 */
@Security.Authenticated(WSAuthenticator.class)
public class UploadFileWS extends Application {

    public Result uploadFileByStream(String folder, String fileName) {
        try {
             Logger.info("uploadFile is **** : " + folder + fileName);
            File file  = request().body().asRaw().asFile();
            file.renameTo(new File(workingDir + "/public/" + folder + "/" + fileName));
            return ok("{\"retCode\":\"0\",\"retMessage\":\"Done!\"}");
        } catch (Exception e) {
            return ok("{\"retCode\":\"1\",\"retMessage\":\"" + e.toString() + "\"}");
        }
    }
}
