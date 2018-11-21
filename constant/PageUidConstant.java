package com.engine.msgcenter.constant;

import java.util.UUID;

/*
 * @ Description：
 * @ Author     ：马宏伟
 * @ Date       ：Created in 2018/10/10 9:46
 */
public class PageUidConstant {

    public static final String MSG_LOG_PAGEUID = "b0712b0b-c635-4c91-9bc0-62745a96b550";
    public static final String MSG_SUB_LOG_PAGEUID = "1941456e-e674-46df-b6fa-faf0451e508a";

    public static final String MSG_SUB_CONFIG_PAGEUID = "038ba5ab-236d-4204-be14-64b8ae53eb4d";

    public static final String MSG_APP_MANAGE_PAGEUID = "e6497b8e-3e61-4a58-8995-b9596deaf06c";

    public static String DISABLE_EM = "n";

    public static String ABLE_EM = "y";


    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            UUID uuid = UUID.randomUUID();
            System.err.println(uuid);
        }
    }

}
