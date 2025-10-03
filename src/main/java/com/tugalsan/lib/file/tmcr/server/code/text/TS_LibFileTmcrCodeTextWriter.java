package com.tugalsan.lib.file.tmcr.server.code.text;

import module com.tugalsan.api.log;
import module com.tugalsan.api.string;
import java.util.stream.*;

public class TS_LibFileTmcrCodeTextWriter {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeTextWriter.class);

    public static String END_TEXT() {
        return TS_LibFileTmcrCodeTextTags.CODE_END_TEXT() + "\n";
    }

    public static String BEGIN_TEXT_LEFT() {
        return TS_LibFileTmcrCodeTextTags.CODE_BEGIN_TEXT() + " " + TS_LibFileTmcrCodeTextTags.CODE_TOKEN_LEFT() + "\n";
    }

    public static String BEGIN_TEXT_RIGHT() {
        return TS_LibFileTmcrCodeTextTags.CODE_BEGIN_TEXT() + " " + TS_LibFileTmcrCodeTextTags.CODE_TOKEN_RIGHT() + "\n";
    }

    public static String BEGIN_TEXT_CENTER() {
        return TS_LibFileTmcrCodeTextTags.CODE_BEGIN_TEXT() + " " + TS_LibFileTmcrCodeTextTags.CODE_TOKEN_CENTER() + "\n";
    }

    public static String BEGIN_TEXT_JUSTIFIED() {
        return TS_LibFileTmcrCodeTextTags.CODE_BEGIN_TEXT() + " " + TS_LibFileTmcrCodeTextTags.CODE_TOKEN_JUST() + "\n";
    }

    public static String ADD_TEXT(CharSequence text) {
        if (text == null){
            return "";
        }
        var lines = TGS_StringUtils.jre().toList(text, "\n");
        var sb = new StringBuilder();
        IntStream.range(0, lines.size()).forEachOrdered(i -> {
            if (i != 0) {
                sb.append(TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_NEWLINE()).append("\n");
            }
            sb.append(TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT()).append(" ").append(lines.get(i)).append("\n");
        });
        return sb.toString();
    }

    public static String ADD_TEXT_THAN_NEWLINE(CharSequence text) {
        var lines = TGS_StringUtils.jre().toList(text, "\n");
        var sb = new StringBuilder();
        lines.stream().forEachOrdered(l -> {
            sb.append(TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT()).append(" ").append(l).append("\n");
            sb.append(TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_NEWLINE()).append("\n");
        });
        return sb.toString();
    }

    public static String ADD_TEXT_NEWLINE() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_NEWLINE() + "\n";
    }

    public static String ADD_TEXT_SPC() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_SPC() + "\n";
    }

    public static String ADD_TEXT_HR() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_HR() + "\n";
    }

    public static String ADD_TEXT_HTML(CharSequence url) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_HTML() + " " + url + "\n";
    }

    public static String ADD_TEXT_DATE() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_DATE() + "\n";
    }

    public static String ADD_TEXT_TIME() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_TIME() + "\n";
    }

    public static String ADD_TEXT_CW() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_CW() + "\n";
    }

    public static String ADD_TEXT_CREATE_USERTABLE(CharSequence table, CharSequence ID) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_CREATE_USER() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_CREATE_DATETABLE(CharSequence table, CharSequence ID) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_CREATE_DATE() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_REVLST_USERTABLE(CharSequence table, CharSequence ID) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_USER() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_REVLST_DATETABLE(CharSequence table, CharSequence ID) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_DATE() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_REVLST_NOTABLE(CharSequence table, CharSequence ID) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_NO() + " " + table + " " + ID + "\n";
    }

    public static String ADD_TEXT_FUNCNAME() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_FUNCNAME() + "\n";
    }

    public static String ADD_TEXT_USER() {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_USER() + "\n";
    }

    public static String ADD_TEXT_TABNAME(CharSequence VAR) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_TABNAME() + " " + VAR + "\n";
    }

    public static String ADD_TEXT_COLNAME(CharSequence VAR) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_COLNAME() + " " + VAR + "\n";
    }

    public static String ADD_TEXT_VAR_FROMSQL(CharSequence VAR, CharSequence ID) {
        d.ci("ADD_TEXT_VAR_FROMSQL VAR. [" + VAR + "], ID: [" + ID + "]");
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL() + " " + VAR + " " + ID + "\n";
    }

    public static String ADD_TEXT_VAR_FROMSQL_REVERSE(CharSequence VAR, CharSequence ID) {
        return TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL_REVERSE() + " " + VAR + " " + ID + "\n";
    }
}
