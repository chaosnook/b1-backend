package com.game.b1ingservice.commons;

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

        ERR_00008("role_code ห้ามว่าง"),
        ERR_00009("description ห้ามว่าง"),
        ERR_00010("role_code มีอยู่แล้ว"),
        ERR_00011("Id not found"),

        ;

        public String msg;

        ERROR(String label) {
            this.msg = label;
        }
    }
    public enum MESSAGE{
        MSG_00000("ระบบดำเนินการสำเร็จ"),
        GRAPES("GREEN");

        public String msg;

        MESSAGE(String label) {
            this.msg = label;
        }
    }
}