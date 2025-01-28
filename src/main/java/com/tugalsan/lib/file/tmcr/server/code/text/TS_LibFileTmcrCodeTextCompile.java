package com.tugalsan.lib.file.tmcr.server.code.text;

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
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_LibFileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.thread.server.sync.TS_ThreadSyncTrigger;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_LibFileTmcrParser_SelectedId;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_COLNAME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_CREATE_DATE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_CREATE_USER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_CW;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_DATE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_FUNCNAME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_HR;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_HTML;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_NEWLINE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_DATE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_NO;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_REVLST_USER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_SPC;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_TABNAME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_TIME;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_USER;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROMSQL_REVERSE;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_ADD_TEXT_VAR_FROM_SQLQUERY;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_BEGIN_TEXT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_END_TEXT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_TOKEN_JUST;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_TOKEN_LEFT;
import static com.tugalsan.lib.file.tmcr.server.code.text.TS_LibFileTmcrCodeTextTags.CODE_TOKEN_RIGHT;
import com.tugalsan.lib.file.tmcr.server.file.TS_LibFileTmcrFileHandler;
import com.tugalsan.lib.rql.buffer.server.*;
import com.tugalsan.lib.rql.client.*;
import com.tugalsan.lib.rql.rev.server.*;
import java.time.Duration;
import java.util.*;

public class TS_LibFileTmcrCodeTextCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeTextCompile.class);

    public static boolean is_BEGIN_TEXT(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_BEGIN_TEXT());
    }

    public static TS_Log.Result_withLog compile_BEGIN_TEXT(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_BEGIN_TEXT");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("token size not 2");
        }
        int allign_Left0_center1_right2_just3;
        var allignText = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(1));
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
        if (!mifHandler.beginText(allign_Left0_center1_right2_just3)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.beginText(allign_Left0_center1_right2_just3) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_END_TEXT(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_END_TEXT());
    }

    public static TS_Log.Result_withLog compile_END_TEXT(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_END_TEXT");
        if (!mifHandler.endText()) {
            return result.mutate2Error("fileCommonConfig.mifHandler.endText() == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT() + " ") || fileCommonConfig.macroLineUpperCase.equals(CODE_ADD_TEXT());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, boolean filenameMode) {
        var result = d.createFuncBoolean("compile_ADD_TEXT");
        var text = CODE_ADD_TEXT().length() == fileCommonConfig.macroLine.length() ? "" : fileCommonConfig.macroLine.substring(CODE_ADD_TEXT().length() + 1);
        if (filenameMode) {
            fileCommonConfig.prefferedFileNameLabel += text;
            return result.mutate2True();
        } else if (mifHandler.addText(text)) {
            return result.mutate2True();
        } else {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
    }

    public static boolean is_ADD_TEXT_HTML(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_HTML());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_HTML(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, Duration timeout) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_HTML");
        var urls = fileCommonConfig.macroLine.substring(CODE_ADD_TEXT_HTML().length() + 1).trim();
        d.ci("url detected as " + urls);
        var text = TS_UrlDownloadUtils.toText(TGS_Url.of(urls), timeout);
        if (text == null) {
            return result.mutate2Error("ERROR: htmlContent return null. check your internet connection!!!");
        }
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_CREATE_DATE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_CREATE_DATE());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_CREATE_DATE(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_CREATE_DATE");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("token size not 3");
        }
        var targetTablename = fileCommonConfig.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return result.mutate2Error("ERROR: " + CODE_ADD_TEXT_CREATE_DATE() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (fileCommonConfig.macroLineTokens.get(2).equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = fileCommonConfig.selectedId;
            if (id == null) {
                return result.mutate2Error("ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(2));
            if (id == null) {
                return result.mutate2Error("ERROR: " + CODE_ADD_TEXT_CREATE_DATE() + ".token[2] should be a number!");
            }
        }
        var createDate = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_CREATE_0(), TS_LibRqlRevRowUtils.PARAM_RET_DATE_0());
        var createDateLong = TGS_CastUtils.toLong(createDate);
        var text = createDateLong == null ? createDate : TGS_Time.toString_dateOnly(createDateLong);
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_CREATE_USER(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_CREATE_USER());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_CREATE_USER(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_CREATE_USER");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("ERROR: token size not 3");
        }
        var targetTablename = fileCommonConfig.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return result.mutate2Error(CODE_ADD_TEXT_CREATE_USER() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (fileCommonConfig.macroLineTokens.get(2).equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = fileCommonConfig.selectedId;
            if (id == null) {
                return result.mutate2Error("ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(2));
            if (id == null) {
                return result.mutate2Error(CODE_ADD_TEXT_CREATE_USER() + ".token[2] should be a number!");
            }
        }
        var text = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_CREATE_0(), TS_LibRqlRevRowUtils.PARAM_RET_USER_2());
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_REVLST_DATE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_REVLST_DATE());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_REVLST_DATE(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_REVLST_DATE");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("token size not 3");
        }
        var targetTablename = fileCommonConfig.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return result.mutate2Error(CODE_ADD_TEXT_REVLST_DATE() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (fileCommonConfig.macroLineTokens.get(2).equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = fileCommonConfig.selectedId;
            if (id == null) {
                return result.mutate2Error("ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(2));
            if (id == null) {
                return result.mutate2Error(CODE_ADD_TEXT_REVLST_DATE() + ".token[2] should be a number!");
            }
        }
        var revDate = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_MODIFY_1(), TS_LibRqlRevRowUtils.PARAM_RET_DATE_0());
        var revDateLong = TGS_CastUtils.toLong(revDate);
        var text = revDateLong == null ? revDate : TGS_Time.toString_dateOnly(revDateLong);
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_REVLST_USER(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_REVLST_USER());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_REVLST_USER(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_REVLST_USER");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("token size not 3");
        }
        var targetTablename = fileCommonConfig.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return result.mutate2Error(CODE_ADD_TEXT_REVLST_USER() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (fileCommonConfig.macroLineTokens.get(2).equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = fileCommonConfig.selectedId;
            if (id == null) {
                return result.mutate2Error("ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(2));
            if (id == null) {
                return result.mutate2Error(CODE_ADD_TEXT_REVLST_USER() + ".token[2] should be a number!");
            }
        }
        var text = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_MODIFY_1(), TS_LibRqlRevRowUtils.PARAM_RET_USER_2());
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_REVLST_NO(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_REVLST_NO());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_REVLST_NO(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_REVLST_NO");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("token size not 3");
        }
        var targetTablename = fileCommonConfig.macroLineTokens.get(1);
        if (!TS_LibRqlBufferUtils.exists(targetTablename)) {
            return result.mutate2Error(CODE_ADD_TEXT_REVLST_NO() + ".token[1] table not found error! " + targetTablename);
        }
        Long id;
        if (fileCommonConfig.macroLineTokens.get(2).equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
            id = fileCommonConfig.selectedId;
            if (id == null) {
                return result.mutate2Error("ERROR: SATIR SEÇİLMEDİ HATASI");
            }
        } else {
            id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(2));
            if (id == null) {
                return result.mutate2Error(CODE_ADD_TEXT_REVLST_NO() + ".token[2] should be a number!");
            }
        }
        var text = TS_LibRqlRevRowUtils.last(anchor, targetTablename, id, TS_LibRqlRevRowUtils.PARAM_ACT_MODIFY_1(), TS_LibRqlRevRowUtils.PARAM_RET_COUNT_3());
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_USER(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_USER());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_USER(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_USER");
        var text = fileCommonConfig.username;
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_NEWLINE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_NEWLINE());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_NEWLINE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_NEWLINE");
        var text = "\n";
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_SPC(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_SPC());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_SPC(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, boolean filenameMode) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_SPC");
        var text = " ";
        if (filenameMode) {
            fileCommonConfig.prefferedFileNameLabel += " ";
            return result.mutate2True();
        } else if (mifHandler.addText(text)) {
            return result.mutate2True();
        } else {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
    }

    public static boolean is_ADD_TEXT_TIME(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_TIME());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_TIME(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_TIME");
        var text = fileCommonConfig.now.toString_timeOnly_simplified();
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_DATE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_DATE());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_DATE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_DATE");
        var text = fileCommonConfig.now.toString_dateOnly();
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_HR(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_HR());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_HR(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_HR");
        if (!mifHandler.addLineBreak()) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addLineBreak() == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_FUNCNAME(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_FUNCNAME());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_FUNCNAME(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_FUNCNAME");
        var text = fileCommonConfig.funcName;
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_VAR_FROM_SQLQUERY(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_VAR_FROM_SQLQUERY());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_VAR_FROM_SQLQUERY(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_VAR_FROM_SQLQUERY");
        var sql = fileCommonConfig.macroLine.substring(CODE_ADD_TEXT_VAR_FROM_SQLQUERY().length() + 1);
        TGS_Tuple1<String> pack = new TGS_Tuple1();
        TS_SQLSelectStmtUtils.select(anchor, sql, rs -> pack.value0 = rs.str.get(0, 0));
        if (pack.value0 == null) {
            return result.mutate2Error("ERROR: text == null");
        }
        if (!mifHandler.addText(pack.value0)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_CW(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_CW());
    }

    //ADD_TEXT_CW VAR ID
    public static TS_Log.Result_withLog compile_ADD_TEXT_CW(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_CW");
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

        d.ci("compile_ADD_TEXT_CW.detection.ok");
        if (!column.getType().equals(TGS_SQLColTypedUtils.TYPE_LNGDATE())) {
            return result.mutate2Error(fileCommonConfig.macroLine + " -> CW column " + colIdx + " needs TK_GWTSQLColumnType.TYPE_LNGDATE but found: " + column.getType());
        }
        d.ci("compile_ADD_TEXT_CW.TYPE_LNGDATE.detected");
        var data_l = TGS_CastUtils.toLong(data);
        if (data_l == null) {
            return result.mutate2Error(fileCommonConfig.macroLine + "CW data_l == null!!!");
        }
        d.ci("compile_ADD_TEXT_CW.data_l: ", data_l);
        var date = TGS_Time.ofDate(data_l);
        d.ci("compile_ADD_TEXT_CW.data_d: ", date.toString_dateOnly());
        var text = String.valueOf(date.getWeekNumber().orElse(0));

        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_VAR_FROMSQL_or_REVERSE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_VAR_FROMSQL());
    }

    //ADD_TEXT_VAR_FROMSQL_REVERSE VAR ID
    public static TS_Log.Result_withLog compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE(TS_ThreadSyncTrigger servletKillThrigger, TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, boolean filenameMode, CharSequence defaultViewTableName) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE");
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.macroline: [" + fileCommonConfig.macroLine + "]");

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.checkTokenSize");
        if (fileCommonConfig.macroLineTokens.size() == 2) {//if last value is empty
            if (mifHandler.addText("EMPTY")) {
                return result.mutate2True();
            } else {
                return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
            }
        }

        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 3)) {
            return result.mutate2Error("token size not 3");
        }

        var nullTag = fileCommonConfig.macroLineTokens.get(2);
        if (TGS_CharSetCast.current().equalsIgnoreCase("null", nullTag)) {
            if (mifHandler.addText("NULL")) {
                return result.mutate2True();
            } else {
                return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
            }
        }

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.parse#1");
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
            d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.parse#1.colIdx:[" + colIdx + "]");
        }

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.getid...");
        var tn = table.nameSql;
        var column = table.columns.get(colIdx);
        var id = TS_LibFileTmcrParser_Assure.getId(fileCommonConfig, mifHandler, 2);
        if (id == null) {
            return result.mutate2Error("id == null");
        }
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.id returns as: " + id);

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.sniffCellSimple...");
        var data = TS_LibFileTmcrParser_Assure.sniffCellSimple(anchor, fileCommonConfig, table, id, colIdx);//FIX THAT THING IMMATDIATELY!!!
        if (data == null) {
            return result.mutate2Error("data == null");
        }
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.sniffCell returns as: " + data);

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.sniffCellVisible...");
        var visibleTextAndSubId = TS_LibFileTmcrParser_Assure.getVisibleTextAndSubId(servletKillThrigger, anchor, fileCommonConfig, tn, defaultViewTableName, column, data);
//        if (visibleTextAndSubId == null) {
//            return result.mutate2Error("visibleTextAndSubId == null");
//        }
        var visibleText = visibleTextAndSubId == null
                ? "ERROR_DATA_NOT_FOUND @" + d.className + "." + "compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE: 'visibleTextAndSubId == null'  W/ tn:" + tn + ", column:" + column + ", data:" + data
                : visibleTextAndSubId.visibleText();
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.visibleText: " + visibleText);

        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.result...");
        String text;
        if (fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_VAR_FROMSQL_REVERSE())) {
            text = TS_LibFileTmcrParser_Assure.reverse(visibleText);
            d.ci(CODE_ADD_TEXT_VAR_FROMSQL_REVERSE() + " finned as " + text);
        } else {
            text = visibleText;
            d.ci(CODE_ADD_TEXT_VAR_FROMSQL() + " finned as " + text);
        }
        d.ci("compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE.result: " + text);
        if (filenameMode) {
            fileCommonConfig.prefferedFileNameLabel += text;
            return result.mutate2True();
        } else if (mifHandler.addText(text)) {
            return result.mutate2True();
        } else {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
    }

    public static boolean is_ADD_TEXT_TABNAME(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_TABNAME());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_TABNAME(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_TABNAME");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("token size not 2");
        }
        var table = TS_LibRqlBufferUtils.get(fileCommonConfig.macroLineTokens.get(1));
        if (table == null) {
            return result.mutate2Error(CODE_ADD_TEXT_TABNAME() + ".token[1] sniff table not worked as expected! table == null");
        }
        var text = table.nameReadable;
        if (!mifHandler.addText(text)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }

    public static boolean is_ADD_TEXT_COLNAME(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(CODE_ADD_TEXT_COLNAME());
    }

    public static TS_Log.Result_withLog compile_ADD_TEXT_COLNAME(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_ADD_TEXT_COLNAME");

        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("token size not 2");
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

        var cnv = table.columns.get(colIdx).getColumnNameVisible();
        if (!mifHandler.addText(cnv)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.addText(text) == false");
        }
        return result.mutate2True();
    }
}
