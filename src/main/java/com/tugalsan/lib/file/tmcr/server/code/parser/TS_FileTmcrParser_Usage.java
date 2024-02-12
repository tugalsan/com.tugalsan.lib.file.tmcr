package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.lib.file.tmcr.server.code.font.TS_FileTmcrCodeFontUsage;
import com.tugalsan.lib.file.tmcr.server.code.filename.TS_FileTmcrCodeFileNameUsage;
import com.tugalsan.lib.rql.client.*;
import java.util.stream.*;

public class TS_FileTmcrParser_Usage {

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
            TS_FileTmcrCodeInjectUsage.addUsageCode(sb);
            TS_FileTmcrCodeMapUsage.addUsageCode(sb);
            TS_FileTmcrCodeLabelUsage.addUsageCode(sb);
            TS_FileTmcrCodePageUsage.addUsageCode(sb);
            TS_FileTmcrCodeFontUsage.addUsageCode(sb);
            TS_FileTmcrCodeTableUsage.addUsageCode(sb);
            TS_FileTmcrCodeImageUsage.addUsageCode(sb);
            TS_FileTmcrCodeTextUsage.addUsageCode(sb);
            TS_FileTmcrCodeFileNameUsage.addUsageCode(sb);
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
