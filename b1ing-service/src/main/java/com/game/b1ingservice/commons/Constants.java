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
        ERR_00008("ไม่พบข้อมูล agent นี้"),
        ERR_00009("ไม่พบข้อมูล user,admin นี้"),
        ERR_00010("id ห้ามว่าง"),


        ERR_01000("id ห้ามว่าง"),
        ERR_01001("ืname ห้ามว่าง"),
        ERR_01002("ชื่อธนาคาร ห้ามว่าง"),
        ERR_01003("หมายเลขบัญชี ห้ามว่าง"),
        ERR_01004("ไม่มีชื่อนี้ในระบบ")
        ;

        public String msg;

        ERROR(String label) {
            this.msg = label;
        }
    }
    public enum MESSAGE{
        MSG_00000("ระบบดำเนินการสำเร็จ"),

        MSG_01000("ดึงข้อมูลมิจฉาชีพสำเร็จ"),
        MSG_01001("เพิ่มข้อมูลมิจฉาชีพสำเร็จ"),
        GRAPES("GREEN");

        public String msg;

        MESSAGE(String label) {
            this.msg = label;
        }
    }
}