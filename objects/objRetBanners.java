package objects;

import com.google.gson.annotations.Expose;
import models.IIIBanner;

import java.util.List;

/**
 * Created by roger.lee on 2016/1/7.
 */
public class objRetBanners extends objRetWS{
    @Expose
    public List<IIIBanner> banners;
}
