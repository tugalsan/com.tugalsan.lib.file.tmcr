package com.tugalsan.lib.file.tmcr.server.code.table;

import com.tugalsan.api.function.client.TGS_FuncEffectivelyFinal;
import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.math.client.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.random.server.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_LibFileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLE;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLECELL;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_END_TABLE;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL_180;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL_270;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL_90;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_TABLECELL_BORDER;
import static com.tugalsan.lib.file.tmcr.server.code.table.TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL;
import com.tugalsan.lib.file.tmcr.server.file.TS_LibFileTmcrFileHandler;

public class TS_LibFileTmcrCodeTableCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeTableCompile.class);
    
    public static int DEFAULT_CELL_HEIGHT(){
        return 20;
    }

    public static boolean is_BEGIN_TABLE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(CODE_BEGIN_TABLE());
    }

    public static boolean is_END_TABLE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(CODE_END_TABLE());
    }

    public static boolean is_BEGIN_TABLECELL(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(CODE_BEGIN_TABLECELL());
    }

    public static boolean is_END_TABLECELL(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(CODE_END_TABLECELL());
    }

    public static boolean is_TABLECELL_BORDER(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(CODE_TABLECELL_BORDER());
    }

    public static TS_Log.Result_withLog compile_BEGIN_TABLECELL(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_BEGIN_TABLECELL");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 4)) {
            return result.mutate2Error("token size not 4");
        }
        Integer rowSpan;
        Integer colSpan;
        Integer cellHeight = null;
        if (TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLineTokens.get(1), CODE_TOKEN_NULL())) {
            rowSpan = 1;
        } else {
            rowSpan = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(1));
            if (rowSpan == null || rowSpan < 1) {
                return result.mutate2Error(CODE_BEGIN_TABLECELL() + " code token[1] error! rowSpan == null || rowSpan < 1");
            }
        }
        if (TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLineTokens.get(2), CODE_TOKEN_NULL())) {
            colSpan = 1;
        } else {
            colSpan = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(2));
            if (colSpan == null || colSpan < 1) {
                return result.mutate2Error(CODE_BEGIN_TABLECELL() + " code token[2] error! colSpan == null || colSpan < 1");
            }
        }
        if (!TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLineTokens.get(3), CODE_TOKEN_NULL())) {
            cellHeight = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(3));
            if (cellHeight == null || cellHeight < 1) {
                return result.mutate2Error(CODE_BEGIN_TABLECELL() + " code token[3] error! cellHeight == null || cellHeight < 1");
            }
        } else {
            cellHeight = DEFAULT_CELL_HEIGHT() * rowSpan;
        }
        fileCommonConfig.cellHeight = cellHeight;
        if (!mifHandler.beginTableCell(rowSpan, colSpan, cellHeight)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.beginTableCell(rowSpan, colSpan, cellHeight) == false");
        }
        return result.mutate2True();
    }

    public static TS_Log.Result_withLog compile_END_TABLECELL(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_END_TABLECELL");
        var rotate = TGS_FuncEffectivelyFinal.ofInt()
                .anoint(val -> 0)
                .anointIf(val -> fileCommonConfig.macroLineUpperCase.startsWith(CODE_END_TABLECELL_90()), val -> 90)
                .anointIf(val -> fileCommonConfig.macroLineUpperCase.startsWith(CODE_END_TABLECELL_180()), val -> 180)
                .anointIf(val -> fileCommonConfig.macroLineUpperCase.startsWith(CODE_END_TABLECELL_270()), val -> 270)
                .coronate();
        fileCommonConfig.cellHeight = null;
        if (!mifHandler.endTableCell(rotate)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.endTableCell(rotate) == false");
        }
        return result.mutate2True();
    }

    public static TS_Log.Result_withLog compile_TABLECELL_BORDER(TS_FileCommonConfig fileCommonConfig) {
        var result = d.createFuncBoolean("compile_TABLECELL_BORDER");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("token size not 2");
        }
        fileCommonConfig.enableTableCellBorder = fileCommonConfig.macroLineTokens.get(1).equals("NOBORDER");
        return result.mutate2True();
    }

    public static TS_Log.Result_withLog compile_BEGIN_TABLE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_BEGIN_TABLE");
        if (fileCommonConfig.macroLineTokens.size() < 2) {
            return result.mutate2Error(CODE_BEGIN_TABLE() + " code should have more than 1 token error!");
        }
        var relColSizes = new int[fileCommonConfig.macroLineTokens.size() - 1];//can be only 1 as that many columns with same size
        for (var rcsi = 0; rcsi < relColSizes.length; rcsi++) {
            var rcsf = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(rcsi + 1));
            if (rcsf == null || rcsf < 0) {
                return result.mutate2Error(CODE_BEGIN_TABLE() + " token.relColSizes[" + rcsi + "] error! rcsf == null || rcsf < 0");
            }
            relColSizes[rcsi] = rcsf;
        }
        d.ci("compile_BEGIN_TABLE.SNF relColSizes: " + TGS_StringUtils.cmn().toString(relColSizes, ", "));
        if (relColSizes.length == 1) {
            relColSizes = TS_RandomUtils.nextIntArray(relColSizes[0], 1, 1);
        }
        d.ci("compile_BEGIN_TABLE.PRE relColSizes: " + TGS_StringUtils.cmn().toString(relColSizes, ", "));
        relColSizes = TGS_MathUtils.convertWeighted(relColSizes, 1, 100);
        d.ci("compile_BEGIN_TABLE.FIX relColSizes: " + TGS_StringUtils.cmn().toString(relColSizes, ", "));
        if (!mifHandler.beginTable(relColSizes)) {
            return result.mutate2Error("fileCommonConfig.mifHandler.beginTable(relColSizes) == false");
        }
        return result.mutate2True();
    }

    public static TS_Log.Result_withLog compile_END_TABLE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_END_TABLE");
        if (!mifHandler.endTable()) {
            return result.mutate2Error("fileCommonConfig.mifHandler.endTable() == false");
        }
        return result.mutate2True();
    }
}
