package com.game.b1ingservice.commons;

public class Constants {
    public enum  ERROR
    {
        ERR_99999("ระบบไม่สามารถดำเนินการได้"),
        ERR_00001("มีข้อมูลอยู่ในระบบแล้ว"),
        ERR_00002("ข้อมูลถูกล๊อกอยู่"),
        ERR_00003("username ห้ามว่าง"),
        ERR_00004("password ห้ามว่าง"),
        ERR_00005("ชื่อ-นามสกุล ห้ามว่าง"),
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