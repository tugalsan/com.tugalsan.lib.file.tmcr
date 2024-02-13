package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.lib.file.tmcr.server.code.filename.TS_FileTmcrCodeFileNameCompile;
import com.tugalsan.api.file.common.server.TS_FileCommonFontTags;
import java.util.*;
import com.tugalsan.lib.boot.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.lib.file.tmcr.server.code.font.TS_FileTmcrCodeFontCompile;
import com.tugalsan.lib.file.tmcr.server.code.image.TS_FileTmcrCodeImageCompile;
import com.tugalsan.lib.file.tmcr.server.code.inject.TS_FileTmcrCodeInjectCompile;
import com.tugalsan.lib.file.tmcr.server.code.label.TS_FileTmcrCodeLabelCompile;
import com.tugalsan.lib.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags;
import com.tugalsan.lib.file.tmcr.server.code.map.TS_FileTmcrCodeMapCompile;
import com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageCompile;
import com.tugalsan.lib.file.tmcr.server.code.table.TS_FileTmcrCodeTableCompile;
import com.tugalsan.lib.file.tmcr.server.code.text.TS_FileTmcrCodeTextCompile;
import com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlCompile;
import com.tugalsan.lib.file.tmcr.server.file.TS_FileTmcrFileHandler;

public class TS_FileTmcrParser {

    final private static TS_Log d = TS_Log.of(false, TS_FileTmcrParser.class);

    public static int CLEAR_PERCENTAGES() {
        return -1;
    }

