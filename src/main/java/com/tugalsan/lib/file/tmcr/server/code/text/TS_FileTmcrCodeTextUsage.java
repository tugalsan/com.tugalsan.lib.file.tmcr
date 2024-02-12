package com.tugalsan.lib.file.tmcr.server.code.text;

import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_COLNAME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_CREATE_DATE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_CREATE_USER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_CW;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_DATE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_FUNCNAME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_HR;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_HTML;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_NEWLINE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_DATE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_NO;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_USER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_SPC;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_TABNAME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_TIME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_USER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL_REVERSE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_BEGIN_TEXT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_END_TEXT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_TOKEN_CENTER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_TOKEN_JUST;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_TOKEN_LEFT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextTags.CODE_TOKEN_RIGHT;

public class TS_FileTmcrCodeTextUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_BEGIN_TEXT()).append(" ").append(CODE_TOKEN_LEFT()).append("/").append(CODE_TOKEN_RIGHT()).append("/").append(CODE_TOKEN_CENTER()).append("/").append(CODE_TOKEN_JUST()).append("\n");
        sb.append("//  ").append(CODE_END_TEXT()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_NEWLINE()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_SPC()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_CW()).append(" VAR ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_HTML()).append(" URL\n");
        sb.append("//  ").append(CODE_ADD_TEXT()).append(" TEXT\n");
        sb.append("//  ").append(CODE_ADD_TEXT_REVLST_NO()).append("TABLE ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_REVLST_DATE()).append("TABLE ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_REVLST_USER()).append("TABLE ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_CREATE_DATE()).append("TABLE ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_CREATE_USER()).append("TABLE ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_USER()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_DATE()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_TIME()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_HR()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_FUNCNAME()).append("\n");
        sb.append("//  ").append(CODE_ADD_TEXT_COLNAME()).append(" VAR\n");
        sb.append("//  ").append(CODE_ADD_TEXT_TABNAME()).append(" VAR\n");
        sb.append("//  ").append(CODE_ADD_TEXT_VAR_FROMSQL()).append(" VAR ID\n");
        sb.append("//  ").append(CODE_ADD_TEXT_VAR_FROMSQL_REVERSE()).append(" VAR ID\n");
    }
}
