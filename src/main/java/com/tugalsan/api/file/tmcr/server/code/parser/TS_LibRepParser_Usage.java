package com.tugalsan.api.file.tmcr.server.code.parser;

import com.tugalsan.api.file.tmcr.server.code.font.TS_LibRepCodeFontUsage;
import com.tugalsan.api.file.tmcr.server.code.filename.TS_LibRepCodeFileNameUsage;
import com.tugalsan.api.file.tmcr.server.code.image.*;
import com.tugalsan.api.file.tmcr.server.code.inject.*;
import com.tugalsan.api.file.tmcr.server.code.label.*;
import com.tugalsan.api.file.tmcr.server.code.map.*;
import com.tugalsan.api.file.tmcr.server.code.page.*;
import com.tugalsan.api.file.tmcr.server.code.table.*;
import com.tugalsan.api.file.tmcr.server.code.text.*;
import com.tugalsan.lib.rql.client.*;
import java.util.stream.*;

public class TS_LibRepParser_Usage {

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
            TS_LibRepCodeInjectUsage.addUsageCode(sb);
            TS_LibRepCodeMapUsage.addUsageCode(sb);
            TS_LibRepCodeLabelUsage.addUsageCode(sb);
            TS_LibRepCodePageUsage.addUsageCode(sb);
            TS_LibRepCodeFontUsage.addUsageCode(sb);
            TS_LibRepCodeTableUsage.addUsageCode(sb);
            TS_LibRepCodeImageUsage.addUsageCode(sb);
            TS_LibRepCodeTextUsage.addUsageCode(sb);
            TS_LibRepCodeFileNameUsage.addUsageCode(sb);
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
