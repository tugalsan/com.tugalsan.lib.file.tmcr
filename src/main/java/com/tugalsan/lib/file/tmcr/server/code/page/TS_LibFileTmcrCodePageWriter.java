package com.tugalsan.lib.file.tmcr.server.code.page;

import static com.tugalsan.lib.file.tmcr.server.code.page.TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_LibFileTmcrCodePageTags.CODE_TOKEN_LAND;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_LibFileTmcrCodePageTags.CODE_TOKEN_PORT;

public class TS_LibFileTmcrCodePageWriter {

    public static String INSERT_PAGE(int A_from_0_to_6, boolean isLandScape) {
        return INSERT_PAGE(A_from_0_to_6, isLandScape, 50, 10, 10, 10);
    }

    public static String INSERT_PAGE(int A_from_0_to_6, boolean isLandScape, int left_def50, int right_def10, int up_def10, int down_def10) {
        return CODE_INSERT_PAGE() + " A" + A_from_0_to_6 + " " + (isLandScape ? CODE_TOKEN_LAND() : CODE_TOKEN_PORT()) + " " + left_def50 + " " + right_def10 + " " + up_def10 + " " + down_def10 + "\n";
    }

}
