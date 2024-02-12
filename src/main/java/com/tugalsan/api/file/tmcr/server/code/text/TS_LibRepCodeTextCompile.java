package com.tugalsan.api.file.tmcr.server.code.text;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.sql.col.typed.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.sql.select.server.*;
import com.tugalsan.api.time.client.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;
import com.tugalsan.api.file.tmcr.server.code.parser.*;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_COLNAME;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_CREATE_DATE;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_CREATE_USER;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_CW;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_DATE;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_FUNCNAME;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_HR;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_HTML;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_NEWLINE;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_REVLST_DATE;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_REVLST_NO;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_REVLST_USER;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_SPC;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_TABNAME;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_TIME;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_USER;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL_REVERSE;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_ADD_TEXT_VAR_FROM_SQLQUERY;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_BEGIN_TEXT;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_END_TEXT;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_TOKEN_JUST;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_TOKEN_LEFT;
import static com.tugalsan.api.file.tmcr.server.code.text.TS_LibRepCodeTextTags.CODE_TOKEN_RIGHT;
import com.tugalsan.lib.rql.buffer.server.*;
import com.tugalsan.lib.rql.client.*;
import com.tugalsan.lib.rql.rev.server.*;
import java.util.*;

public class TS_LibRepCodeTextCompile {

    final private static TS_Log d = TS_Log.of(TS_LibRepCodeTextCompile.class);

