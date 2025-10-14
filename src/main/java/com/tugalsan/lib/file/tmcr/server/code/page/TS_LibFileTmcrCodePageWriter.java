package com.tugalsan.lib.file.tmcr.server.code.page;

public class TS_LibFileTmcrCodePageWriter {

    public static String INSERT_PAGE(int A_from_0_to_6, boolean isLandScape) {
        return INSERT_PAGE(A_from_0_to_6, isLandScape, 50, 10, 10, 10);
    }

    public static String INSERT_PAGE(int A_from_0_to_6, boolean isLandScape, int left_def50, int right_def10, int up_def10, int down_def10) {
        return TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " A" + A_from_0_to_6 + " " + (isLandScape ? TS_LibFileTmcrCodePageTags.CODE_TOKEN_LAND() : TS_LibFileTmcrCodePageTags.CODE_TOKEN_PORT()) + " " + left_def50 + " " + right_def10 + " " + up_def10 + " " + down_def10 + "\n";
    }

    public static String COPY_PAGE_BEGIN(CharSequence id, CharSequence loc, CharSequence name) {
        return TS_LibFileTmcrCodePageTags.CODE_COPY_PAGE_BEGIN() + " " + id + " {" + loc.toString().replace(" ", "*") + "} {" + name.toString().replace(" ", "*") + "}\n";
    }

    public static String COPY_PAGE_END(CharSequence id) {
        return TS_LibFileTmcrCodePageTags.CODE_COPY_PAGE_END() + " " + id + "\n";
    }
}
