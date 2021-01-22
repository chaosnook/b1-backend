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
        ERR_01001("ืชื่อ ห้ามว่าง"),
        ERR_01002("ชื่อธนาคาร ห้ามว่าง"),
        ERR_01003("หมายเลขบัญชี ห้ามว่าง"),
        ERR_01004("ไม่มีชื่อนี้ในระบบ"),
        ERR_01005("ไม่พบข้อมลมิจฉาชีพนี้"),

        ERR_02000("bank_code ห้ามว่าง"),
        ERR_02001("bank_type ห้ามว่าง"),
        ERR_02002("bank_name ห้ามว่าง"),
        ERR_02003("bank_account_name ห้ามว่าง"),
        ERR_02004("bank_account_no ห้ามว่าง"),
        ERR_02005("username ห้ามว่าง"),
        ERR_02006("password ห้ามว่าง"),
        ERR_02007("bank_order ห้ามว่าง"),
        ERR_02008("bank_group ห้ามว่าง"),
        ERR_02009("bot_ip ห้ามว่าง"),
        ERR_02010("ไม่มีข้อมูลธนาคาร"),
        ERR_02011("ไม่พบข้อมูลธนาคารนี้"),
        ERR_02012("หมายเลขบัญชีธนาคารต้องเป็นตัวเลขเท่านั้น"),
        ERR_02013("หมายเลข Bot Ip ไม่ถูกต้อง"),

        ERR_01101("role_code ห้ามว่าง"),
        ERR_01102("description ห้ามว่าง"),
        ERR_01103("role_code มีอยู่แล้ว"),
        ERR_01104("Id not found"),
        ERR_01105("Id ห้ามว่าง"),
        ERR_01106("phone number ห้ามว่าง"),
        ERR_01107("phone number นี้มีอยู่ในระบบแล้ว"),
        ERR_01108("password ห้ามว่าง"),
        ERR_01109("name ห้ามว่าง"),
        ERR_01110("bot ip ห้ามว่าง"),
        ERR_01111("bank group ห้ามว่าง"),
        ERR_01112("new user flag ห้ามว่าง"),
        ERR_01113("active ห้ามว่าง"),
        ERR_01114("phone number ไม่ถูกต้อง"),
        ERR_O1115("หมายเลข Bot Ip ไม่ถูกต้อง"),

        ERR_03000("IP ห้ามว่าง"),
        ERR_03001("ไม่พบข้อมูล"),

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
        MSG_01002("แก้ไขข้อมูลมิจฉาชีพสำเร็จ"),
        MSG_01003("ลบข้อมููลมิจฉาชีพสำเร็จ"),

        MSG_02000("เพิ่มข้อมูลธนาคารสำเร็จ"),
        MSG_02001("แก้ไขข้อมูลธนาคารสำเร็จ"),
        MSG_02002("ลบข้อมูลธนาคารสำเร็จ"),
        GRAPES("GREEN");

        public String msg;

        MESSAGE(String label) {
            this.msg = label;
        }
    }
}