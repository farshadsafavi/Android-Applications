package com.journaldev.recyclerviewmultipleviewtype;

import io.realm.RealmObject;

/**
 * Created by Owlia on 12/1/2016.
 */

public class PersonalInventory extends RealmObject {
    String campus;
    String affiliation;
    String living;
    boolean accessibilty=false;
    boolean lGBTQ=false;
    boolean international=false;
    boolean haveChild=false;

    public String getCampus(){return campus;}
    public void setCampus(String campus){this.campus=campus;}

    public String getAffiliation(){return affiliation;}
    public void setAffiliation(String affiliation){this.affiliation=affiliation;}

    public String getLiving(){return living;}
    public void setLiving(String living){this.living=living;}

    public boolean isAccessibilty(){return accessibilty;}
    public void setAccessibilty(boolean accessibilty){this.accessibilty=accessibilty;}

    public boolean islGBTQ(){return lGBTQ;}
    public void setlGBTQ(boolean lGBTQ){this.lGBTQ=lGBTQ;}

    public boolean isInternational(){return international;}
    public void setInternational(boolean international){this.international=international;}

    public boolean isHaveChild(){return haveChild;}
    public void setHaveChild(boolean haveChild){this.haveChild=haveChild;}

}
