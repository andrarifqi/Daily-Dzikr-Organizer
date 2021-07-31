package com.rifqiandra.dailydzikrorganizer.page.report_page.cla;

public class C_Item_DzikrName {
    String dzikrname,dzikr_target,c_recitation;

    public C_Item_DzikrName(
            String dzikrname, String dzikr_target, String c_recitation
    )
    {

        this.dzikrname = dzikrname;
        this.dzikr_target = dzikr_target;
        this.c_recitation = c_recitation;
    }
    public String get_dzikrname() {return dzikrname;}
    public void set_dzikrname(String dzikrname) {this.dzikrname = dzikrname;}

    public String get_dzikr_target() {return dzikr_target;}
    public void dzikr_target(String dzikr_target) {this.dzikr_target = dzikr_target;}

    public String get_c_recitation() {return c_recitation;}
    public void set_c_recitation(String c_recitation) {this.c_recitation = c_recitation;}


    public String toString() {return this.dzikrname;}
}
