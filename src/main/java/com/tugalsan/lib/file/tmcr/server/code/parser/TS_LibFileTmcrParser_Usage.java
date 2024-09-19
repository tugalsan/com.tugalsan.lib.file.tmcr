package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.lib.file.tmcr.server.code.font.TS_LibFileTmcrCodeFontUsage;
import com.tugalsan.lib.file.tmcr.server.code.filename.TS_LibFileTmcrCodeFileNameUsage;
import com.tugalsan.lib.file.tmcr.server.code.image.TS_LibFileTmcrCodeImageUsage;
import com.tugalsan.lib.file.tmcr.server.code.inject.TS_LibFileTmcrCodeInjectUsage;
import com.tugalsan.lib.file.tmcr.server.code.label.TS_LibFileTmcrCodeLabelUsage;
import com.tugalsan.lib.file.tmcr.server.code.map.TS_LibFileTmcrCodeMapUsage;
import com.tugalsan.lib.file.tmcr.server.code.page.TS_LibFileTmcrCodePageUsage;
import com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableUsage;
import com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextUsage;
import com.tugalsan.lib.rql.client.*;
import java.util.stream.*;

public class TS_LibFileTmcrParser_Usage {

//    final public static String CODE_EXT () "EXT";
//    final public static String CODE_TOKEN_ADMIN () "ADMIN";
//    final public static String CODE_TOKEN_BYCONTENT () "BYCONTENT";
    public static String getUsageCode(TGS_LibRqlTbl curTable) {
        var tn = curTable.nameSql;
        var sb = new StringBuilder();
//        sb.append("//CODE PTABLE USAGE:\n");
//        sb.append("//" + CODE_PROGRAMTABLE_BEGIN + "TABLENAME\n");
//        sb.append("//" + CODE_PROGRAMTABLE_USER + " " + CODE_TOKEN_ADMIN + "\n");
//        sb.append("//" + CODE_PROGRAMTABLE_FILTER + " COLIDX " + CODE_TOKEN_BYCONTENT + "\n");
//        sb.append("//" + "\n");
        {
            sb.append("//CODE PDF USAGE:\n");
            TS_LibFileTmcrCodeInjectUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeMapUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeLabelUsage.addUsageCode(sb);
            TS_LibFileTmcrCodePageUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeFontUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeTableUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeImageUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeTextUsage.addUsageCode(sb);
            TS_LibFileTmcrCodeFileNameUsage.addUsageCode(sb);
        }
        sb.append("//\n");
        sb.append("//VARIABLE EXAMPLES:\n");
        sb.append("//  RQL/TEMPLATES/groupmesa.jpg\n");
        sb.append("//  SELECTED_ID\n");
        IntStream.range(0, curTable.columns.size()).forEachOrdered(i -> {
            var col = curTable.columns.get(i);
            sb.append("//  ").append(tn).append(".").append(col.getColumnName()).append("\n");
        });
        sb.append("//CODE:\n");
        return sb.toString();
    }
}
