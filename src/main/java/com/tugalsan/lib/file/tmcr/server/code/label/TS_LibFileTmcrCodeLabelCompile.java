package com.tugalsan.lib.file.tmcr.server.code.label;

import module com.tugalsan.api.cast;
import module com.tugalsan.api.charset;
import module com.tugalsan.api.log;
import module com.tugalsan.api.sql.conn;
import module com.tugalsan.api.string;
import module com.tugalsan.api.file.common;
import module com.tugalsan.lib.file.tmcr;
import module com.tugalsan.lib.rql;
import module com.tugalsan.lib.rql.txt;
import java.util.*;

public class TS_LibFileTmcrCodeLabelCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeLabelCompile.class);

    public static boolean is_SET_LABEL(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL());
    }

    public static boolean is_SET_LABEL_ON_SEARCH(TS_FileCommonConfig fileCommonConfig) {
        if (TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLine, TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL() + " " + fileCommonConfig.doFind_gotoLabel)) {
            d.ci("is_SET_LABEL_ON_SEARCH", TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL() + " " + fileCommonConfig.doFind_gotoLabel + " found");
            return true;
        } else {
            return false;
        }
    }

    public static boolean is_SET_LABEL_ON_ERROR(TS_FileCommonConfig fileCommonConfig) {
        if (TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLine, TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL() + " " + TS_LibFileTmcrCodeLabelTags.ERROR())) {
            d.ci("is_SET_LABEL_ON_ERROR", TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL() + " " + TS_LibFileTmcrCodeLabelTags.ERROR() + " found");
            return true;
        } else {
            return false;
        }
    }

    public static boolean is_GOTO_LABEL(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL());
    }

    public static String get_GOTO_LABEL(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig) {
        //FETCH IDX_0 cmd
        d.ci("get_GOTO_LABEL", "IDX_0", fileCommonConfig.macroLineTokens.get(0));
        String doFind_gotoLabel = null;

        //SIZE 2 COMPILE
        if (fileCommonConfig.macroLineTokens.size() == 2) {//i = 0; TODO IF NECESSARY
            doFind_gotoLabel = fileCommonConfig.macroLineTokens.get(1);
            d.ci(TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL(), "set as", doFind_gotoLabel);
            return doFind_gotoLabel;
        }

        //SIZE 6 OR 7 CHECK
        //GOTO_LABEL ORG_BOYA3 IF_VAL 0 EQUALS     fasongiris.LNGLINK_hamtedkart_LNG_ID_BOYA3    SELECTED_ID
        //GOTO_LABEL ORG_BOYA3 IF_VAL 0 NOT_EQUALS fasonmuskart.LNGLINK_hamtedkart_LNG_ID_BOYA3  MAPGET.0_CONVERTED
        //GOTO_LABEL STARTBOYAKUL IF_VAL 0 NOT_EQUALS MAPGET.0
        //    0          1       2    3   4            -------------- 5 ---------------------        6       
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, new int[]{6, 7})) {
            return TS_LibFileTmcrCodeLabelTags.ERROR();
        }

        //FETCH IDX_1 label
        //FETCH IDX_2 ifval
        var label = fileCommonConfig.macroLineTokens.get(1);
        var ifVal = fileCommonConfig.macroLineTokens.get(2);
        d.ci("get_GOTO_LABEL", "IDX_1", label);
        d.ci("get_GOTO_LABEL", "IDX_2", ifVal);
        if (!ifVal.equals(TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_IF_VAL())) {
            d.ce(TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
            d.ce("get_GOTO_LABEL", "ERROR: !parsedLineCodes.get(2).equals(CODE_IF_VAL) as '" + ifVal + "'");
            return TS_LibFileTmcrCodeLabelTags.ERROR();
        }

        //FETCH IDX_3 val
        //FETCH IDX_4 equalsText
        var val = fileCommonConfig.macroLineTokens.get(3);
        var equalsText = fileCommonConfig.macroLineTokens.get(4);
        d.ci("get_GOTO_LABEL", "IDX_3", val);
        d.ci("get_GOTO_LABEL", "IDX_4", equalsText);

        Boolean ifValEquals;
        if (Objects.equals(equalsText, TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_EQUALS())) {
            ifValEquals = true;
        } else if (Objects.equals(equalsText, TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_NOT_EQUALS())) {
            ifValEquals = false;
        } else {
            d.ce(TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
            d.ce("get_GOTO_LABEL", "ERROR: parsedLineCodes.get(4) not recognized", equalsText);
            return TS_LibFileTmcrCodeLabelTags.ERROR();
        }
        d.ci("get_GOTO_LABEL", "ifValEquals", ifValEquals);

        String data;
        if (fileCommonConfig.macroLineTokens.size() == 7) {
            d.ci("get_GOTO_LABEL", "sz7");
            //FETCH IDX_5 tableAndColname
            TGS_LibRqlTbl table;
            Integer tableColIdx;
            {
                d.ci("get_GOTO_LABEL", "sz7", "IDX_5", fileCommonConfig.macroLineTokens.get(5));
                var tableAndColname = TS_LibFileTmcrParser_Assure.splitTableDotColname(fileCommonConfig, 5);
                if (tableAndColname == null) {
                    return TS_LibFileTmcrCodeLabelTags.ERROR();
                }
                table = TS_LibFileTmcrParser_Assure.getTable(fileCommonConfig, tableAndColname.tableName());
                if (table == null) {
                    return TS_LibFileTmcrCodeLabelTags.ERROR();
                }
                tableColIdx = TS_LibFileTmcrParser_Assure.getColumnIndex(fileCommonConfig, table, tableAndColname.colname());
                if (tableColIdx == null) {
                    return TS_LibFileTmcrCodeLabelTags.ERROR();
                }
            }
            d.ci("get_GOTO_LABEL", "sz7", "table", table.nameSql);
            d.ci("get_GOTO_LABEL", "sz7", "tableColIdx", tableColIdx);

            //FETCH IDX_6 ids
            Long id;
            {
                var ids = fileCommonConfig.macroLineTokens.get(6);
                d.ci("get_GOTO_LABEL", "sz7", "IDX_6", ids);
                if (ids.equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
                    id = fileCommonConfig.selectedId;
                    if (id == null) {
                        d.ce(fileCommonConfig.macroLine + ".id requires you select a row from the table!");
                        d.ce(TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
                        d.ce("HATA: SATIR SEÇİLMEDİ HATASI", false);
                        return TS_LibFileTmcrCodeLabelTags.ERROR();
                    }
                } else {
                    id = TGS_CastUtils.toLong(ids).orElse(null);;
                    if (id == null) {
                        d.ce(fileCommonConfig.macroLine + ".id should be a number, is it still mapget???");
                        d.ce(TGS_StringUtils.cmn().toString_ln(fileCommonConfig.macroLineTokens));
                        return TS_LibFileTmcrCodeLabelTags.ERROR();
                    }
                }
            }
            d.ci("get_GOTO_LABEL", "sz7", "id", id);

            //FETCH data
            {
                data = TS_LibRqlTxtRowUtils.get(anchor, table, id, tableColIdx);
                if (data == null) {
                    d.ce(fileCommonConfig.macroLine + ".sniffCell() == null");
                    data = "ERROR: " + fileCommonConfig.macroLine + ".sniffCell() == null";
                }
            }
            d.ci("get_GOTO_LABEL", "sz7", "data", data);
        } else { //6
            d.ci("get_GOTO_LABEL", "sz6");
            data = fileCommonConfig.macroLineTokens.get(5);
            d.ci("get_GOTO_LABEL", "sz6", "data", data);
        }

        if ((ifValEquals && val.equals(data)) || (!ifValEquals && !val.equals(data))) {
//                        i = 0; TODO IF NECESSARY
            doFind_gotoLabel = label;
            d.ci("get_GOTO_LABEL", "get_GOTO_LABEL.doFind_gotoLabel,", label);
            d.ci("get_GOTO_LABEL", TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL() + " set as " + doFind_gotoLabel);
        } else {
            d.ci("get_GOTO_LABEL", TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL() + " skipped");
        }
        return doFind_gotoLabel;
    }
}
