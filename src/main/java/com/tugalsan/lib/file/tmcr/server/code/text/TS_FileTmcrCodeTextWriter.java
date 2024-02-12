package com.tugalsan.lib.file.tmcr.server.code.text;

import com.tugalsan.api.log.server.*;
import com.tugalsan.api.string.server.*;
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
import java.util.stream.*;

public class TS_FileTmcrCodeTextWriter {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeTextWriter.class);

    public static String END_TEXT() {
        return CODE_END_TEXT() + "\n";
    }

    public static String BEGIN_TEXT_LEFT() {
        return CODE_BEGIN_TEXT() + " " + CODE_TOKEN_LEFT() + "\n";
    }

    public static String BEGIN_TEXT_RIGHT() {
        return CODE_BEGIN_TEXT() + " " + CODE_TOKEN_RIGHT() + "\n";
    }

    public static String BEGIN_TEXT_CENTER() {
        return CODE_BEGIN_TEXT() + " " + CODE_TOKEN_CENTER() + "\n";
    }

    public static String BEGIN_TEXT_JUSTIFIED() {
        return CODE_BEGIN_TEXT() + " " + CODE_TOKEN_JUST() + "\n";
    }

    public static String ADD_TEXT(CharSequence text) {
        if (text == null){
            return "";
        }
        var lines = TS_StringUtils.toList(text, "\n");
        var sb = new StringBuilder();
        IntStream.range(0, lines.size()).forEachOrdered(i -> {
            if (i != 0) {
                sb.append(CODE_ADD_TEXT_NEWLINE()).append("\n");
            }
            sb.append(CODE_ADD_TEXT()).append(" ").append(lines.get(i)).append("\n");
        });
        return sb.toString();
    }

    public static String ADD_TEXT_THAN_NEWLINE(CharSequence text) {
        var lines = TS_StringUtils.toList(text, "\n");
        var sb = new StringBuilder();
        lines.stream().forEachOrdered(l -> {
            sb.append(CODE_ADD_TEXT()).append(" ").append(l).append("\n");
            sb.append(CODE_ADD_TEXT_NEWLINE()).append("\n");
        });
        return sb.toString();
    }

    public static String ADD_TEXT_NEWLINE() {
        return CODE_ADD_TEXT_NEWLINE() + "\n";
    }

    public static String ADD_TEXT_SPC() {
        return CODE_ADD_TEXT_SPC() + "\n";
    }

    public static String ADD_TEXT_HR() {
        return CODE_ADD_TEXT_HR() + "\n";
    }

    public static String ADD_TEXT_HTML(CharSequence url) {
        return CODE_ADD_TEXT_HTML() + " " + url + "\n";
    }

    public static String ADD_TEXT_DATE() {
        return CODE_ADD_TEXT_DATE() + "\n";
    }

    public static String ADD_TEXT_TIME() {
        return CODE_ADD_TEXT_TIME() + "\n";
    }

    public static String ADD_TEXT_CW() {
        return CODE_ADD_TEXT_CW() + "\n";
    }

    public static String ADD_TEXT_CREATE_USERTABLE(CharSequence table, CharSequence ID) {
        return CODE_ADD_TEXT_CREATE_USER() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_CREATE_DATETABLE(CharSequence table, CharSequence ID) {
        return CODE_ADD_TEXT_CREATE_DATE() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_REVLST_USERTABLE(CharSequence table, CharSequence ID) {
        return CODE_ADD_TEXT_REVLST_USER() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_REVLST_DATETABLE(CharSequence table, CharSequence ID) {
        return CODE_ADD_TEXT_REVLST_DATE() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_REVLST_NOTABLE(CharSequence table, CharSequence ID) {
        return CODE_ADD_TEXT_REVLST_NO() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_FUNCNAME() {
        return CODE_ADD_TEXT_FUNCNAME() + "\n";
    }

    public static String ADD_TEXT_USER() {
        return CODE_ADD_TEXT_USER() + "\n";
    }

    public static String ADD_TEXT_TABNAME(CharSequence VAR) {
        return CODE_ADD_TEXT_TABNAME() + " " + VAR + "\n";
    }

    public static String ADD_TEXT_COLNAME(CharSequence VAR) {
        return CODE_ADD_TEXT_COLNAME() + " " + VAR + "\n";
    }

    public static String ADD_TEXT_VAR_FROMSQL(CharSequence VAR, CharSequence ID) {
        d.ci("ADD_TEXT_VAR_FROMSQL VAR. [" + VAR + "], ID: [" + ID + "]");
        return CODE_ADD_TEXT_VAR_FROMSQL() + " " + VAR + " " + ID + "\n";
    }

    public static String ADD_TEXT_VAR_FROMSQL_REVERSE(CharSequence VAR, CharSequence ID) {
        return CODE_ADD_TEXT_VAR_FROMSQL_REVERSE() + " " + VAR + " " + ID + "\n";
    }
}
