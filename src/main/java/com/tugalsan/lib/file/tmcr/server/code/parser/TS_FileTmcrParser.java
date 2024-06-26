package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.callable.client.TGS_CallableType2_Run;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.lib.file.tmcr.server.code.filename.TS_FileTmcrCodeFileNameCompile;
import com.tugalsan.api.file.common.server.TS_FileCommonFontTags;
import java.util.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.unsafe.client.*;

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
import java.time.Duration;

public class TS_FileTmcrParser {

    final private static TS_Log d = TS_Log.of(false, TS_FileTmcrParser.class);

    public static int CLEAR_PERCENTAGES() {
        return -1;
    }

    public static void compileCode(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler, TGS_CallableType2_Run<String, Integer> progressUpdate_with_userDotTable_and_percentage, Duration timeout) {
        var e = TGS_UnSafe.call(() -> {
            if (progressUpdate_with_userDotTable_and_percentage != null) {
                progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, CLEAR_PERCENTAGES());
                progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, 1);
            }
            d.ci("compileCode", "replacing...");
            for (var i = 0; i < fileCommonConfig.macroLines.size(); i++) {
                if (TS_FileTmcrCodeUrlCompile.is_CODE_URL_SH_OLD(fileCommonConfig, i)) {
                    var cmd = TS_FileTmcrCodeUrlCompile.compile_CODE_URL_SH_OLD(fileCommonConfig, i);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                }

                if (TS_FileTmcrCodeUrlCompile.is_CODE_URL_LOCALHOST(fileCommonConfig, i)) {
                    var cmd = TS_FileTmcrCodeUrlCompile.compile_CODE_URL_LOCALHOST(anchor, fileCommonConfig, i);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                }
            }

            d.ci("compileCode", "injecting...");
            var cmd = TS_FileTmcrCodeInjectCompile.compile_CODE_INJECT_CODE(fileCommonConfig, timeout);
            if (!cmd.value1) {
                mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                return null;
            }

            d.ci("compileCode", "compiling...");
            var filenameMode = false;
            for (var i = 0; i < fileCommonConfig.macroLines.size(); i++) {
                var percent = 100 * (i + 1) / fileCommonConfig.macroLines.size();
                if (progressUpdate_with_userDotTable_and_percentage != null) {
                    progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, percent);
                }
                d.ci("compileCode", "percent", percent);

                cmd = TS_FileTmcrParser_Tokenize.compile_TOKENIZE(fileCommonConfig, i);
                if (!cmd.value1) {
                    mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                    return null;
                }
                d.ci("compileCode", "macroLine", fileCommonConfig.macroLine);

                cmd = TS_FileTmcrCodeMapCompile.compile_CODE_MAPGET(fileCommonConfig);
                if (!cmd.value1) {
                    mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                    return null;
                } else {
                    d.ci("compileCode", "AFTER_MAPGET.macroLines[" + i + "/" + fileCommonConfig.macroLines.size() + "]-> [" + fileCommonConfig.macroLines.get(i) + "]");
                }

                if (TS_FileTmcrParser_WhiteSpace.is_WHITE_SPACE(fileCommonConfig)) {
                    d.ci("compileCode", "***  COMMENT SKIPPED");
                    continue;
                }

                if (TS_FileTmcrCodeLabelCompile.is_SET_LABEL_ON_ERROR(fileCommonConfig)) {//IF SPECIAL TAG: ERROR LABEL
                    d.ci("compileCode", "***  is_SET_LABEL ERROR found");
                    mifHandler.saveFile("is_SET_LABEL_ON_ERROR");
                    return null;
                }

                if (fileCommonConfig.doFind_gotoLabel == null) {//LABEL HANDLER
                    if (TS_FileTmcrCodeLabelCompile.is_GOTO_LABEL(fileCommonConfig)) {//SET LABEL
                        var s = TS_FileTmcrCodeLabelCompile.get_GOTO_LABEL(anchor, fileCommonConfig);
                        if (TS_FileTmcrCodeLabelTags.ERROR().equals(s)) {
                            d.ci("compileCode", "***  GOTO_LABEL DETECTED AS ERROR");
                            mifHandler.saveFile("error_get_GOTO_LABEL_see_console");
                            return null;
                        }
                        d.ci("compileCode", "***  GOTO_LABEL DETECTED as " + s);
                        fileCommonConfig.doFind_gotoLabel = s;
                        continue;
                    }
                    if (TS_FileTmcrCodeLabelCompile.is_SET_LABEL(fileCommonConfig)) {//NO SEARCH
                        d.ci("compileCode", "***  is_SET_LABEL SKIPPED because acroGlobals.doFind_gotoLabel == null");
                        continue;
                    }
                } else {//ON SEARCH
                    if (TS_FileTmcrCodeLabelCompile.is_SET_LABEL_ON_SEARCH(fileCommonConfig)) {
                        d.ci("compileCode", "***  is_SET_LABEL SKIPPED -> fileCommonConfig.doFind_gotoLabel set as null???");
                        fileCommonConfig.doFind_gotoLabel = null;//starts from the next macro line
                    }
                    continue;
                }

                if (TS_FileTmcrCodePageCompile.is_INSERT_PAGE(fileCommonConfig)) {//PAGE HANDLER
                    cmd = TS_FileTmcrCodePageCompile.compile_INSERT_PAGE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    } else {
                        fileCommonConfig.insertPageTriggeredBefore = true;
                        continue;
                    }
                }

