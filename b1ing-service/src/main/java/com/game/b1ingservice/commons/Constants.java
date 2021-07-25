package com.game.b1ingservice.commons;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import static com.game.b1ingservice.commons.Constants.AGENT_CONFIG.*;

public class Constants {

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public static int AMB_ERROR = 999;
    public enum  ERROR
    {
        ERR_TOKEN("Your token has expired"),
        ERR_PREFIX("ข้อมูลไม่ถูกต้อง"),
        ERR_IMAGE("ไม่พบไฟล์รูปภาพ"),
        ERR_99999("ระบบไม่สามารถดำเนินการได้"),
        ERR_88888("ไม่มีสิทธ์ในการทำรายการ"),
        ERR_77777("Account นี้ถูก login ซ้อนบนอุปกรณ์อื่น"),

        ERR_99001("ไม่สามารถเข้าถึงไฟล์ได้"),
        ERR_99002("ข้อมูลไฟล์ไม่ถูกต้อง"),
        ERR_99003("ไม่พบไฟล์ดังกล่าว"),


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
        ERR_00011("ไม่พบข้อมูล"),
        ERR_00012("ไม่พบข้อมูล user นี้"),

        ERR_00013("กรุณารออนุมัติถอน"),
        ERR_00014("ถอนไม่สำเร็จ"),
        ERR_00015("Admin คนอื่นกำลังทำรายการ"),


        ERR_01000("id ห้ามว่าง"),
        ERR_01001("ืชื่อ ห้ามว่าง"),
        ERR_01002("ชื่อธนาคาร ห้ามว่าง"),
        ERR_01003("หมายเลขบัญชี ห้ามว่าง"),
        ERR_01004("ไม่มีชื่อนี้ในระบบ"),
        ERR_01005("ไม่พบข้อมลมิจฉาชีพนี้"),

        ERR_02000("bank code ห้ามว่าง"),
        ERR_02001("bank type ห้ามว่าง"),
        ERR_02002("bank name ห้ามว่าง"),
        ERR_02003("bank account name ห้ามว่าง"),
        ERR_02004("bank account no ห้ามว่าง"),
        ERR_02005("username ห้ามว่าง"),
        ERR_02006("password ห้ามว่าง"),
        ERR_02007("bank order ห้ามว่าง"),
        ERR_02008("bank group ห้ามว่าง"),
        ERR_02009("bot ip ห้ามว่าง"),
        ERR_02010("ไม่มีข้อมูลธนาคาร"),
        ERR_02011("ไม่พบข้อมูลธนาคารนี้"),
        ERR_02012("หมายเลขบัญชีธนาคารต้องเป็นตัวเลขเท่านั้น"),
        ERR_02013("หมายเลข Bot Ip ไม่ถูกต้อง"),
        ERR_02014("bank order ไม่ถูกต้อง"),
        ERR_02016("bank group ไม่ถูกต้อง"),
        ERR_02018("bank group และ bank order มีอยู่ในระบบแล้ว"),

        ERR_01101("role code ห้ามว่าง"),
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
        ERR_01116("bank group ไม่ถูกต้อง"),
        ERR_01117("bank group มีอยู่ในระบบแล้ว"),
        ERR_01118("username ห้ามว่าง"),
        ERR_01119("username นี้มีอยูในระบบแล้ว"),
        ERR_01120("bank name ห้ามว่าง"),
        ERR_01121("account number ห้ามว่าง"),
        ERR_01122("account number ไม่ถูกต้อง"),
        ERR_01123("first name ห้ามว่าง"),
        ERR_01124("last name ห้ามว่าง"),
        ERR_01125("is bonus ห้ามว่าง"),
        ERR_01126("password ไม่ถูกต้อง"),
        ERR_01127("username นี้ไม่มีอยูในระบบ"),
        ERR_01128("credit ห้ามว่าง"),
        ERR_01129("point ห้ามว่าง"),
        ERR_01130("prefix ห้ามว่าง"),
        ERR_01131("credit ไม่ถูกต้อง"),
        ERR_01132("ไม่พบ wallet ของ username นี้"),
        ERR_01133("มี credit ไม่พอ"),
        ERR_01134("แก้ไขข้อผิดพลาดเกินกำหนดของวันนี้"),
        ERR_01135("หมายเลขบัญชีธนาคารนี้ มีอยู่ในระบบแล้ว"),
        ERR_01136("ถอนเงินเกินกำหนด กรุณาทำรายการใหม่วันพรุ้งนี้"),
        ERR_03000("IP ห้ามว่าง"),
        ERR_03001("ไม่พบข้อมูล"),

