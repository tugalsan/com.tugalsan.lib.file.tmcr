package com.tugalsan.lib.file.tmcr.server.code.map;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.string.client.*;
import static com.tugalsan.lib.file.tmcr.server.code.map.TS_FileTmcrCodeMapTags.CODE_MAPADD_FROMSQL;
import static com.tugalsan.lib.file.tmcr.server.code.map.TS_FileTmcrCodeMapTags.CODE_MAPGET;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_SelectedId;
import com.tugalsan.lib.file.tmcr.server.file.TS_FileTmcrFileHandler;
import com.tugalsan.lib.rql.client.*;
import java.util.*;

public class TS_FileTmcrCodeMapCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeMapCompile.class);

    public static boolean is_MAPADD_FROMSQL(TS_FileCommonBall fileCommonBall) {
        return fileCommonBall.macroLineUpperCase.startsWith(CODE_MAPADD_FROMSQL());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_MAPGET(TS_FileCommonBall fileCommonBall) {
        var result = d.createFuncBoolean("compile_CODE_MAPGET");
        for (var j = fileCommonBall.macroLineTokens.size() - 1; j > -1; j--) {
            if (fileCommonBall.macroLineTokens.get(j).startsWith(CODE_MAPGET())) {
                String mapValue;
                if (!fileCommonBall.macroLineTokens.get(j).contains(".")) {
                    return d.returnError(result, "ERROR: " + fileCommonBall.macroLineTokens.get(j) + " code should have . error!");
                }
                mapValue = fileCommonBall.macroLineTokens.get(j).substring(CODE_MAPGET().length() + 1);
                Integer idx;
                if (Objects.equals(mapValue, TS_FileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
                    if (fileCommonBall.selectedId == null) {
                        return d.returnError(result, "ERROR: SATIR SEÇİLMEDİ HATASI!");
                    }
                    idx = fileCommonBall.selectedId.intValue();
                } else if (Objects.equals(mapValue, "LAST")) {
                    idx = fileCommonBall.mapVars.size() - 1;
                } else {
                    idx = TGS_CastUtils.toInteger(mapValue);
                    if (idx == null) {
                        return d.returnError(result, "ERROR: İKİNCİ TAKI BİR SAYI OLMALI HATASI!");
                    }
                }
                fileCommonBall.macroLineTokens.set(j, fileCommonBall.mapVars.get(idx));
                d.ci(result.value0 + ".change: " + TGS_StringUtils.toString_ln(fileCommonBall.macroLineTokens));
            }
        }
        var sb = new StringBuilder();
        fileCommonBall.macroLineTokens.stream().forEachOrdered(token -> sb.append(token).append(" "));
        fileCommonBall.macroLine = sb.toString().trim();
        fileCommonBall.macroLineUpperCase = TGS_CharSetCast.toLocaleUpperCase(fileCommonBall.macroLine);
        return d.returnTrue(result);
    }

    //MAPADD_FROMSQL VAR ID ...
    public static TGS_Tuple3<String, Boolean, String> compile_MAPADD_FROMSQL(TS_SQLConnAnchor anchor, TS_FileCommonBall fileCommonBall, TS_FileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_MAPADD_FROMSQL");
        if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonBall, 3)) {
            return d.returnError(result, "token size not 3");
        }
        TGS_LibRqlTbl table;
        Integer colIdx;
        {
            var tableAndColname = TS_FileTmcrParser_Assure.splitTableDotColname(fileCommonBall, 1);
            if (tableAndColname == null) {
                return d.returnError(result, "tableAndColname == null");
            }
            table = TS_FileTmcrParser_Assure.getTable(fileCommonBall, tableAndColname[0]);
            if (table == null) {
                return d.returnError(result, "table == null");
            }
            colIdx = TS_FileTmcrParser_Assure.getColumnIndex(fileCommonBall, table, tableAndColname[1]);
            if (colIdx == null) {
                return d.returnError(result, "colIdx == null");
            }
        }
        var tn = table.nameSql;
        var column = table.columns.get(colIdx);
        var id = TS_FileTmcrParser_Assure.getId(fileCommonBall, mifHandler, 2);
        if (id == null) {
            return d.returnError(result, "id == null");
        }
        d.ci("id returns as: ", id);

        var data = TS_FileTmcrParser_Assure.sniffCellSimple(anchor, fileCommonBall, table, id, colIdx);
        if (data == null) {
            return d.returnError(result, "data == null");
        }
        d.ci("sniff returns as: ", data);

        var visibleTextAndSubId = TS_FileTmcrParser_Assure.getVisibleTextAndSubId(anchor, fileCommonBall, tn, column, data);
        var visibleText = visibleTextAndSubId[0];
        var subId = visibleTextAndSubId[1];
        d.ci("visibleText.self: ", visibleText);
        d.ci("subId.self: " + subId);

        var resultText = subId.equals("null") ? visibleText : subId;
        d.ci(CODE_MAPADD_FROMSQL(), " finned as ", resultText);
        fileCommonBall.mapVars.add(resultText);
        return d.returnTrue(result);
    }
}
