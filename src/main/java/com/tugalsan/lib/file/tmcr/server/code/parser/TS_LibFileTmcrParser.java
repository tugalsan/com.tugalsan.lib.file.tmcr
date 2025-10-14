package com.tugalsan.lib.file.tmcr.server.code.parser;

import module com.tugalsan.api.file.common;
import module com.tugalsan.api.function;
import module com.tugalsan.api.log;
import module com.tugalsan.api.sql.conn;
import module com.tugalsan.api.thread;
import module com.tugalsan.lib.file.tmcr;
import java.time.*;
import java.util.*;

public class TS_LibFileTmcrParser {

    private TS_LibFileTmcrParser() {

    }

    final private static TS_Log d = TS_Log.of(false, TS_LibFileTmcrParser.class);

    public static int CLEAR_PERCENTAGES() {
        return -1;
    }

    public static void compileCode(TS_ThreadSyncTrigger servletKillThrigger, TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, TGS_FuncMTU_In2<String, Integer> progressUpdate_with_userDotTable_and_percentage, Duration timeout, CharSequence defaultViewTableName) {
        var e = TGS_FuncMTCUtils.call(() -> {
            if (progressUpdate_with_userDotTable_and_percentage != null) {
                progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, CLEAR_PERCENTAGES());
                progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, 1);
            }
            d.ci("compileCode", "replacing...");
            for (var i = 0; i < fileCommonConfig.macroLines.size(); i++) {
                if (servletKillThrigger.hasTriggered()) {
                    return null;
                }
                if (TS_LibFileTmcrCodeUrlCompile.is_CODE_URL_SH_OLD(fileCommonConfig, i)) {
                    var cmd = TS_LibFileTmcrCodeUrlCompile.compile_CODE_URL_SH_OLD(fileCommonConfig, i);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                }

                if (TS_LibFileTmcrCodeUrlCompile.is_CODE_URL_LOCALHOST(fileCommonConfig, i)) {
                    var cmd = TS_LibFileTmcrCodeUrlCompile.compile_CODE_URL_LOCALHOST(anchor, fileCommonConfig, i);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                }
            }
            if (servletKillThrigger.hasTriggered()) {
                return null;
            }

            d.ci("compileCode", "injecting...");
            var cmd = TS_LibFileTmcrCodeInjectCompile.compile_CODE_INJECT_CODE(fileCommonConfig, timeout);
            if (!cmd.result) {
                mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                return null;
            }
            if (servletKillThrigger.hasTriggered()) {
                return null;
            }

            var pageCopyIds_begin = new ArrayList();
            var pageCopyIds_loc = new ArrayList();
            var pageCopyIds_name = new ArrayList();
            var pageCopyIds_end = new ArrayList();
            for (var i = 0; i < fileCommonConfig.macroLines.size(); i++) {
                cmd = TS_LibFileTmcrParser_Tokenize.compile_TOKENIZE(fileCommonConfig, i);
                if (!cmd.result) {
                    mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                    return null;
                }
                d.ci("compileCode", "macroLine", fileCommonConfig.macroLine);
                if (TS_LibFileTmcrCodePageCompile.is_COPY_PAGE_BEGIN(fileCommonConfig)) {
                    cmd = TS_LibFileTmcrCodePageCompile.compile_COPY_PAGE_BEGIN(fileCommonConfig, mifHandler, pageCopyIds_begin, pageCopyIds_loc, pageCopyIds_name);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                }

                if (TS_LibFileTmcrCodePageCompile.is_COPY_PAGE_END(fileCommonConfig)) {
                    cmd = TS_LibFileTmcrCodePageCompile.compile_COPY_PAGE_END(fileCommonConfig, mifHandler, pageCopyIds_begin, pageCopyIds_end);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                }
            }
            if (pageCopyIds_begin.size() != pageCopyIds_end.size()) {
                mifHandler.saveFile(cmd.classNameDotfuncName + "->" + "pageCopyIds_begin.size() != pageCopyIds_end.size()");
                return null;
            }

            d.ci("compileCode", "compiling...");
            var filenameMode = false;
            for (var i = 0; i < fileCommonConfig.macroLines.size(); i++) {
                if (servletKillThrigger.hasTriggered()) {
                    return null;
                }
                var percent = 100 * (i + 1) / fileCommonConfig.macroLines.size();
                if (progressUpdate_with_userDotTable_and_percentage != null) {
                    progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, percent);
                }
                d.ci("compileCode", "percent", percent);

                cmd = TS_LibFileTmcrParser_Tokenize.compile_TOKENIZE(fileCommonConfig, i);
                if (!cmd.result) {
                    mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                    return null;
                }
                d.ci("compileCode", "macroLine", fileCommonConfig.macroLine);

                cmd = TS_LibFileTmcrCodeMapCompile.compile_CODE_MAPGET(fileCommonConfig);
                if (!cmd.result) {
                    mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                    return null;
                } else {
                    d.ci("compileCode", "AFTER_MAPGET.macroLines[" + i + "/" + fileCommonConfig.macroLines.size() + "]-> [" + fileCommonConfig.macroLines.get(i) + "]");
                }

                if (TS_LibFileTmcrParser_WhiteSpace.is_WHITE_SPACE(fileCommonConfig)) {
                    d.ci("compileCode", "***  COMMENT SKIPPED");
                    continue;
                }

                if (TS_LibFileTmcrCodeLabelCompile.is_SET_LABEL_ON_ERROR(fileCommonConfig)) {//IF SPECIAL TAG: ERROR LABEL
                    d.ci("compileCode", "***  is_SET_LABEL ERROR found");
                    mifHandler.saveFile("is_SET_LABEL_ON_ERROR");
                    return null;
                }

                if (fileCommonConfig.doFind_gotoLabel == null) {//LABEL HANDLER
                    if (TS_LibFileTmcrCodeLabelCompile.is_GOTO_LABEL(fileCommonConfig)) {//SET LABEL
                        var s = TS_LibFileTmcrCodeLabelCompile.get_GOTO_LABEL(anchor, fileCommonConfig);
                        if (TS_LibFileTmcrCodeLabelTags.ERROR().equals(s)) {
                            d.ci("compileCode", "***  GOTO_LABEL DETECTED AS ERROR");
                            mifHandler.saveFile("error_get_GOTO_LABEL_see_console");
                            return null;
                        }
                        d.ci("compileCode", "***  GOTO_LABEL DETECTED as " + s);
                        fileCommonConfig.doFind_gotoLabel = s;
                        continue;
                    }
                    if (TS_LibFileTmcrCodeLabelCompile.is_SET_LABEL(fileCommonConfig)) {//NO SEARCH
                        d.ci("compileCode", "***  is_SET_LABEL SKIPPED because acroGlobals.doFind_gotoLabel == null");
                        continue;
                    }
                } else {//ON SEARCH
                    if (TS_LibFileTmcrCodeLabelCompile.is_SET_LABEL_ON_SEARCH(fileCommonConfig)) {
                        d.ci("compileCode", "***  is_SET_LABEL SKIPPED -> fileCommonConfig.doFind_gotoLabel set as null???");
                        fileCommonConfig.doFind_gotoLabel = null;//starts from the next macro line
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodePageCompile.is_INSERT_PAGE(fileCommonConfig)) {//PAGE HANDLER
                    cmd = TS_LibFileTmcrCodePageCompile.compile_INSERT_PAGE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    } else {
                        fileCommonConfig.insertPageTriggeredBefore = true;
                        continue;
                    }
                }

                if (!fileCommonConfig.insertPageTriggeredBefore) {
                    TS_LibFileTmcrCodePageCompile.createNewPageDefault(fileCommonConfig, mifHandler);
                    mifHandler.saveFile("ERROR: First code should be new page! (try to download TMCR file to see macro code)");
                    return null;
                }

                if (TS_LibFileTmcrCodeImageCompile.is_INSERT_IMAGE(fileCommonConfig)) {
                    d.ci("compileCode", "is_INSERT_IMAGE");
                    cmd = TS_LibFileTmcrCodeImageCompile.compile_INSERT_IMAGE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        var fontColorBackup = fileCommonConfig.fontColor;
                        fileCommonConfig.fontColor = TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED();
                        mifHandler.beginText(0);
                        mifHandler.addText(cmd.classNameDotfuncName + "->" + cmd.log);
                        mifHandler.endText();
                        fileCommonConfig.fontColor = fontColorBackup;
//                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
//                        return;
//                    } else {
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTableCompile.is_BEGIN_TABLECELL(fileCommonConfig)) {
                    d.ci("compileCode", "is_BEGIN_TABLECELL");
                    cmd = TS_LibFileTmcrCodeTableCompile.compile_BEGIN_TABLECELL(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTableCompile.is_END_TABLECELL(fileCommonConfig)) {
                    d.ci("compileCode", "is_END_TABLECELL");
                    cmd = TS_LibFileTmcrCodeTableCompile.compile_END_TABLECELL(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTableCompile.is_TABLECELL_BORDER(fileCommonConfig)) {
                    d.ci("compileCode", "is_TABLECELL_BORDER");
                    cmd = TS_LibFileTmcrCodeTableCompile.compile_TABLECELL_BORDER(fileCommonConfig);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTableCompile.is_BEGIN_TABLE(fileCommonConfig)) {
                    d.ci("compileCode", "is_BEGIN_TABLE");
                    cmd = TS_LibFileTmcrCodeTableCompile.compile_BEGIN_TABLE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTableCompile.is_END_TABLE(fileCommonConfig)) {
                    d.ci("compileCode", "is_END_TABLE");
                    cmd = TS_LibFileTmcrCodeTableCompile.compile_END_TABLE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_BEGIN_TEXT(fileCommonConfig)) {
                    d.ci("compileCode", "is_BEGIN_TEXT");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_BEGIN_TEXT(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_END_TEXT(fileCommonConfig)) {
                    d.ci("compileCode", "is_END_TEXT");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_END_TEXT(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT(fileCommonConfig, mifHandler, filenameMode);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_HTML(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_HTML");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_HTML(fileCommonConfig, mifHandler, timeout);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_CREATE_DATE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_CREATE_DATE");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_CREATE_DATE(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_CREATE_USER(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_CREATE_USER");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_CREATE_USER(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_DATE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_DATE");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_DATE(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_USER(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_USER");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_USER(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_NO(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_NO");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_NO(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_USER(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_USER");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_USER(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_NEWLINE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_NEWLINE");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_NEWLINE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_SPC(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_SPC");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_SPC(fileCommonConfig, mifHandler, filenameMode);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_TIME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_TIME");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_TIME(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_DATE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_DATE");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_DATE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_HR(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_HR");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_HR(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_FUNCNAME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_FUNCNAME");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_FUNCNAME(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_VAR_FROM_SQLQUERY(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_VAR_FROM_SQLQUERY");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_VAR_FROM_SQLQUERY(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_CW(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_CW");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_CW(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeMapCompile.is_MAPADD_FROMSQL(fileCommonConfig)) {
                    d.ci("compileCode", "is_MAPADD_FROMSQL");
                    cmd = TS_LibFileTmcrCodeMapCompile.compile_MAPADD_FROMSQL(servletKillThrigger, anchor, fileCommonConfig, mifHandler, defaultViewTableName);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_VAR_FROMSQL_or_REVERSE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_VAR_FROMSQL_or_REVERSE");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE(servletKillThrigger, anchor, fileCommonConfig, mifHandler, filenameMode, defaultViewTableName);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_TABNAME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_TABNAME");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_TABNAME(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeTextCompile.is_ADD_TEXT_COLNAME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_COLNAME");
                    cmd = TS_LibFileTmcrCodeTextCompile.compile_ADD_TEXT_COLNAME(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeFontCompile.is_SET_FONT_COLOR(fileCommonConfig)) {
                    d.ci("compileCode", "is_SET_FONT_COLOR");
                    cmd = TS_LibFileTmcrCodeFontCompile.compile_SET_FONT_COLOR(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeFontCompile.is_SET_FONT_SIZE(fileCommonConfig)) {
                    d.ci("compileCode", "is_SET_FONT_SIZE");
                    cmd = TS_LibFileTmcrCodeFontCompile.compile_SET_FONT_SIZE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }

                if (TS_LibFileTmcrCodeFontCompile.is_SET_FONT_STYLE(fileCommonConfig)) {
                    d.ci("compileCode", "is_SET_FONT_STYLE");
                    cmd = TS_LibFileTmcrCodeFontCompile.compile_SET_FONT_STYLE(fileCommonConfig, mifHandler);
                    if (!cmd.result) {
                        mifHandler.saveFile(cmd.classNameDotfuncName + "->" + cmd.log);
                        return null;
                    }
                    continue;
                }
                
                if (TS_LibFileTmcrCodePageCompile.is_COPY_PAGE_BEGIN(fileCommonConfig)) {//WILL BE PROCESSED AFTER PDF CONSTRUCTION
                    continue;
                }
                if (TS_LibFileTmcrCodePageCompile.is_COPY_PAGE_END(fileCommonConfig)) {//WILL BE PROCESSED AFTER PDF CONSTRUCTION
                    continue;
                }

                if (TS_LibFileTmcrCodeFileNameCompile.is_FILENAME_START(fileCommonConfig)) {
                    d.ci("compileCode", "is_FILENAME_START");
                    filenameMode = true;
                    continue;
                }
                if (TS_LibFileTmcrCodeFileNameCompile.is_FILENAME_END(fileCommonConfig)) {
                    d.ci("compileCode", "is_FILENAME_END");
                    filenameMode = false;
                    continue;
                }
                TGS_FuncMTUUtils.thrw(d.className(), "compileCode", "BREAK: Unknown or unwritten code error! (check FILE TMCR): {" + fileCommonConfig.macroLine + "}");
            }
            d.ci("compileCode", "for.macroLines.done.");
            return null;
        }, ex -> ex);
        if (e != null) {
            compileCode_failed(fileCommonConfig, mifHandler, e, progressUpdate_with_userDotTable_and_percentage);
            return;
        }
        compileCode_completed(fileCommonConfig, progressUpdate_with_userDotTable_and_percentage);
    }

    private static void compileCode_completed(TS_FileCommonConfig fileCommonConfig, TGS_FuncMTU_In2<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        d.ci("compileCode_completed", "SAVE EXPORT FILES");
        if (progressUpdate_with_userDotTable_and_percentage != null) {
            progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, CLEAR_PERCENTAGES());
        }
        fileCommonConfig.runReport = true;
        d.ci("compileCode_completed", "FIN");
    }

    private static void compileCode_failed(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, Exception e, TGS_FuncMTU_In2<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        addMacro_Lines_ErrorText(fileCommonConfig, mifHandler, e);
        fileCommonConfig.runReport = true;
        if (progressUpdate_with_userDotTable_and_percentage != null) {
            progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, CLEAR_PERCENTAGES());
//                TS_LibRepSGEProgress.clearPercentages(fileCommonConfig.userDotTablename);
        }
        mifHandler.saveFile("compileCode_failed." + e.getMessage());
    }

    public static void addMacro_Lines_ErrorText(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, String text) {
        d.ce("addMacro_Lines_ErrorText", "SKIP: Unknown or unwritten code error! (check FILE TMCR): {" + fileCommonConfig.macroLine + "}", false);
        mifHandler.beginText(0);
        mifHandler.addText(text);
        mifHandler.endText();
    }

    public static void addMacro_Lines_ErrorText(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler, Throwable t) {
        if (t == null) {//I KNOW
            d.ce("addMacro_Lines_ErrorText", "WHY T is null!!!");
        }
        mifHandler.beginText(0);
        mifHandler.addText(t.toString() + "\n");
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> mifHandler.addText(ste.toString() + "\n"));
        mifHandler.endText();
    }

}
