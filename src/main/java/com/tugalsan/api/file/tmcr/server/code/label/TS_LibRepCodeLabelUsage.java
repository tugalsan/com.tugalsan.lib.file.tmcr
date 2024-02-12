package com.tugalsan.api.file.tmcr.server.code.label;

import static com.tugalsan.api.file.tmcr.server.code.label.TS_LibRepCodeLabelTags.CODE_GOTO_LABEL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_LibRepCodeLabelTags.CODE_SET_LABEL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_LibRepCodeLabelTags.CODE_TOKEN_IF_VAL;

public class TS_LibRepCodeLabelUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_GOTO_LABEL()).append(" label\n");
        sb.append("//  ").append(CODE_GOTO_LABEL()).append(" label ").append(CODE_TOKEN_IF_VAL()).append(" NOT_EQUALS/EQUALS table.colName id\n");
        sb.append("//  ").append(CODE_SET_LABEL()).append(" label\n");
    }
}