        ERR_04000("ไม่พบข้อมูล true wallet"),
        ERR_04001("ไม่สามารถแก้ไขฝาก true wallet"),

        ERR_05001("กรุณาเลือกช่วงเวลาที่ต้องการค้นหา"),

        ERR_06001("name ห้ามว่าง"),
        ERR_06002("type bonus ห้ามว่าง"),
        ERR_06003("type promotion ห้ามว่าง"),
        ERR_06004("max bonus ห้ามว่าง"),
        ERR_06005("min topup ห้ามว่าง"),
        ERR_06006("max topup ห้ามว่าง"),
        ERR_06007("turn over ห้ามว่าง"),
        ERR_06008("max withdraw ห้ามว่าง"),
        ERR_04002("คุณมี Point ไม่เพียงพอ"),
        ERR_04003("คุณมี Credit ไม่เพียงพอ กรุณาเติมเงิน"),
        ERR_04004("คุณมี Turn over อีก %s credit"),
        ERR_04005("คุณมี Credit ไม่เพียงพอ"),
        ERR_09001("BotType ห้ามว่าง"),
        ERR_09002("TransactionId ห้ามว่าง"),
        ERR_09003("BotIp ห้ามว่าง"),
        ERR_09004("BankCode ห้ามว่าง"),
        ERR_09005("BankAccountNo ห้ามว่าง"),
        ERR_09006("AccountNo ห้ามว่าง"),
        ERR_09007("Amount ห้ามว่าง"),
        ERR_09008("TransactionDate ห้ามว่าง"),
        ERR_09009("Type ห้ามว่าง"),
        ERR_09010("max receive bonus ห้ามว่าง"),
        ERR_09011("ไม่พบโปรโมชัน"),

        ERR_10001("ไม่สามารถตัด credit ได้"),

        ERR_11001("bonus ของ condition ห้ามว่าง"),
        ERR_11002("max ของ condition ห้ามว่าง"),
        ERR_11003("min ของ condition ห้ามว่าง"),
        ERR_11004("ประเภทโบนัท ไม่ถูกต้อง"),

        ERR_12001("min topup ห้ามมากกว่า max topup "),
        ERR_12002("min topup ห้ามเท่ากับ max topup"),
        ERR_12003("เงี่อนไข min topup ห้ามน้อยกว่า promotion min topup"),
        ERR_12004("เงื่อนไข max topup ห้ามมากกว่า promotion max topup"),
        ;

        public String msg;

        ERROR(String label) {
            this.msg = label;
        }
    }
    public enum MESSAGE{
        MSG_00000("ระบบดำเนินการสำเร็จ"),

        MSG_00001("ถอนเงินสำเร็จ"),

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

    public enum ROLE {
        XSUPERADMIN("XSUPERADMIN"),
        SUPERADMIN("SUPERADMIN"),
        ADMIN("ADMIN"),
        STAFF("STAFF");

        private final String role;

        ROLE(final String text) {
            this.role = text;
        }

        @Override
        public String toString() {
            return role;
        }
    }

    public static class Sort {
        public static final String ASC = "A";
        public static final String DESC = "D";
    }

    public static class AGENT_CONFIG_TYPE  {
        public static final String AMB_CONFIG = "AMB-CONFIG";
    }
    public static class AGENT_CONFIG  {
        public static final String MAX_AUTO_WITHDRAW = "maxAutoWithdraw";
        public static final String MIN_WITHDRAW_CREDIT = "minWithdrawCredit";
        public static final String COUNT_WITHDRAW = "countWithDraw";
        public static final String LIMIT_WITHDRAW = "limitWithDraw";
        public static final String APPROVE_WITHDRAW_AUTO = "approveWithdrawAuto";
        public static final String APPROVE_WITHDRAW_AUTO_NEW = "approveWithdrawAutoNew";
        public static final String ON_OFF_WEBSITE = "onOffWebsite";
        public static final String ALLOW_OUTSTANDING_BET = "allowOutstandingBet";
        public static final String TRANS_CREDIT = "TRANS_CREDIT";
        public static final String URL_CONFIG = "URL-CONFIG";

        public static final String URL_AMB_GAME = "urlAmbGame";
        public static final String URL_AMB_MOBILE_GAME = "urlMobileAmbGame";
        public static final String AMB_HASH = "ambHash";
    }

    public static List<String> AGENT_CONFIG_STATUS = Arrays.asList(LIMIT_WITHDRAW, APPROVE_WITHDRAW_AUTO, APPROVE_WITHDRAW_AUTO_NEW, ON_OFF_WEBSITE);