                if (!fileCommonConfig.insertPageTriggeredBefore) {
                    TS_FileTmcrCodePageCompile.createNewPageDefault(fileCommonConfig, mifHandler);
                    mifHandler.saveFile("ERROR: First code should be new page! (try to download TMCR file to see macro code)");
                    return null;
                }

                if (TS_FileTmcrCodeImageCompile.is_INSERT_IMAGE(fileCommonConfig)) {
                    d.ci("compileCode", "is_INSERT_IMAGE");
                    cmd = TS_FileTmcrCodeImageCompile.compile_INSERT_IMAGE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        var fontColorBackup = fileCommonConfig.fontColor;
                        fileCommonConfig.fontColor = TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED();
                        mifHandler.beginText(0);
                        mifHandler.addText(cmd.value0 + "->" + cmd.value2);
                        mifHandler.endText();
                        fileCommonConfig.fontColor = fontColorBackup;
//                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
//                        return;
//                    } else {
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_BEGIN_TABLECELL(fileCommonConfig)) {
                    d.ci("compileCode", "is_BEGIN_TABLECELL");
                    cmd = TS_FileTmcrCodeTableCompile.compile_BEGIN_TABLECELL(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_END_TABLECELL(fileCommonConfig)) {
                    d.ci("compileCode", "is_END_TABLECELL");
                    cmd = TS_FileTmcrCodeTableCompile.compile_END_TABLECELL(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_TABLECELL_BORDER(fileCommonConfig)) {
                    d.ci("compileCode", "is_TABLECELL_BORDER");
                    cmd = TS_FileTmcrCodeTableCompile.compile_TABLECELL_BORDER(fileCommonConfig);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_BEGIN_TABLE(fileCommonConfig)) {
                    d.ci("compileCode", "is_BEGIN_TABLE");
                    cmd = TS_FileTmcrCodeTableCompile.compile_BEGIN_TABLE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTableCompile.is_END_TABLE(fileCommonConfig)) {
                    d.ci("compileCode", "is_END_TABLE");
                    cmd = TS_FileTmcrCodeTableCompile.compile_END_TABLE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_BEGIN_TEXT(fileCommonConfig)) {
                    d.ci("compileCode", "is_BEGIN_TEXT");
                    cmd = TS_FileTmcrCodeTextCompile.compile_BEGIN_TEXT(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_END_TEXT(fileCommonConfig)) {
                    d.ci("compileCode", "is_END_TEXT");
                    cmd = TS_FileTmcrCodeTextCompile.compile_END_TEXT(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT(fileCommonConfig, mifHandler, filenameMode);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_HTML(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_HTML");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_HTML(fileCommonConfig, mifHandler, timeout);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_CREATE_DATE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_CREATE_DATE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_CREATE_DATE(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_CREATE_USER(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_CREATE_USER");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_CREATE_USER(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_DATE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_DATE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_DATE(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_USER(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_USER");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_USER(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_REVLST_NO(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_REVLST_NO");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_REVLST_NO(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_USER(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_USER");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_USER(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_NEWLINE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_NEWLINE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_NEWLINE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_SPC(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_SPC");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_SPC(fileCommonConfig, mifHandler, filenameMode);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_TIME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_TIME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_TIME(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_DATE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_DATE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_DATE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_HR(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_HR");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_HR(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_FUNCNAME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_FUNCNAME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_FUNCNAME(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_VAR_FROM_SQLQUERY(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_VAR_FROM_SQLQUERY");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_VAR_FROM_SQLQUERY(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_CW(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_CW");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_CW(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeMapCompile.is_MAPADD_FROMSQL(fileCommonConfig)) {
                    d.ci("compileCode", "is_MAPADD_FROMSQL");
                    cmd = TS_FileTmcrCodeMapCompile.compile_MAPADD_FROMSQL(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_VAR_FROMSQL_or_REVERSE(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_VAR_FROMSQL_or_REVERSE");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_VAR_FROMSQL_or_REVERSE(anchor, fileCommonConfig, mifHandler, filenameMode);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_TABNAME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_TABNAME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_TABNAME(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeTextCompile.is_ADD_TEXT_COLNAME(fileCommonConfig)) {
                    d.ci("compileCode", "is_ADD_TEXT_COLNAME");
                    cmd = TS_FileTmcrCodeTextCompile.compile_ADD_TEXT_COLNAME(anchor, fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFontCompile.is_SET_FONT_COLOR(fileCommonConfig)) {
                    d.ci("compileCode", "is_SET_FONT_COLOR");
                    cmd = TS_FileTmcrCodeFontCompile.compile_SET_FONT_COLOR(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFontCompile.is_SET_FONT_SIZE(fileCommonConfig)) {
                    d.ci("compileCode", "is_SET_FONT_SIZE");
                    cmd = TS_FileTmcrCodeFontCompile.compile_SET_FONT_SIZE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFontCompile.is_SET_FONT_STYLE(fileCommonConfig)) {
                    d.ci("compileCode", "is_SET_FONT_STYLE");
                    cmd = TS_FileTmcrCodeFontCompile.compile_SET_FONT_STYLE(fileCommonConfig, mifHandler);
                    if (!cmd.value1) {
                        mifHandler.saveFile(cmd.value0 + "->" + cmd.value2);
                        return null;
                    }
                    continue;
                }

                if (TS_FileTmcrCodeFileNameCompile.is_FILENAME_START(fileCommonConfig)) {
                    d.ci("compileCode", "is_FILENAME_START");
                    filenameMode = true;
                    continue;
                }
                if (TS_FileTmcrCodeFileNameCompile.is_FILENAME_END(fileCommonConfig)) {
                    d.ci("compileCode", "is_FILENAME_END");
                    filenameMode = false;
                    continue;
                }
                TGS_UnSafe.thrw(d.className, "compileCode", "BREAK: Unknown or unwritten code error! (check FILE TMCR): {" + fileCommonConfig.macroLine + "}");
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

    private static void compileCode_completed(TS_FileCommonConfig fileCommonConfig, TGS_CallableType2_Run<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        d.ci("compileCode_completed", "SAVE EXPORT FILES");
        if (progressUpdate_with_userDotTable_and_percentage != null) {
            progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, CLEAR_PERCENTAGES());
        }
        fileCommonConfig.runReport = true;
        d.ci("compileCode_completed", "FIN");
    }

    private static void compileCode_failed(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler, Exception e, TGS_CallableType2_Run<String, Integer> progressUpdate_with_userDotTable_and_percentage) {
        addMacro_Lines_ErrorText(fileCommonConfig, mifHandler, e);
        fileCommonConfig.runReport = true;
        if (progressUpdate_with_userDotTable_and_percentage != null) {
            progressUpdate_with_userDotTable_and_percentage.run(fileCommonConfig.userDotTablename, CLEAR_PERCENTAGES());
//                TS_LibRepSGEProgress.clearPercentages(fileCommonConfig.userDotTablename);
        }
        mifHandler.saveFile("compileCode_failed." + e.getMessage());
    }

    public static void addMacro_Lines_ErrorText(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler, String text) {
        d.ce("addMacro_Lines_ErrorText", "SKIP: Unknown or unwritten code error! (check FILE TMCR): {" + fileCommonConfig.macroLine + "}", false);
        mifHandler.beginText(0);
        mifHandler.addText(text);
        mifHandler.endText();
    }

    public static void addMacro_Lines_ErrorText(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler, Throwable t) {
        if (t == null) {
            d.ce("addMacro_Lines_ErrorText", "WHY T is null!!!");
        }
        mifHandler.beginText(0);
        mifHandler.addText(t.toString() + "\n");
        Arrays.stream(t.getStackTrace()).forEachOrdered(ste -> mifHandler.addText(ste.toString() + "\n"));
        mifHandler.endText();
    }

}
