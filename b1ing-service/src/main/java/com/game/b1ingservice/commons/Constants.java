package com.game.b1ingservice.commons;

import javax.swing.*;

public class Constants {
    public enum  ERROR
    {
        ERR_TOKEN("Your token has expired"),
        ERR_99999("ระบบไม่สามารถดำเนินการได้"),
        ERR_00000("ไม่สามารถต่อ Database ได้"),
        ERR_00001("มีข้อมูลอยู่ในระบบแล้ว"),
        ERR_00002("ข้อมูลถูกล๊อกอยู่"),
        ERR_00003("username ห้ามว่าง"),
        ERR_00004("username นี้มีอยู่แล้ว"),
        ERR_00005("password ห้ามว่าง"),
        ERR_00006("ชื่อ-นามสกุล ห้ามว่าง"),
        ERR_00007("username หรือ password ไม่ถูกต้อง"),

//        ERR_10001("name ห้ามว่าง"),
        ERR_10002("name ห้ามว่าง"),
        ERR_10003("ีname ห้ามซ้ำ"),
        ERR_10004("quantity ห้ามว่าง"),
        ERR_10005("cost ห้ามว่าง"),
        ERR_10006("sale ห้ามว่าง"),
        ;

        public String msg;

        ERROR(String label) {
            this.msg = label;
        }
    }
    public enum MESSAGE{
        MSG_00000("ระบบดำเนินการสำเร็จ"),

        MSG_10000("เพิ่มสินค้าสำเร็จ"),
        GRAPES("GREEN");

        public String msg;

        MESSAGE(String label) {
            this.msg = label;
        }
    }
}