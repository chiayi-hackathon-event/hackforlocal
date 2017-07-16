package models;

import com.avaje.ebean.Model;

import javax.persistence.Embeddable;

/**
 * Created by abow on 15/11/15.
 */
@Embeddable
public class IIIModelMemberActionPK extends Model {
    public Long modelID;
    public Long memberID;

    public boolean equals(Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof IIIModelMemberActionPK)){
            return false;
        }
        IIIModelMemberActionPK castOther = (IIIModelMemberActionPK)other;
        return (this.modelID == castOther.modelID)
                && (this.memberID == castOther.memberID);
    }

    public int hashCode(){
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + ((int)(this.modelID ^ (this.modelID >>>32)));
        hash = hash * prime + ((int)(this.memberID ^ (this.memberID >>>32)));
        return hash;
    }
}

