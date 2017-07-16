package controllers;

import play.mvc.Controller;
import play.mvc.Result;
import views.html.FixedHTMLs.*;

public class FixedHTMLs extends Controller {

    public Result tool3D(){
        return ok(tool3D.render("The 3D tool of Fast Lab."));
    }

    public Result gallerySearch() {
        return ok(gallerySearch.render("The gallery searching tool of Fast Lab."));
    }

    public Result resourceFastDetail(){
        return ok(resourceFastDetail.render("Fastlab"));
    }

    public Result stlViewer() {
        return ok(stlViewer.render());
    }

    public Result objViewer() {
        return ok(objViewer.render());
    }

    public Result sitemap() {
        return ok(sitemap.render("Fast Lab Sitemap"));
    }

    public Result ErrorMessages() {
          return ok(ErrorMessages.render(""));
        }
	//2017.02.08新增 3DP競賽列表//
    public Result champion() {
        return ok(champion.render(""));
    }


}
