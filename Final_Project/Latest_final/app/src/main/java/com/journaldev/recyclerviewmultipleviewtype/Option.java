package com.journaldev.recyclerviewmultipleviewtype;

/**
 * Created by Farshad on 10/25/2016.
 */
public class Option {

    public static final int TEXT_TYPE=0;
    public static final int CHECKBOX_TYPE=1;

    public int type;
    String text = null;
    boolean selected = false;

    public Option(int type, String text, boolean selected) {
        this.type=type;
        this.text = text;
        this.selected = selected;
    }

    public String getName() {
        return text;
    }
    public void setName(String name) {
        this.text = text;
    }

    public boolean isSelected() {
        return selected;
    }
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

}
