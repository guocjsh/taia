package io.github.guocjsh.excel.constant;

public final class Constant {
    /**
     * Excel自动刷新到磁盘的数量
     */
    public static final int DEFAULT_ROW_ACCESS_WINDOW_SIZE = 2000;
    /**
     * 分页条数
     */
    public static final int DEFAULT_PAGE_SIZE = 3000;
    /**
     * 分Sheet条数
     */
    public static final int DEFAULT_RECORD_COUNT_PEER_SHEET = 80000;
    /**
     * 是否开启自动适配宽度
     */
    public static final boolean OPEN_AUTO_COLUM_WIDTH = true;

    public static final String CELL = "c";
    public static final String XYZ_LOCATION = "r";
    public static final String CELL_T_PROPERTY = "t";
    public static final String CELL_S_VALUE = "s";
    public static final String ROW = "row";

    public static final int CHINESES_ATUO_SIZE_COLUMN_WIDTH_MAX = 60;
    public static final int CHINESES_ATUO_SIZE_COLUMN_WIDTH_MIN = 15;

    public static final int MAX_RECORD_COUNT_PEER_SHEET = 1000000;
}