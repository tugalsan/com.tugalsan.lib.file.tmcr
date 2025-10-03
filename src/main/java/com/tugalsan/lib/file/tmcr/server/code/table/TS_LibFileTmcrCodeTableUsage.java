package com.tugalsan.lib.file.tmcr.server.code.table;

public class TS_LibFileTmcrCodeTableUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLE()).append(" COLCOUNT\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLE()).append(" COLWIDTH0 COLWIDTH1 COLWIDTH2...\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeTableTags.CODE_END_TABLE()).append("\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLECELL()).append(" ROWSPAN/").append(TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL()).append(" COLSPAN/").append(TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL()).append(" HEIGHT/").append(TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL()).append("\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL()).append("\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeTableTags.CODE_TABLECELL_BORDER()).append(" NOBORDER/BORDER\n");
    }
}
