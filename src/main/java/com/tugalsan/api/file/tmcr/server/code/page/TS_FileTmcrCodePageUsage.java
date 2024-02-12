package com.tugalsan.api.file.tmcr.server.code.page;

import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_INSERT_PAGE;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A0;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A1;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A2;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A3;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A4;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A5;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A6;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_LAND;
import static com.tugalsan.api.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_PORT;

public class TS_FileTmcrCodePageUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_INSERT_PAGE()).append(" ").append(CODE_TOKEN_A0()).append("/").append(CODE_TOKEN_A1()).append("/").append(CODE_TOKEN_A2()).append("/").append(CODE_TOKEN_A3()).append("/").append(CODE_TOKEN_A4()).append("/").append(CODE_TOKEN_A5()).append("/").append(CODE_TOKEN_A6()).append(" ").append(CODE_TOKEN_PORT()).append("/").append(CODE_TOKEN_LAND()).append(" MARGINS[50 50 50 50]\n");
    }
}
