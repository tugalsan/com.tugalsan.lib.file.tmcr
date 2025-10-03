package com.tugalsan.lib.file.tmcr.server.code.label;

public class TS_LibFileTmcrCodeLabelUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL()).append(" label\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL()).append(" label ").append(TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_IF_VAL()).append(" NOT_EQUALS/EQUALS table.colName id\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL()).append(" label\n");
    }
}
