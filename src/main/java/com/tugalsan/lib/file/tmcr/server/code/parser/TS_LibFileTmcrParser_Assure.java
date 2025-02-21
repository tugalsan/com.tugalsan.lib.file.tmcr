package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.lib.rql.buffer.server.*;
import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.math.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.api.time.client.*;
import com.tugalsan.lib.file.tmcr.server.file.TS_LibFileTmcrFileHandler;
import com.tugalsan.lib.rql.client.*;
import com.tugalsan.lib.rql.link.server.*;
import com.tugalsan.lib.rql.txt.server.*;
import java.util.stream.*;

public class TS_LibFileTmcrParser_Assure {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrParser_Assure.class);

    public static boolean checkTokenSize(TS_FileCommonConfig fileCommonConfig, int tokenSize) {
        if (fileCommonConfig.macroLineTokens.size() != tokenSize) {
            d.ce("fileCommonConfig.macroLine: [", fileCommonConfig.macroLine, "] should have ", tokenSize, " tokens error!");
            d.ce(TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
            return false;
        }
        return true;
    }

    public static boolean checkTokenSize(TS_FileCommonConfig fileCommonConfig, int[] tokenSizes) {
        var macroTokenSize = fileCommonConfig.macroLineTokens.size();
        for (var tsi : tokenSizes) {
            if (macroTokenSize == tsi) {
                return true;
            }
        }
        d.ce("fileCommonConfig.macroLine: [", fileCommonConfig.macroLine, "] should have [", TGS_StringUtils.cmn().toString(tokenSizes, ","), "] tokens error!");
        d.ce(TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
        return false;
    }

    public static TGS_LibRqlTbl getTable(TS_FileCommonConfig fileCommonConfig, String tableName) {
        var table = TS_LibRqlBufferUtils.get(tableName);
        if (table == null) {
            d.ce(fileCommonConfig.macroLine, " -> tablename: [", tableName, "] sniff table not worked as expected!");
            return null;
        }
        return table;
    }

    public static Result_splitTableDotColname splitTableDotColname(TS_FileCommonConfig fileCommonConfig, int tableDotColnameIdx) {
        var tableDotColname = fileCommonConfig.macroLineTokens.get(tableDotColnameIdx);
        var dividerIdx = tableDotColname.indexOf(".");
        if (dividerIdx == -1) {
            d.ce(fileCommonConfig.macroLine, ".token[", tableDotColnameIdx, "] = [", tableDotColname, "] should have . in it!");
            return null;
        }
        return new Result_splitTableDotColname(tableDotColname.substring(0, dividerIdx), tableDotColname.substring(dividerIdx + 1));
    }

    public static record Result_splitTableDotColname(String tableName, String colname) {

    }

    public static Integer getColumnIndex(TS_FileCommonConfig fileCommonConfig, TGS_LibRqlTbl table, String colname) {
        var colIdx = TGS_LibRqlTblUtils.colIdx(table, colname);
        if (colIdx == -1) {
            var tn = table.nameSql;
            d.ce(fileCommonConfig.macroLine, " getColumnIdex table:", tn, ", colname:[", colname, "]");
        }
        return colIdx;
    }

    public static Long getId(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, int idIdx) {
        Long id;
        var token = fileCommonConfig.macroLineTokens.get(idIdx);
        if (token.equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = fileCommonConfig.selectedId;
            if (id == null) {
                var errText = TGS_StringUtils.cmn().concat("HATA: SATIR SEÇİLMEDİ HATASI! fileCommonConfig.macroLine: [", fileCommonConfig.macroLine, "].tokenAsId[", String.valueOf(idIdx), "]: [", token, "] requires you select a row from the table!");
                d.ce(errText, false);
                mifHandler.saveFile(errText);
            }
        } else {
            id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(idIdx)).orElse(null);;
            if (id == null) {
                var errText = TGS_StringUtils.cmn().concat("HATA: ID BULUNAMADI!fileCommonConfig.macroLine: [", fileCommonConfig.macroLine, "].tokenAsId[", String.valueOf(idIdx), "] should be a number!");
                d.ce(errText, false);
                mifHandler.saveFile(errText);
            }
        }
        return id;
    }

    public static String sniffCellSimple(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TGS_LibRqlTbl table, long id, int colIdx) {
        if (d.infoEnable) {
            var tn = table.nameSql;
            d.ci("sniffCellSimple.table:", tn);
            d.ci("sniffCellSimple.id:", id);
            d.ci("sniffCellSimple.colIdx:", colIdx);
        }
        var data = TS_LibRqlTxtRowUtils.get(anchor, table, id, colIdx);
        if (data == null && id == 0) {
            d.ci("sniffCellSimple.process.fixed[id==0 and sniffCell().getData() == null]");
            d.ci(fileCommonConfig.macroLine + " fixed for lng value 0");
            data = "0";
        } else if (data == null) {
            var tn = table.nameSql;
            d.ce("sniffCellSimple.anchor:", anchor.config.dbName);
            d.ce("sniffCellSimple.table:", tn);
            d.ce("sniffCellSimple.id:", id);
            d.ce("sniffCellSimple.colIdx:", colIdx);
            d.ce("sniffCellSimple.process.error:[data == null]");
            d.ce(fileCommonConfig.macroLine + ".sniffCell() == null");
            data = "HATA: linecode(" + fileCommonConfig.macroLine + ") -> sniffCellSimple.data==null";
        } else {
            d.ci("sniffCellSimple.process.ok");
        }
        d.ci("sniffCellSimple.data:" + data);
        return data;
    }

    public static String reverse(String text) {
        d.ci("reverse.start.as", text);
        text = TGS_Time.isDate(text) ? TGS_Time.reverseDate(text) : TGS_StringUtils.cmn().reverse(text);
        d.ci("reverse.end.as:", text);
        return text;
    }

    public static Result_VisibleTextAndSubId getVisibleTextAndSubId(TS_ThreadSyncTrigger servletKillThrigger, TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig,
            CharSequence tableName, CharSequence defaultViewTableName, TGS_LibRqlCol column, String inputData) {
        String outputData;
        Long subId = null;

        d.ci("getVisibleTextAndSubId.getType:" + column.getType());
        var tc = TGS_LibRqlColUtils.toSqlCol(column);
        if (tc.typeLngTime()) {
            var inputDataLng = TGS_CastUtils.toLong(inputData).orElse(null);
            if (inputDataLng == null) {
                d.ci("getVisibleTextAndSubId.cast2Time.cannotConvertLng:" + inputData);
                outputData = inputData;
            } else {
                outputData = TGS_Time.toString_timeOnly_simplified(inputDataLng);
                d.ci("getVisibleTextAndSubId.cast2Time:" + outputData);
            }
        } else if (tc.typeLngDate()) {
            var inputDataLng = TGS_CastUtils.toLong(inputData).orElse(null);
            if (inputDataLng == null) {
                outputData = inputData;
                d.ci("getVisibleTextAndSubId.cast2Date.cannotConvertLng:" + inputData);
            } else {
                outputData = TGS_Time.toString_dateOnly(inputDataLng);
                d.ci("getVisibleTextAndSubId.cast2Date:" + outputData);
            }
        } else if (tc.typeLngDbl()) {
            var sb = new StringBuilder();
            var inputDataLng = TGS_CastUtils.toLong(inputData).orElse(null);
            if (inputDataLng == null) {
                d.ci("getVisibleTextAndSubId.cast2Double.cannotConvertLng:" + inputData);
                outputData = inputData;
            } else {
                outputData = String.valueOf(TGS_MathUtils.long2Double(inputDataLng, column.getDataInt_STRFamilyMaxCharSize_or_LNGDOUBLEPrecision()));
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
            }
        } else if (tc.typeLngLnk()) {
            var datas = TGS_StringUtils.jre().toList_spc(inputData);
            if (datas == null) {
                var errorText = fileCommonConfig.macroLine + ".TK_GWTGeneralUtils.parseTokenGWT == null";
                d.ce("getVisibleTextAndSubId:", errorText);
                return null;
            } else if (datas.isEmpty()) {
                var errorText = fileCommonConfig.macroLine + ".TK_GWTGeneralUtils.parseTokenGWT.size == 0";
                d.ce("getVisibleTextAndSubId:", errorText);
                return null;
            }
            subId = TGS_CastUtils.toLong(datas.get(0)).orElse(null);
            if (subId == null) {
                var errorText = fileCommonConfig.macroLine + ".subId == null";
                d.ce("getVisibleTextAndSubId:", errorText);
                return null;
            }
            {
                var ra = TS_LibRqlLinkUtils.get(servletKillThrigger, TS_LibRqlBufferUtils.items, anchor, tableName, defaultViewTableName, column.getDataString1_LnkTargetTableName(), subId);
                if (ra == null) {
                    var errorText = fileCommonConfig.macroLine + ".getLNGLINKText() == null";
                    d.ce("getVisibleTextAndSubId:", errorText);
                    return null;
                } else if (ra.errTxt != null) {
                    var errorText = fileCommonConfig.macroLine + ".getLNGLINKText().getErrorText(): " + ra.errTxt;
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
        return new Result_VisibleTextAndSubId(outputData, subId);
    }

    public static record Result_VisibleTextAndSubId(String visibleText, Long subId) {

    }
}
