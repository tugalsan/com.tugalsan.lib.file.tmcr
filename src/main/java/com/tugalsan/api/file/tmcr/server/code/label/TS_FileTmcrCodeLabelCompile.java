package com.tugalsan.api.file.tmcr.server.code.label;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.string.client.*;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_GOTO_LABEL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_SET_LABEL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_TOKEN_EQUALS;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_TOKEN_IF_VAL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_TOKEN_NOT_EQUALS;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.ERROR;
import com.tugalsan.api.file.tmcr.server.code.parser.*;
import com.tugalsan.lib.rql.client.*;
import com.tugalsan.lib.rql.txt.server.*;
import java.util.*;

public class TS_FileTmcrCodeLabelCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeLabelCompile.class);

    public static boolean is_SET_LABEL(TS_FileTmcrParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_SET_LABEL());
    }

    public static boolean is_SET_LABEL_ON_SEARCH(TS_FileTmcrParser_Globals macroGlobals) {
        if (TGS_CharSetCast.equalsLocaleIgnoreCase(macroGlobals.macroLine, CODE_SET_LABEL() + " " + macroGlobals.doFind_gotoLabel)) {
            d.ci("is_SET_LABEL_ON_SEARCH", CODE_SET_LABEL() + " " + macroGlobals.doFind_gotoLabel + " found");
            return true;
        } else {
            return false;
        }
    }

    public static boolean is_SET_LABEL_ON_ERROR(TS_FileTmcrParser_Globals macroGlobals) {
        if (TGS_CharSetCast.equalsLocaleIgnoreCase(macroGlobals.macroLine, CODE_SET_LABEL() + " " + ERROR())) {
            d.ci("is_SET_LABEL_ON_ERROR", CODE_SET_LABEL() + " " + ERROR() + " found");
            return true;
        } else {
            return false;
        }
    }

    public static boolean is_GOTO_LABEL(TS_FileTmcrParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_GOTO_LABEL());
    }

    public static String get_GOTO_LABEL(TS_SQLConnAnchor anchor, TS_FileTmcrParser_Globals macroGlobals) {
        //FETCH IDX_0 cmd
        d.ci("get_GOTO_LABEL", "IDX_0", macroGlobals.macroLineTokens.get(0));
        String doFind_gotoLabel = null;

        //SIZE 2 COMPILE
        if (macroGlobals.macroLineTokens.size() == 2) {//i = 0; TODO IF NECESSARY
            doFind_gotoLabel = macroGlobals.macroLineTokens.get(1);
            d.ci(CODE_GOTO_LABEL(), "set as", doFind_gotoLabel);
            return doFind_gotoLabel;
        }

        //SIZE 6 OR 7 CHECK
        //GOTO_LABEL ORG_BOYA3 IF_VAL 0 EQUALS     fasongiris.LNGLINK_hamtedkart_LNG_ID_BOYA3    SELECTED_ID
        //GOTO_LABEL ORG_BOYA3 IF_VAL 0 NOT_EQUALS fasonmuskart.LNGLINK_hamtedkart_LNG_ID_BOYA3  MAPGET.0_CONVERTED
        //GOTO_LABEL STARTBOYAKUL IF_VAL 0 NOT_EQUALS MAPGET.0
        //    0          1       2    3   4            -------------- 5 ---------------------        6       
        if (!TS_FileTmcrParser_Assure.checkTokenSize(macroGlobals, new int[]{6, 7})) {
            return ERROR();
        }

        //FETCH IDX_1 label
        //FETCH IDX_2 ifval
        var label = macroGlobals.macroLineTokens.get(1);
        var ifVal = macroGlobals.macroLineTokens.get(2);
        d.ci("get_GOTO_LABEL", "IDX_1", label);
        d.ci("get_GOTO_LABEL", "IDX_2", ifVal);
        if (!ifVal.equals(CODE_TOKEN_IF_VAL())) {
            d.ce(TGS_StringUtils.toString_ln(macroGlobals.macroLineTokens));
            d.ce("get_GOTO_LABEL", "ERROR: !parsedLineCodes.get(2).equals(CODE_IF_VAL) as '" + ifVal + "'");
            return ERROR();
        }

        //FETCH IDX_3 val
        //FETCH IDX_4 equalsText
        var val = macroGlobals.macroLineTokens.get(3);
        var equalsText = macroGlobals.macroLineTokens.get(4);
        d.ci("get_GOTO_LABEL", "IDX_3", val);
        d.ci("get_GOTO_LABEL", "IDX_4", equalsText);

        Boolean ifValEquals;
        if (Objects.equals(equalsText, CODE_TOKEN_EQUALS())) {
            ifValEquals = true;
        } else if (Objects.equals(equalsText, CODE_TOKEN_NOT_EQUALS())) {
            ifValEquals = false;
        } else {
            d.ce(TGS_StringUtils.toString_ln(macroGlobals.macroLineTokens));
            d.ce("get_GOTO_LABEL", "ERROR: parsedLineCodes.get(4) not recognized", equalsText);
            return ERROR();
        }
        d.ci("get_GOTO_LABEL", "ifValEquals", ifValEquals);

        String data;
        if (macroGlobals.macroLineTokens.size() == 7) {
            d.ci("get_GOTO_LABEL", "sz7");
            //FETCH IDX_5 tableAndColname
            TGS_LibRqlTbl table;
            Integer tableColIdx;
            {
                d.ci("get_GOTO_LABEL", "sz7", "IDX_5", macroGlobals.macroLineTokens.get(5));
                var tableAndColname = TS_FileTmcrParser_Assure.splitTableDotColname(macroGlobals, 5);
                if (tableAndColname == null) {
                    return ERROR();
                }
                table = TS_FileTmcrParser_Assure.getTable(macroGlobals, tableAndColname[0]);
                if (table == null) {
                    return ERROR();
                }
                tableColIdx = TS_FileTmcrParser_Assure.getColumnIndex(macroGlobals, table, tableAndColname[1]);
                if (tableColIdx == null) {
                    return ERROR();
                }
            }
            d.ci("get_GOTO_LABEL", "sz7", "table", table.nameSql);
            d.ci("get_GOTO_LABEL", "sz7", "tableColIdx", tableColIdx);

            //FETCH IDX_6 ids
            Long id;
            {
                var ids = macroGlobals.macroLineTokens.get(6);
                d.ci("get_GOTO_LABEL", "sz7", "IDX_6", ids);
                if (ids.equals(TS_FileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
                    id = macroGlobals.selectedId;
                    if (id == null) {
                        d.ce(macroGlobals.macroLine + ".id requires you select a row from the table!");
                        d.ce(TGS_StringUtils.toString_ln(macroGlobals.macroLineTokens));
                        d.ce("HATA: SATIR SEÇİLMEDİ HATASI", false);
                        return ERROR();
                    }
                } else {
                    id = TGS_CastUtils.toLong(ids);
                    if (id == null) {
                        d.ce(macroGlobals.macroLine + ".id should be a number, is it still mapget???");
                        d.ce(TGS_StringUtils.toString_ln(macroGlobals.macroLineTokens));
                        return ERROR();
                    }
                }
            }
            d.ci("get_GOTO_LABEL", "sz7", "id", id);

            //FETCH data
            {
                data = TS_LibRqlTxtRowUtils.get(anchor, table, id, tableColIdx);
                if (data == null) {
                    d.ce(macroGlobals.macroLine + ".sniffCell() == null");
                    data = "ERROR: " + macroGlobals.macroLine + ".sniffCell() == null";
                }
            }
            d.ci("get_GOTO_LABEL", "sz7", "data", data);
        } else { //6
            d.ci("get_GOTO_LABEL", "sz6");
            data = macroGlobals.macroLineTokens.get(5);
            d.ci("get_GOTO_LABEL", "sz6", "data", data);
        }

        if ((ifValEquals && val.equals(data)) || (!ifValEquals && !val.equals(data))) {
//                        i = 0; TODO IF NECESSARY
            doFind_gotoLabel = label;
            d.ci("get_GOTO_LABEL", "get_GOTO_LABEL.doFind_gotoLabel,", label);
            d.ci("get_GOTO_LABEL", CODE_GOTO_LABEL() + " set as " + doFind_gotoLabel);
        } else {
            d.ci("get_GOTO_LABEL", CODE_GOTO_LABEL() + " skipped");
        }
        return doFind_gotoLabel;
    }
}