    public static boolean is_BEGIN_TEXT(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_BEGIN_TEXT());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_BEGIN_TEXT(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_BEGIN_TEXT");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 2)) {
            return d.returnError(result, "token size not 2");
        }
        int allign_Left0_center1_right2_just3;
        var allignText = TGS_CharSetCast.toLocaleUpperCase(macroGlobals.macroLineTokens.get(1));
        if (Objects.equals(allignText, CODE_TOKEN_LEFT())) {
            allign_Left0_center1_right2_just3 = 0;
        } else if (Objects.equals(allignText, CODE_TOKEN_LEFT())) {
            allign_Left0_center1_right2_just3 = 1;
        } else if (Objects.equals(allignText, CODE_TOKEN_RIGHT())) {
            allign_Left0_center1_right2_just3 = 2;
        } else if (Objects.equals(allignText, CODE_TOKEN_JUST())) {
            allign_Left0_center1_right2_just3 = 3;
        } else {
//            d.ce(CODE_BEGIN_TEXT() + " ERROR: code token[1] error! -> SET Default as ", CODE_TOKEN_LEFT());
            allign_Left0_center1_right2_just3 = 0;
        }
        if (!macroGlobals.mifHandler.beginText(allign_Left0_center1_right2_just3)) {
            return d.returnError(result, "macroGlobals.mifHandler.beginText(allign_Left0_center1_right2_just3) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_END_TEXT(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_END_TEXT());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_END_TEXT(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_END_TEXT");
        if (!macroGlobals.mifHandler.endText()) {
            return d.returnError(result, "macroGlobals.mifHandler.endText() == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT() + " ") || macroGlobals.macroLineUpperCase.equals(CODE_ADD_TEXT());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT(TS_LibRepParser_Globals macroGlobals, boolean filenameMode) {
        var result = d.createFuncBoolean("compile_ADD_TEXT");
        var text = CODE_ADD_TEXT().length() == macroGlobals.macroLine.length() ? "" : macroGlobals.macroLine.substring(CODE_ADD_TEXT().length() + 1);
        if (filenameMode) {
            macroGlobals.prefferedFileNameLabel += text;
            return d.returnTrue(result);
        } else if (macroGlobals.mifHandler.addText(text)) {
            return d.returnTrue(result);
        } else {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
    }

    public static boolean is_ADD_TEXT_HTML(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_HTML());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_HTML(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_HTML");
        var urls = macroGlobals.macroLine.substring(CODE_ADD_TEXT_HTML().length() + 1).trim();
        d.ci("url detected as " + urls);
        var text = TS_UrlDownloadUtils.toText(TGS_Url.of(urls));
        if (text == null) {
            return d.returnError(result, "ERROR: htmlContent return null. check your internet connection!!!");
        }
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_CREATE_DATE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_CREATE_DATE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_CREATE_DATE(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_CREATE_DATE");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "token size not 3");
        }
        var targetTablename = macroGlobals.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return d.returnError(result, "ERROR: " + CODE_ADD_TEXT_CREATE_DATE() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (macroGlobals.macroLineTokens.get(2).equals(TS_LibRepParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = macroGlobals.selectedId;
            if (id == null) {
                return d.returnError(result, "ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(macroGlobals.macroLineTokens.get(2));
            if (id == null) {
                return d.returnError(result, "ERROR: " + CODE_ADD_TEXT_CREATE_DATE() + ".token[2] should be a number!");
            }
        }
        var createDate = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_CREATE_0(), TS_LibRqlRevRowUtils.PARAM_RET_DATE_0());
        var createDateLong = TGS_CastUtils.toLong(createDate);
        var text = createDateLong == null ? createDate : TGS_Time.toString_dateOnly(createDateLong);
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_CREATE_USER(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_CREATE_USER());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_CREATE_USER(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_CREATE_USER");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "ERROR: token size not 3");
        }
        var targetTablename = macroGlobals.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return d.returnError(result, CODE_ADD_TEXT_CREATE_USER() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (macroGlobals.macroLineTokens.get(2).equals(TS_LibRepParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = macroGlobals.selectedId;
            if (id == null) {
                return d.returnError(result, "ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(macroGlobals.macroLineTokens.get(2));
            if (id == null) {
                return d.returnError(result, CODE_ADD_TEXT_CREATE_USER() + ".token[2] should be a number!");
            }
        }
        var text = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_CREATE_0(), TS_LibRqlRevRowUtils.PARAM_RET_USER_2());
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_REVLST_DATE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_REVLST_DATE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_REVLST_DATE(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_REVLST_DATE");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "token size not 3");
        }
        var targetTablename = macroGlobals.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return d.returnError(result, CODE_ADD_TEXT_REVLST_DATE() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (macroGlobals.macroLineTokens.get(2).equals(TS_LibRepParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = macroGlobals.selectedId;
            if (id == null) {
                return d.returnError(result, "ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(macroGlobals.macroLineTokens.get(2));
            if (id == null) {
                return d.returnError(result, CODE_ADD_TEXT_REVLST_DATE() + ".token[2] should be a number!");
            }
        }
        var revDate = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_MODIFY_1(), TS_LibRqlRevRowUtils.PARAM_RET_DATE_0());
        var revDateLong = TGS_CastUtils.toLong(revDate);
        var text = revDateLong == null ? revDate : TGS_Time.toString_dateOnly(revDateLong);
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_REVLST_USER(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_REVLST_USER());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_REVLST_USER(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_REVLST_USER");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "token size not 3");
        }
        var targetTablename = macroGlobals.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return d.returnError(result, CODE_ADD_TEXT_REVLST_USER() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (macroGlobals.macroLineTokens.get(2).equals(TS_LibRepParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = macroGlobals.selectedId;
            if (id == null) {
                return d.returnError(result, "ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(macroGlobals.macroLineTokens.get(2));
            if (id == null) {
                return d.returnError(result, CODE_ADD_TEXT_REVLST_USER() + ".token[2] should be a number!");
            }
        }
        var text = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_MODIFY_1(), TS_LibRqlRevRowUtils.PARAM_RET_USER_2());
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_REVLST_NO(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_REVLST_NO());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_REVLST_NO(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_REVLST_NO");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "token size not 3");
        }
        var targetTablename = macroGlobals.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return d.returnError(result, CODE_ADD_TEXT_REVLST_NO() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (macroGlobals.macroLineTokens.get(2).equals(TS_LibRepParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = macroGlobals.selectedId;
            if (id == null) {
                return d.returnError(result, "ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(macroGlobals.macroLineTokens.get(2));
            if (id == null) {
                return d.returnError(result, CODE_ADD_TEXT_REVLST_USER() + ".token[2] should be a number!");
            }
        }
        var text = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_MODIFY_1(), TS_LibRqlRevRowUtils.PARAM_RET_COUNT_3());
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_USER(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_USER());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_USER(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_USER");
        var text = macroGlobals.username;
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_NEWLINE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_NEWLINE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_NEWLINE(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_NEWLINE");
        var text = "\n";
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_SPC(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_SPC());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_SPC(TS_LibRepParser_Globals macroGlobals, boolean filenameMode) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_SPC");
        var text = " ";
        if (filenameMode) {
            macroGlobals.prefferedFileNameLabel += " ";
            return d.returnTrue(result);
        } else if (macroGlobals.mifHandler.addText(text)) {
            return d.returnTrue(result);
        } else {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
    }

    public static boolean is_ADD_TEXT_TIME(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_TIME());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_TIME(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_TIME");
        var text = macroGlobals.now.toString_timeOnly_simplified();
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_DATE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_DATE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_DATE(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_DATE");
        var text = macroGlobals.now.toString_dateOnly();
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_HR(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_HR());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_HR(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_HR");
        if (!macroGlobals.mifHandler.addLineBreak()) {
            return d.returnError(result, "macroGlobals.mifHandler.addLineBreak() == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_FUNCNAME(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_FUNCNAME());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_FUNCNAME(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_FUNCNAME");
        var text = macroGlobals.funcName;
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_VAR_FROM_SQLQUERY(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_VAR_FROM_SQLQUERY());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_VAR_FROM_SQLQUERY(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_VAR_FROM_SQLQUERY");
        var sql = macroGlobals.macroLine.substring(CODE_ADD_TEXT_VAR_FROM_SQLQUERY().length() + 1);
        TGS_Tuple1<String> pack = new TGS_Tuple1();
        TS_SQLSelectStmtUtils.select(anchor, sql, rs -> pack.value0 = rs.str.get(0, 0));
        if (pack.value0 == null) {
            return d.returnError(result, "ERROR: text == null");
        }
        if (!macroGlobals.mifHandler.addText(pack.value0)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_CW(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_CW());
    }

    //ADD_TEXT_CW VAR ID
    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_CW(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_CW");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "token size not 3");
        }
        TGS_LibRqlTbl table;
        Integer colIdx;
        {
            var tableAndColname = TS_LibRepParser_Assure.splitTableDotColname(macroGlobals, 1);
            if (tableAndColname == null) {
                return d.returnError(result, "tableAndColname == null");
            }
            table = TS_LibRepParser_Assure.getTable(macroGlobals, tableAndColname[0]);
            if (table == null) {
                return d.returnError(result, "table == null");
            }
            colIdx = TS_LibRepParser_Assure.getColumnIndex(macroGlobals, table, tableAndColname[1]);
            if (colIdx == null) {
                return d.returnError(result, "colIdx == null");
            }
        }
        var column = table.columns.get(colIdx);
        var id = TS_LibRepParser_Assure.getId(macroGlobals, 2);
        if (id == null) {
            return d.returnError(result, "id == null");
        }
        d.ci("id returns as: ", id);

        var data = TS_LibRepParser_Assure.sniffCellSimple(anchor, macroGlobals, table, id, colIdx);
        if (data == null) {
            return d.returnError(result, "data == null");
        }
        d.ci("sniff returns as: ", data);

        d.ci("compile_ADD_TEXT_CW.detection.ok");
        if (!column.getType().equals(TGS_SQLColTypedUtils.TYPE_LNGDATE())) {
            return d.returnError(result, macroGlobals.macroLine + " -> CW column " + colIdx + " needs TK_GWTSQLColumnType.TYPE_LNGDATE but found: " + column.getType());
        }
        d.ci("compile_ADD_TEXT_CW.TYPE_LNGDATE.detected");
        var data_l = TGS_CastUtils.toLong(data);
        if (data_l == null) {
            return d.returnError(result, macroGlobals.macroLine + "CW data_l == null!!!");
        }
        d.ci("compile_ADD_TEXT_CW.data_l: ", data_l);
        var date = TGS_Time.ofDate(data_l);
        d.ci("compile_ADD_TEXT_CW.data_d: ", date.toString_dateOnly());
        var text = String.valueOf(date.getWeekNumber());
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_VAR_FROMSQL_or_REVERSE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_VAR_FROMSQL());
    }

    //ADD_TEXT_VAR_FROMSQL_REVERSE VAR ID
    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals, boolean filenameMode) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE");
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.macroline: [" + macroGlobals.macroLine + "]");

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.checkTokenSize");
        if (macroGlobals.macroLineTokens.size() == 2) {//if last value is empty
            if (macroGlobals.mifHandler.addText("EMPTY")) {
                return d.returnTrue(result);
            } else {
                return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
            }
        }

        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 3)) {
            return d.returnError(result, "token size not 3");
        }

        var nullTag = macroGlobals.macroLineTokens.get(2);
        if (TGS_CharSetCast.equalsLocaleIgnoreCase("null", nullTag)) {
            if (macroGlobals.mifHandler.addText("NULL")) {
                return d.returnTrue(result);
            } else {
                return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
            }
        }

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.parse#1");
        TGS_LibRqlTbl table;
        Integer colIdx;
        {
            var tableAndColname = TS_LibRepParser_Assure.splitTableDotColname(macroGlobals, 1);
            if (tableAndColname == null) {
                return d.returnError(result, "tableAndColname == null");
            }
            table = TS_LibRepParser_Assure.getTable(macroGlobals, tableAndColname[0]);
            if (table == null) {
                return d.returnError(result, "table == null");
            }
            colIdx = TS_LibRepParser_Assure.getColumnIndex(macroGlobals, table, tableAndColname[1]);
            if (colIdx == null) {
                return d.returnError(result, "colIdx == null");
            }
            d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.parse#1.colIdx:[" + colIdx + "]");
        }

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.getid...");
        var tn = table.nameSql;
        var column = table.columns.get(colIdx);
        var id = TS_LibRepParser_Assure.getId(macroGlobals, 2);
        if (id == null) {
            return d.returnError(result, "id == null");
        }
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.id returns as: " + id);

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.sniffCellSimple...");
        var data = TS_LibRepParser_Assure.sniffCellSimple(anchor, macroGlobals, table, id, colIdx);//FIX THAT THING IMMATDIATELY!!!
        if (data == null) {
            return d.returnError(result, "data == null");
        }
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.sniffCell returns as: " + data);

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.sniffCellVisible...");
        var visibleTextAndSubId = TS_LibRepParser_Assure.getVisibleTextAndSubId(anchor, macroGlobals, tn, column, data);
        if (visibleTextAndSubId == null) {
            return d.returnError(result, "visibleTextAndSubId == null");
        }
        var visibleText = visibleTextAndSubId[0];
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.visibleText: " + visibleText);

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.result...");
        String text;
        if (macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_VAR_FROMSQL_REVERSE())) {
            text = TS_LibRepParser_Assure.reverse(visibleText);
            d.ci(CODE_ADD_TEXT_VAR_FROMSQL_REVERSE() + " finned as " + text);
        } else {
            text = visibleText;
            d.ci(CODE_ADD_TEXT_VAR_FROMSQL() + " finned as " + text);
        }
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.result: " + text);
        if (filenameMode) {
            macroGlobals.prefferedFileNameLabel += text;
            return d.returnTrue(result);
        } else if (macroGlobals.mifHandler.addText(text)) {
            return d.returnTrue(result);
        } else {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
    }

    public static boolean is_ADD_TEXT_TABNAME(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_TABNAME());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_TABNAME(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_TABNAME");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 2)) {
            return d.returnError(result, "token size not 2");
        }
        var table = TS_LibRqlBufferUtils.get(macroGlobals.macroLineTokens.get(1));
        if (table == null) {
            return d.returnError(result, CODE_ADD_TEXT_TABNAME() + ".token[1] sniff table not worked as expected! table == null");
        }
        var text = table.nameReadable;
        if (!macroGlobals.mifHandler.addText(text)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_ADD_TEXT_COLNAME(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(CODE_ADD_TEXT_COLNAME());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_ADD_TEXT_COLNAME(TS_SQLConnAnchor anchor, TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_COLNAME");

        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 2)) {
            return d.returnError(result, "token size not 2");
        }
        TGS_LibRqlTbl table;
        Integer colIdx;
        {
            var tableAndColname = TS_LibRepParser_Assure.splitTableDotColname(macroGlobals, 1);
            if (tableAndColname == null) {
                return d.returnError(result, "tableAndColname == null");
            }
            table = TS_LibRepParser_Assure.getTable(macroGlobals, tableAndColname[0]);
            if (table == null) {
                return d.returnError(result, "table == null");
            }
            colIdx = TS_LibRepParser_Assure.getColumnIndex(macroGlobals, table, tableAndColname[1]);
            if (colIdx == null) {
                return d.returnError(result, "colIdx == null");
            }
        }

        var cnv = table.columns.get(colIdx).getColumnNameVisible();
        if (!macroGlobals.mifHandler.addText(cnv)) {
            return d.returnError(result, "macroGlobals.mifHandler.addText(text) == false");
        }
        return d.returnTrue(result);
    }
}
