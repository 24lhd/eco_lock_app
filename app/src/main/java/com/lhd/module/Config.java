package com.lhd.module;

/**
 * Created by D on 8/8/2017.
 */

public class Config {
    public static final String LOG_APP = "LOG_APP";
    public static final String IS_WELLCOME = "IS_WELLCOME";
    public static final String LAYOUT_FRAGMENT = "LAYOUT_FRAGMENT";
    public static final String INDEX_LAYOUT_FRAGMENT = "INDEX_LAYOUT_FRAGMENT";
    public static final String LOCKED_APP = "LOCKED_APP";
    public static final String ALL_APP = "ALL_APP";
    public static final String DB_NAME = "app";
    public static final String TABLE_LIST = "app_locked";
    public static final String CL_LIST_LOCKED = "package";
    public static final String CREATE_TABLE_LIST = "CREATE TABLE IF NOT EXISTS `app_locked` (\n" +
            "\t`stt`\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t`package`\tTEXT\n" +
            ");";
    public static final String NAME_PACKAGE_ICON = "NAME_PACKAGE_ICON";
    public static final String PIN_CODE = "PIN_CODE";
    public static final String IS_FINGERPRINT = "IS_FINGERPRINT";
    public static final String APP_UNLOCK = "APP_UNLOCK";
    public static final String IS_CHANGE_PIN = "IS_CHANGE_PIN";
    public static final String MY_APP_UNLOCK = "MY_APP_UNLOCK";
    public static final String IS_FIRST_SET_LOCK = "IS_FIRST_SET_LOCK";
}
