package com.tugalsan.api.file.tmcr.server.code.table;

import static com.tugalsan.api.file.tmcr.server.code.table.TS_FileTmcrCodeTableTags.CODE_BEGIN_TABLE;
import static com.tugalsan.api.file.tmcr.server.code.table.TS_FileTmcrCodeTableTags.CODE_BEGIN_TABLECELL;
import static com.tugalsan.api.file.tmcr.server.code.table.TS_FileTmcrCodeTableTags.CODE_END_TABLE;
import static com.tugalsan.api.file.tmcr.server.code.table.TS_FileTmcrCodeTableTags.CODE_END_TABLECELL;
import static com.tugalsan.api.file.tmcr.server.code.table.TS_FileTmcrCodeTableTags.CODE_TABLECELL_BORDER;
import static com.tugalsan.api.file.tmcr.server.code.table.TS_FileTmcrCodeTableTags.CODE_TOKEN_NULL;

public class TS_FileTmcrCodeTableUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_BEGIN_TABLE()).append(" COLCOUNT\n");
        sb.append("//  ").append(CODE_BEGIN_TABLE()).append(" COLWIDTH0 COLWIDTH1 COLWIDTH2...\n");
        sb.append("//  ").append(CODE_END_TABLE()).append("\n");
        sb.append("//  ").append(CODE_BEGIN_TABLECELL()).append(" ROWSPAN/").append(CODE_TOKEN_NULL()).append(" COLSPAN/").append(CODE_TOKEN_NULL()).append(" HEIGHT/").append(CODE_TOKEN_NULL()).append("\n");
        sb.append("//  ").append(CODE_END_TABLECELL()).append("\n");
        sb.append("//  ").append(CODE_TABLECELL_BORDER()).append(" NOBORDER/BORDER\n");
    }
}