    public static void compileCode(TS_SQLConnAnchor anchor, TS_FileCommonBall macroGlobals, TS_FileTmcrFileHandler mifHandler, TGS_RunnableType2<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        var e = TGS_UnSafe.call(() -> {
            var cp = TS_LibBootUtils.pck;
            if (progressUpdate_with_userDotTable_and_percentage != null) {
                progressUpdate_with_userDotTable_and_percentage.run(macroGlobals.userDotTablename, CLEAR_PERCENTAGES());
                progressUpdate_with_userDotTable_and_percentage.run(macroGlobals.userDotTablename, 1);
            }
            d.ci("compileCode", "replacing...");
            for (var i = 0; i < macroGlobals.macroLines.size(); i++) {
                if (TS_FileTmcrCodeUrlCompile.is_CODE_URL_SH_OLD(macroGlobals, i)) {
                    var cmd = TS_FileTmcrCodeUrlCompile.compile_CODE_URL_SH_OLD(macroGlobals, i);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                }

                if (TS_FileTmcrCodeUrlCompile.is_CODE_URL_LOCALHOST(macroGlobals, i)) {
                    var cmd = TS_FileTmcrCodeUrlCompile.compile_CODE_URL_LOCALHOST(anchor, macroGlobals, i);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                }
            }

            d.ci("compileCode", "injecting...");
            var cmd = TS_FileTmcrCodeInjectCompile.compile_CODE_INJECT_CODE(macroGlobals);
            if (!cmd.value1) {
                mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                return null;
            }

            d.ci("compileCode", "compiling...");
            var filenameMode = false;
            for (var i = 0; i < macroGlobals.macroLines.size(); i++) {
                var percent = 100 * (i + 1) / macroGlobals.macroLines.size();
                if (progressUpdate_with_userDotTable_and_percentage != null) {
                    progressUpdate_with_userDotTable_and_percentage.run(macroGlobals.userDotTablename, percent);
                }
                d.ci("compileCode", "percent", percent);

                cmd = TS_FileTmcrParser_Tokenize.compile_TOKENIZE(macroGlobals, i);
                if (!cmd.value1) {
                    mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                    return null;
                }
                d.ci("compileCode", "macroLine", macroGlobals.macroLine);

                cmd = TS_FileTmcrCodeMapCompile.compile_CODE_MAPGET(macroGlobals);
                if (!cmd.value1) {
                    mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                    return null;
                } else {
                    d.ci("compileCode", "AFTER_MAPGET.macroLines[" + i + "/" + macroGlobals.macroLines.size() + "]-> [" + macroGlobals.macroLines.get(i) + "]");
                }

                if (TS_FileTmcrParser_WhiteSpace.is_WHITE_SPACE(macroGlobals)) {
                    d.ci("compileCode", "***  COMMENT SKIPPED");
                    continue;
                }

                if (TS_FileTmcrCodeLabelCompile.is_SET_LABEL_ON_ERROR(macroGlobals)) {//IF SPECIAL TAG: ERROR LABEL
                    d.ci("compileCode", "***  is_SET_LABEL ERROR found");
                    mifHandler.saveFile("is_SET_LABEL_ON_ERROR");
                    return null;
                }

                if (macroGlobals.doFind_gotoLabel == null) {//LABEL HANDLER
                    if (TS_FileTmcrCodeLabelCompile.is_GOTO_LABEL(macroGlobals)) {//SET LABEL
                        var s = TS_FileTmcrCodeLabelCompile.get_GOTO_LABEL(anchor, macroGlobals);
                        if (TS_FileTmcrCodeLabelTags.ERROR().equals(s)) {
                            d.ci("compileCode", "***  GOTO_LABEL DETECTED AS ERROR");
                            mifHandler.saveFile("error_get_GOTO_LABEL_see_console");
                            return null;
                        }
                        d.ci("compileCode", "***  GOTO_LABEL DETECTED as " + s);
                        macroGlobals.doFind_gotoLabel = s;
                        continue;
                    }
                    if (TS_FileTmcrCodeLabelCompile.is_SET_LABEL(macroGlobals)) {//NO SEARCH
                        d.ci("compileCode", "***  is_SET_LABEL SKIPPED because acroGlobals.doFind_gotoLabel == null");
                        continue;
                    }
                } else {//ON SEARCH
                    if (TS_FileTmcrCodeLabelCompile.is_SET_LABEL_ON_SEARCH(macroGlobals)) {
                        d.ci("compileCode", "***  is_SET_LABEL SKIPPED -> macroGlobals.doFind_gotoLabel set as null???");
                        macroGlobals.doFind_gotoLabel = null;//starts from the next macro line
                    }
                    continue;
                }

                if (TS_FileTmcrCodePageCompile.is_INSERT_PAGE(macroGlobals)) {//PAGE HANDLER
                    cmd = TS_FileTmcrCodePageCompile.compile_INSERT_PAGE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    } else {
                        macroGlobals.insertPageTriggeredBefore = true;
                        continue;
                    }
                }

                if (!macroGlobals.insertPageTriggeredBefore) {
                    TS_FileTmcrCodePageCompile.createNewPageDefault(macroGlobals, mifHandler);
                    mifHandler.saveFile("ERROR: First code should be new page! (try to download TMCR file to see macro code)");
                    return null;
                }

                if (TS_FileTmcrCodeImageCompile.is_INSERT_IMAGE(macroGlobals)) {
                    d.ci("compileCode", "is_INSERT_IMAGE");
                    cmd = TS_FileTmcrCodeImageCompile.compile_INSERT_IMAGE(macroGlobals, mifHandler, cp.dirDAT);
                    if (!cmd.value1) {
                        var fontColorBackup = macroGlobals.fontColor;
                        macroGlobals.fontColor = TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED();
                        mifHandler.beginText(0);
                        mifHandler.addText(cmd.value0 + "->" + cmd.value2);
                        mifHandler.endText();
                        macroGlobals.fontColor = fontColorBackup;
//                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
//                        return;
//                    } else {
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_BEGIN_TABLECELL(macroGlobals)) {
                    d.ci("compileCode", "is_BEGIN_TABLECELL");
                    cmd = TS_FileTmcrCodeTableCompile.compile_BEGIN_TABLECELL(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_END_TABLECELL(macroGlobals)) {
                    d.ci("compileCode", "is_END_TABLECELL");
                    cmd = TS_FileTmcrCodeTableCompile.compile_END_TABLECELL(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_TABLECELL_BORDER(macroGlobals)) {
                    d.ci("compileCode", "is_TABLECELL_BORDER");
                    cmd = TS_FileTmcrCodeTableCompile.compile_TABLECELL_BORDER(macroGlobals);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_BEGIN_TABLE(macroGlobals)) {
                    d.ci("compileCode", "is_BEGIN_TABLE");
                    cmd = TS_FileTmcrCodeTableCompile.compile_BEGIN_TABLE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_END_TABLE(macroGlobals)) {
                    d.ci("compileCode", "is_END_TABLE");
                    cmd = TS_FileTmcrCodeTableCompile.compile_END_TABLE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_BEGIN_TEXT(macroGlobals)) {
                    d.ci("compileCode", "is_BEGIN_TEXT");
                    cmd = TS_FileTmcrCodeTextCompile.compile_BEGIN_TEXT(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_END_TEXT(macroGlobals)) {
                    d.ci("compileCode", "is_END_TEXT");
                    cmd = TS_FileTmcrCodeTextCompile.compile_END_TEXT(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT(macroGlobals, mifHandler, filenameMode);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_HTML(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_HTML");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_HTML(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_CREATE_DATE(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_CREATE_DATE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_CREATE_DATE(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_CREATE_USER(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_CREATE_USER");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_CREATE_USER(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_DATE(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_DATE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_DATE(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_USER(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_USER");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_USER(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_NO(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_NO");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_NO(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_USER(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_USER");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_USER(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_NEWLINE(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_NEWLINE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_NEWLINE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_SPC(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_SPC");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_SPC(macroGlobals, mifHandler, filenameMode);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_TIME(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_TIME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_TIME(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_DATE(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_DATE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_DATE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_HR(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_HR");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_HR(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_FUNCNAME(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_FUNCNAME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_FUNCNAME(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_VAR_FROM_SQLQUERY(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_VAR_FROM_SQLQUERY");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_VAR_FROM_SQLQUERY(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_CW(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_CW");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_CW(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeMapCompile.is_MAPADD_FROMSQL(macroGlobals)) {
                    d.ci("compileCode", "is_MAPADD_FROMSQL");
                    cmd = TS_FileTmcrCodeMapCompile.compile_MAPADD_FROMSQL(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_VAR_FROMSQL_or_REVERSE(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_VAR_FROMSQL_or_REVERSE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE(anchor, macroGlobals, mifHandler, filenameMode);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_TABNAME(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_TABNAME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_TABNAME(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_COLNAME(macroGlobals)) {
                    d.ci("compileCode", "is_ADD_TEXT_COLNAME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_COLNAME(anchor, macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFontCompile.is_SET_FONT_COLOR(macroGlobals)) {
                    d.ci("compileCode", "is_SET_FONT_COLOR");
                    cmd = TS_FileTmcrCodeFontCompile.compile_SET_FONT_COLOR(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFontCompile.is_SET_FONT_SIZE(macroGlobals)) {
                    d.ci("compileCode", "is_SET_FONT_SIZE");
                    cmd = TS_FileTmcrCodeFontCompile.compile_SET_FONT_SIZE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFontCompile.is_SET_FONT_STYLE(macroGlobals)) {
                    d.ci("compileCode", "is_SET_FONT_STYLE");
                    cmd = TS_FileTmcrCodeFontCompile.compile_SET_FONT_STYLE(macroGlobals, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFileNameCompile.is_FILENAME_START(macroGlobals)) {
                    d.ci("compileCode", "is_FILENAME_START");
                    filenameMode = true;
                    continue;
                }
                if (TS_FileTmcrCodeFileNameCompile.is_FILENAME_END(macroGlobals)) {
                    d.ci("compileCode", "is_FILENAME_END");
                    filenameMode = false;
                    continue;
                }
                TGS_UnSafe.thrw(d.className, "compileCode", "BREAK: Unknown or unwritten code error! (check FILE TMCR): {" + macroGlobals.macroLine + "}");
            }
            d.ci("compileCode", "for.macroLines.done.");
            return null;
        }, ex -> ex);
        if (e != null) {
            compileCode_failed(macroGlobals, mifHandler, e, progressUpdate_with_userDotTable_and_percentage);
            return;
        }
        compileCode_completed(macroGlobals, mifHandler, progressUpdate_with_userDotTable_and_percentage);
    }

    private static void compileCode_completed(TS_FileCommonBall macroGlobals, TS_FileTmcrFileHandler mifHandler, TGS_RunnableType2<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        d.ci("compileCode_completed", "SAVE EXPORT FILES");
        if (progressUpdate_with_userDotTable_and_percentage != null) {
            progressUpdate_with_userDotTable_and_percentage.run(macroGlobals.userDotTablename, CLEAR_PERCENTAGES());
        }

        d.ci("compileCode_completed", "ADDING DEBUG FN FOOTER");
        if (d.infoEnable) {//DEBUG
            var fontColorBackup = macroGlobals.fontColor;
            macroGlobals.fontColor = TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY();
            mifHandler.setFontColor();
            mifHandler.beginText(0);
            //mifHandler.addText("DEBUG.prefferedFilename->[" + macroGlobals.prefferedFileNameLabel + "]");
            mifHandler.endText();
            macroGlobals.fontColor = fontColorBackup;
            mifHandler.setFontColor();
        }
//        mifHandler.mifHandler.saveFile(null);
        macroGlobals.runReport = true;
        d.ci("compileCode_completed", "FIN");
    }

    private static void compileCode_failed(TS_FileCommonBall macroGlobals, TS_FileTmcrFileHandler mifHandler, Exception e, TGS_RunnableType2<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        addMacro_Lines_ErrorText(macroGlobals, mifHandler, e);
        macroGlobals.runReport = true;
        if (progressUpdate_with_userDotTable_and_percentage != null) {
            progressUpdate_with_userDotTable_and_percentage.run(macroGlobals.userDotTablename, CLEAR_PERCENTAGES());
//                TS_LibRepSGEProgress.clearPercentages(macroGlobals.userDotTablename);
        }
        mifHandler.saveFile("compileCode_failed." + e.getMessage());
    }

    public static void addMacro_Lines_ErrorText(TS_FileCommonBall macroGlobals, TS_FileTmcrFileHandler mifHandler, String text) {
        d.ce("addMacro_Lines_ErrorText", "SKIP: Unknown or unwritten code error! (check FILE TMCR): {" + macroGlobals.macroLine + "}", false);
        mifHandler.beginText(0);
        mifHandler.addText(text);
        mifHandler.endText();
    }

    public static void addMacro_Lines_ErrorText(TS_FileCommonBall macroGlobals, TS_FileTmcrFileHandler mifHandler, Throwable t) {
        if (t == null) {
            d.ce("addMacro_Lines_ErrorText", "WHY T is null!!!");
        }
        mifHandler.beginText(0);
        mifHandler.addText(t.toString() + "\n");
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> mifHandler.addText(ste.toString() + "\n"));
        mifHandler.endText();
    }

}
