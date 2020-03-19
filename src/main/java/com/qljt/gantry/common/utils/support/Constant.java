// Decompiled by Jad v1.5.8e2. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://kpdus.tripod.com/jad.html
// Decompiler options: packimports(3) fieldsfirst ansi space 
// Source File Name:   Constant.java

package com.qljt.gantry.common.utils.support;


public class Constant {
    //    public static String FILE_FRAME_ID = "";
    public static String IMG_UPLOAD_FILE = "/picture/";
    public static String LOG_UPLOAD_FILE = "/log/";
    public static String UPGRADE_UPLOAD_FILE = "/upgrade/";
    //获取设备状态数据 frameID前缀
    public static String HISTORY_FRAMEID = "xxxx-";
    //储存获取log日志的clientId的key
    public static String CACHE_LOG_KEY = "log";
    //储存获取实时状态的缓存过期时间 1分钟
    public static long CACHE_MONITOR_STATE_TIME = 60000;
    //储存获取log日志的clientId的缓存过期时间 5分钟
    public static long CACHE_LOG_TIME = 300000;
    //储存获取控制指令frameId-clientID的缓存过期时间 5分钟
    public static long CACHE_FRAME_ID_TIME = 300000;
    //储存获取实时状态的缓存过期时间 5秒
    public static Integer TURN_OFF_TIME = 5000;
    //重启时开启到关闭时间间隔后的响应时间 12秒
    public static Integer TURN_OFF_TIME_AFTER = 12000;
    //自定义数据权限 2
    public static Integer ROLE_DATA_SCOPE = 2;
    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "page";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "limit";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    public static final class ScheduleStatus {

        public static final ScheduleStatus NORMAL;
        public static final ScheduleStatus PAUSE;
        private int value;
        private static final ScheduleStatus $VALUES[];

        public static ScheduleStatus[] values() {
            return (ScheduleStatus[]) $VALUES.clone();
        }

        public int getValue() {
            return value;
        }

        static {
            NORMAL = new ScheduleStatus("NORMAL", 0, 0);
            PAUSE = new ScheduleStatus("PAUSE", 1, 1);
            $VALUES = (new ScheduleStatus[]{
                    NORMAL, PAUSE
            });
        }

        private ScheduleStatus(String s, int i, int value) {
            this.value = value;
        }
    }

    public static final class MenuType {

        public static final MenuType CATALOG;
        public static final MenuType MENU;
        public static final MenuType BUTTON;
        private int value;
        private static final MenuType $VALUES[];

        public static MenuType[] values() {
            return (MenuType[]) $VALUES.clone();
        }


        public int getValue() {
            return value;
        }

        static {
            CATALOG = new MenuType("CATALOG", 0, 0);
            MENU = new MenuType("MENU", 1, 1);
            BUTTON = new MenuType("BUTTON", 2, 2);
            $VALUES = (new MenuType[]{
                    CATALOG, MENU, BUTTON
            });
        }

        private MenuType(String s, int i, int value) {
            this.value = value;
        }
    }
}
