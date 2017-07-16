package library;

import com.avaje.ebean.Ebean;
import models.DeveloperGroupAuthorization;
import models.DeveloperRegister;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;

import java.util.List;

/**
 * Created by abow on 15/11/1.
 */
public class WSAuthenticator extends Security.Authenticator {
    @Override
    public String getUsername(Http.Context ctx) {
        String access_key = getTokenFromHeader(ctx);
        String access_method = getAccessMethod(ctx);
        if (access_key != null && access_method !=null) {
            access_method = (access_method.indexOf("?")>0)?access_method.substring(0,access_method.indexOf("?")):access_method;

            DeveloperRegister dbDR = DeveloperRegister.find.byId(access_key);
            if (dbDR == null) return null; // no record
            if (!dbDR.active) return null; // not active
            if (dbDR.isAdmin) return "grant access";
            for (DeveloperGroupAuthorization dbDGA :dbDR.devGroup.devGroupAuthorizations){
                if (dbDGA.resource.equals(access_method)) return "grant access";
            }
            return null;

        }
        return null;
    }

    @Override
    public Result onUnauthorized(Http.Context context) {
        return super.onUnauthorized(context);
    }

    private String getTokenFromHeader(Http.Context ctx) {
        String[] access_key = ctx.request().headers().get("access_key");
        if ((access_key != null) && (access_key.length == 1) && (!access_key[0].isEmpty())) {
            return access_key[0];
        }
        return null;
    }

    private String getAccessMethod(Http.Context ctx) {
        String url = ctx.request().uri();
        if (url != null && !url.isEmpty()) {
            return url;
        }
        return null;
    }
}
