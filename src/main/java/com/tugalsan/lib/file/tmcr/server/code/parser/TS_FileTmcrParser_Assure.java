package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.lib.rql.buffer.server.*;
import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.math.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.string.server.*;
import com.tugalsan.api.time.client.*;
import com.tugalsan.lib.file.tmcr.server.file.TS_FileTmcrFileHandler;
import com.tugalsan.lib.rql.client.*;
import com.tugalsan.lib.rql.link.server.*;
import com.tugalsan.lib.rql.txt.server.*;
import java.util.stream.*;


public class TS_FileTmcrParser_Assure {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrParser_Assure.class);

    public static boolean checkTokenSize(TS_FileCommonBall macroGlobals, int tokenSize) {
        if (macroGlobals.macroLineTokens.size() != tokenSize) {
            d.ce("macroGlobals.macroLine: [", macroGlobals.macroLine, "] should have ", tokenSize, " tokens error!");
            d.ce(TGS_StringUtils.toString_ln(macroGlobals.macroLineTokens));
            return false;
        }
        return true;
    }

    public static boolean checkTokenSize(TS_FileCommonBall macroGlobals, int[] tokenSizes) {
        var macroTokenSize = macroGlobals.macroLineTokens.size();
        for (var tsi : tokenSizes) {
            if (macroTokenSize == tsi) {
                return true;
            }
        }
        d.ce("macroGlobals.macroLine: [", macroGlobals.macroLine, "] should have [", TGS_StringUtils.toString(tokenSizes, ","), "] tokens error!");
        d.ce(TGS_StringUtils.toString_ln(macroGlobals.macroLineTokens));
        return false;
    }

    public static TGS_LibRqlTbl getTable(TS_FileCommonBall macroGlobals, String tableName) {
        var table = TS_LibRqlBufferUtils.get(tableName);
        if (table == null) {
            d.ce(macroGlobals.macroLine, " -> tablename: [", tableName, "] sniff table not worked as expected!");
            return null;
        }
        return table;
    }

    public static String[] splitTableDotColname(TS_FileCommonBall macroGlobals, int tableDotColnameIdx) {
        var tableDotColname = macroGlobals.macroLineTokens.get(tableDotColnameIdx);
        var dividerIdx = tableDotColname.indexOf(".");
        if (dividerIdx == -1) {
            d.ce(macroGlobals.macroLine, ".token[", tableDotColnameIdx, "] = [", tableDotColname, "] should have . in it!");
            return null;
        }
        return new String[]{
            tableDotColname.substring(0, dividerIdx), //tableName
            tableDotColname.substring(dividerIdx + 1) //colname
        };
    }

    public static Integer getColumnIndex(TS_FileCommonBall macroGlobals, TGS_LibRqlTbl table, String colname) {
        var colIdx = TGS_LibRqlTblUtils.colIdx(table, colname);
        if (colIdx == -1) {
            var tn = table.nameSql;
            d.ce(macroGlobals.macroLine, " getColumnIdex table:", tn, ", colname:[", colname, "]");
        }
        return colIdx;
    }

    public static Long getId(TS_FileCommonBall macroGlobals, TS_FileTmcrFileHandler mifHandler, int idIdx) {
        Long id;
        var token = macroGlobals.macroLineTokens.get(idIdx);
        if (token.equals(TS_FileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = macroGlobals.selectedId;
            if (id == null) {
                var errText = TGS_StringUtils.concat("HATA: SATIR SEÇİLMEDİ HATASI! macroGlobals.macroLine: [", macroGlobals.macroLine, "].tokenAsId[", String.valueOf(idIdx), "]: [", token, "] requires you select a row from the table!");
                d.ce(errText, false);
                mifHandler.saveFile(errText);
            }
        } else {
            id = TGS_CastUtils.toLong(macroGlobals.macroLineTokens.get(idIdx));
            if (id == null) {
                var errText = TGS_StringUtils.concat("HATA: ID BULUNAMADI!macroGlobals.macroLine: [", macroGlobals.macroLine, "].tokenAsId[", String.valueOf(idIdx), "] should be a number!");
                d.ce(errText, false);
                mifHandler.saveFile(errText);
            }
        }
        return id;
    }

    public static String sniffCellSimple(TS_SQLConnAnchor anchor, TS_FileCommonBall macroGlobals, TGS_LibRqlTbl table, long id, int colIdx) {
        if (d.infoEnable) {
            var tn = table.nameSql;
            d.ci("sniffCellSimple.table:", tn);
            d.ci("sniffCellSimple.id:", id);
            d.ci("sniffCellSimple.colIdx:", colIdx);
        }
        var data = TS_LibRqlTxtRowUtils.get(anchor, table, id, colIdx);
        if (data == null && id == 0) {
            d.ci("sniffCellSimple.process.fixed[id==0 and sniffCell().getData() == null]");
            d.ci(macroGlobals.macroLine + " fixed for lng value 0");
            data = "0";
        } else if (data == null) {
            var tn = table.nameSql;
            d.ce("sniffCellSimple.anchor:", anchor.config.dbName);
            d.ce("sniffCellSimple.table:", tn);
            d.ce("sniffCellSimple.id:", id);
            d.ce("sniffCellSimple.colIdx:", colIdx);
            d.ce("sniffCellSimple.process.error:[data == null]");
            d.ce(macroGlobals.macroLine + ".sniffCell() == null");
            data = "HATA: linecode(" + macroGlobals.macroLine + ") -> sniffCellSimple.data==null";
        } else {
            d.ci("sniffCellSimple.process.ok");
        }
        d.ci("sniffCellSimple.data:" + data);
        return data;
    }

    public static String reverse(String text) {
        d.ci("reverse.start.as", text);
        text = TGS_Time.isDate(text) ? TGS_Time.reverseDate(text) : TGS_StringUtils.reverse(text);
        d.ci("reverse.end.as:", text);
        return text;
    }

    public static String[] getVisibleTextAndSubId(TS_SQLConnAnchor anchor, TS_FileCommonBall macroGlobals,
            CharSequence tableName, TGS_LibRqlCol column, String inputData) {
        String outputData;
        Long subId = null;

        d.ci("getVisibleTextAndSubId.getType:" + column.getType());
        var tc = TGS_LibRqlColUtils.toSqlCol(column);
        if (tc.typeLngTime()) {
            outputData = TGS_Time.toString_timeOnly(TGS_CastUtils.toLong(inputData));
            d.ci("getVisibleTextAndSubId.cast2Time:" + outputData);
        } else if (tc.typeLngDate()) {
            outputData = TGS_Time.toString_dateOnly(TGS_CastUtils.toLong(inputData));
            d.ci("getVisibleTextAndSubId.cast2Date:" + outputData);
        } else if (tc.typeLngDbl()) {
            var sb = new StringBuilder();
            outputData = String.valueOf(TGS_MathUtils.long2Double(TGS_CastUtils.toLong(inputData), column.getDataInt_STRFamilyMaxCharSize_or_LNGDOUBLEPrecision()));
            sb.append(outputData);
            var di = outputData.indexOf(".");
            if (di == -1) {
                sb.append(".");
                IntStream.range(0, column.getDataInt_STRFamilyMaxCharSize_or_LNGDOUBLEPrecision()).forEachOrdered(i -> sb.append("0"));
            } else {
                IntStream.range(outputData.substring(di + 1).length(), column.getDataInt_STRFamilyMaxCharSize_or_LNGDOUBLEPrecision()).forEachOrdered(i -> sb.append("0"));
            }
            outputData = sb.toString();
            d.ci("getVisibleTextAndSubId.cast2Double:" + outputData);
        } else if (tc.typeLngLnk()) {
            var datas = TS_StringUtils.toList_spc(inputData);
            if (datas == null) {
                var errorText = macroGlobals.macroLine + ".TK_GWTGeneralUtils.parseTokenGWT == null";
                d.ce("getVisibleTextAndSubId:", errorText);
                return null;
            } else if (datas.isEmpty()) {
                var errorText = macroGlobals.macroLine + ".TK_GWTGeneralUtils.parseTokenGWT.size == 0";
                d.ce("getVisibleTextAndSubId:", errorText);
                return null;
            }
            subId = TGS_CastUtils.toLong(datas.get(0));
            if (subId == null) {
                var errorText = macroGlobals.macroLine + ".subId == null";
                d.ce("getVisibleTextAndSubId:", errorText);
                return null;
            }
            {
                var ra = TS_LibRqlLinkUtils.get(TS_LibRqlBufferUtils.items, anchor, tableName, column.getDataString1_LnkTargetTableName(), subId);
                if (ra == null) {
                    var errorText = macroGlobals.macroLine + ".getLNGLINKText() == null";
                    d.ce("getVisibleTextAndSubId:", errorText);
                    return null;
                } else if (ra.errTxt != null) {
                    var errorText = macroGlobals.macroLine + ".getLNGLINKText().getErrorText(): " + ra.errTxt;
                    d.ce("getVisibleTextAndSubId:", errorText);
                    return null;
                }
                //TODO OK toString_targetTableSearchColumnValues-false
                outputData = ra.linkText;
            }
            d.ci("getVisibleTextAndSubId.cast2Link:" + outputData);
            d.ci("getVisibleTextAndSubId.withSubId:" + subId);
        } else {
            outputData = inputData;
        }
        return new String[]{
            outputData,
            String.valueOf(subId)
        };
    }

}
