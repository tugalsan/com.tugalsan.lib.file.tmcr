package com.tugalsan.lib.file.tmcr.server.code.map;

import module com.tugalsan.api.cast;
import module com.tugalsan.api.charset;
import module com.tugalsan.api.log;
import module com.tugalsan.api.sql.conn;
import module com.tugalsan.api.string;
import module com.tugalsan.api.file.common;
import module com.tugalsan.api.thread;
import module com.tugalsan.lib.file.tmcr;
import module com.tugalsan.lib.rql;
import java.util.*;

public class TS_LibFileTmcrCodeMapCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeMapCompile.class);

    public static boolean is_MAPADD_FROMSQL(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_LibFileTmcrCodeMapTags.CODE_MAPADD_FROMSQL());
    }

    public static TS_Log.Result_withLog compile_CODE_MAPGET(TS_FileCommonConfig fileCommonConfig) {
        var result = d.createFuncBoolean("compile_CODE_MAPGET");
        for (var j = fileCommonConfig.macroLineTokens.size() - 1; j > -1; j--) {
            if (fileCommonConfig.macroLineTokens.get(j).startsWith(TS_LibFileTmcrCodeMapTags.CODE_MAPGET())) {
                String mapValue;
                if (!fileCommonConfig.macroLineTokens.get(j).contains(".")) {
                    return result.mutate2Error("ERROR: " + fileCommonConfig.macroLineTokens.get(j) + " code should have . error!");
                }
                mapValue = fileCommonConfig.macroLineTokens.get(j).substring(TS_LibFileTmcrCodeMapTags.CODE_MAPGET().length() + 1);
                Integer idx;
                if (Objects.equals(mapValue, TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
                    if (fileCommonConfig.selectedId == null) {
                        return result.mutate2Error("ERROR: SATIR SEÇİLMEDİ HATASI!");
                    }
                    idx = fileCommonConfig.selectedId.intValue();
                } else if (Objects.equals(mapValue, "LAST")) {
                    idx = fileCommonConfig.mapVars.size() - 1;
                } else {
                    idx = TGS_CastUtils.toInteger(mapValue).orElse(null);
                    if (idx == null) {
                        return result.mutate2Error("ERROR: İKİNCİ TAKI BİR SAYI OLMALI HATASI!");
                    }
                }
                fileCommonConfig.macroLineTokens.set(j, fileCommonConfig.mapVars.get(idx));
                d.ci(result.classNameDotfuncName + ".change: " + TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
            }
        }
        var sb = new StringBuilder();
        fileCommonConfig.macroLineTokens.stream().forEachOrdered(token -> sb.append(token).append(" "));
        fileCommonConfig.macroLine = sb.toString().trim();
        fileCommonConfig.macroLineUpperCase = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLine);
        return result.mutate2True();
    }

    //MAPADD_FROMSQL VAR ID ...
    public static TS_Log.Result_withLog compile_MAPADD_FROMSQL(TS_ThreadSyncTrigger servletKillThrigger, TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, CharSequence defaultViewTableName) {
        var result = d.createFuncBoolean("compile_MAPADD_FROMSQL");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("token size not 3");
        }
        TGS_LibRqlTbl table;
        Integer colIdx;
        {
            var tableAndColname = TS_LibFileTmcrParser_Assure.splitTableDotColname(fileCommonConfig, 1);
            if (tableAndColname == null) {
                return result.mutate2Error("tableAndColname == null");
            }
            table = TS_LibFileTmcrParser_Assure.getTable(fileCommonConfig, tableAndColname.tableName());
            if (table == null) {
                return result.mutate2Error("table == null");
            }
            colIdx = TS_LibFileTmcrParser_Assure.getColumnIndex(fileCommonConfig, table, tableAndColname.colname());
            if (colIdx == null) {
                return result.mutate2Error("colIdx == null");
            }
        }
        var tn = table.nameSql;
        var column = table.columns.get(colIdx);
        var id = TS_LibFileTmcrParser_Assure.getId(fileCommonConfig, mifHandler, 2);
        if (id == null) {
            return result.mutate2Error("id == null");
        }
        d.ci("id returns as: ", id);

        var data = TS_LibFileTmcrParser_Assure.sniffCellSimple(anchor, fileCommonConfig, table, id, colIdx);
        if (data == null) {
            return result.mutate2Error("data == null");
        }
        d.ci("sniff returns as: ", data);

        var visibleTextAndSubId = TS_LibFileTmcrParser_Assure.getVisibleTextAndSubId(servletKillThrigger, anchor, fileCommonConfig, tn, defaultViewTableName, column, data);
        d.ci("visibleText.self: ", visibleTextAndSubId.visibleText());
        d.ci("subId.self: " + visibleTextAndSubId.subId());

        var resultText = visibleTextAndSubId.subId() == null ? visibleTextAndSubId.visibleText() : visibleTextAndSubId.subId().toString();
        d.ci(TS_LibFileTmcrCodeMapTags.CODE_MAPADD_FROMSQL(), " finned as ", resultText);
        fileCommonConfig.mapVars.add(resultText);
        return result.mutate2True();
    }
}
