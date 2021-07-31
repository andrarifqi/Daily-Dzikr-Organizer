package com.rifqiandra.dailydzikrorganizer.page.dzikr_page.module.custom.cla;

public class C_Item_DzikrName {

    String id,dzikrname;

    public C_Item_DzikrName(
            String vid,
            String vjudul
    )
    {
        this.id = vid;
        this.dzikrname = vjudul;
    }

    public String get_id() {return id;}
    public void set_id(String vid) {this.id = vid;}

    public String get_dzikrname() {return dzikrname;}
    public void set_dzikrname(String vjudul) {this.dzikrname = vjudul;}

    public String toString() {return this.dzikrname;}
}