    public static class DEPOSIT_STATUS {
        public static final String PENDING = "PENDING";
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";

        public static final String NOT_SURE = "NOT_SURE";
        public static final String BLOCK_AUTO = "BLOCK_AUTO";
        public static final String REJECT = "REJECT";
        public static final String REJECT_N_REFUND = "REJECT_N_REFUND";
    }

    public static class WITHDRAW_STATUS {
        public static final String PENDING = "PENDING";
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";

        public static final String BLOCK_AUTO = "BLOCK_AUTO";
        public static final String REJECT = "REJECT";
        public static final String REJECT_N_REFUND = "REJECT_N_REFUND";

        public static final String SELF_TRANSFER = "SELF_TRANSFER";

    }


    public static class DEPOSIT_TYPE {
        public static final String BANK = "BANK";
        public static final String TRUEWALLET = "TRUEWALLET";
    }

    public static class POINT_TRANS_STATUS {
        public static final String PENDING = "PENDING";
        public static final String SUCCESS = "SUCCESS";
        public static final String ERROR = "ERROR";
    }


    public static class POINT_TYPE {
        public static final String EARN_POINT = "EARN_POINT";
        public static final String TRANS_CREDIT = "TRANS_CREDIT";
    }

    public static class PROBLEM {
        public static final String NO_SLIP = "NO_SLIP";
        public static final String CUT_CREDIT = "CUT_CREDIT";
        public static final String ADD_CREDIT = "ADD_CREDIT";
    }

    public static class PROMOTION_TYPE {
        public static final String ALLDAY = "ALLDAY";
        public static final String NEWUSER = "NEWUSER";
        public static final String FIRSTTIME = "FIRSTTIME";
        public static final String GOLDTIME = "GOLDTIME";
        public static final String SEVENDAYINROW = "7DAYINROW";
    }

    public static class AFFILIATE_TYPE {
        public static final String FIX = "FIX";
        public static final String PERCENT = "PERCENT";
    }

    public static final String MESSAGE_WITHDRAW_BLOCK = "User %s ขออนุมัติถอน credit จำนวน %s บาท";

    public static final String MESSAGE_WITHDRAW_APPROVE = "Admin %s อนุมัติถอน credit ให้ User %s จำนวน %s บาท";

    public static final String MESSAGE_WITHDRAW_REJECT = "Admin %s ปฏิเสธการถอนเงิน ของ User %s จำนวน %s บาท";

    public static final String MESSAGE_WITHDRAW_REJECT_RF = "Admin %s ปฏิเสธการถอนเงินแล้วคืน credit ให้ User %s จำนวน %s บาท";

    public static final String MESSAGE_WITHDRAW = "User %s ถอน credit จำนวน %s บาท";

    public static final String MESSAGE_WITHDRAW_ERROR = "User %s ถอน credit จำนวน %s บาท ไม่สำเร็จ : %s";

    public static final String MESSAGE_WITHDRAW_SELF = "Admin %s ถอนมือให้ User %s จำนวน %s บาท";

    public static final String MESSAGE_RE_WITHDRAW = "Admin %s ทำการถอนใหม่ให้ User %s จำนวน %s บาท";

    public static final String MESSAGE_RE_WITHDRAW_ERROR = "Admin %s ทำการถอนใหม่ให้ User %s จำนวน %s บาท ไม่สำเร็จ : %s";

    public static final String MESSAGE_WITHDRAW_REMAIN = " ยอดเงินคงเหลือ %s บาท";


    public static final String MESSAGE_DEPOSIT = "User %s เติม credit จำนวน %s บาท";

    public static final String MESSAGE_DEPOSIT_REJECT = "Admin %s ปฏิเสธการฝากเงิน ของ User %s จำนวน %s บาท";

    public static final String MESSAGE_DEPOSIT_REJECT_RF = "Admin %s ปฏิเสธการฝากเงินแล้วคืนเงินให้ User %s จำนวน %s บาท";

    public static final String MESSAGE_ADMIN_DEPOSIT = "Admin %s เติม credit ให้ User %s จำนวน %s บาท";

    public static final String MESSAGE_DEPOSIT_BLOCK = "User %s ขออนุมัติฝาก credit จำนวน %s บาท";

    public static final String MESSAGE_POINT_TRANSFER = "User %s โอน point %s";

    public static final String MESSAGE_NOTSURE_REJECT = "Admin %s ปฏิเสธการฝากเงิน จำนวน %s บาท";
}