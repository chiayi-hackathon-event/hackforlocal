package objects;

import com.google.gson.annotations.Expose;

import models.Events;

import java.util.List;

/**
 * Created by apple on 2015/11/18.
 */
public class objRetEvents extends objRetWS{
    @Expose
    public List<Events> events;
}
