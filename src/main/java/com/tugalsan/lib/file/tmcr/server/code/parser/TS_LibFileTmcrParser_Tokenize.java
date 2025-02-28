package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.charset.client.TGS_CharSet;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.tuple.client.TGS_Tuple3;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.string.client.TGS_StringUtils;

public class TS_LibFileTmcrParser_Tokenize {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrParser_Tokenize.class);

    public static TS_Log.Result_withLog compile_TOKENIZE(TS_FileCommonConfig fileCommonConfig, int i) {
        var result = d.createFuncBoolean("compile_TOKENIZE");
        fileCommonConfig.macroLine = TGS_StringUtils.cmn().removeConsecutive(fileCommonConfig.macroLines.get(i), " ").trim();
        fileCommonConfig.macroLineUpperCase = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLine);
        d.ci(result.classNameDotfuncName, "compileAll.After Tokenize.trim");
        fileCommonConfig.macroLineTokens = TGS_StringUtils.jre().toList(fileCommonConfig.macroLine, " ");
        d.ci(result.classNameDotfuncName, "compileAll.After Tokenize.parse");
        if (fileCommonConfig.macroLineTokens == null) {
            return result.mutate2Error("ERROR: fileCommonConfig.macroLineTokens == null for macroline[" + i + "]->[" + fileCommonConfig.macroLines.get(i) + "]");
        }
        d.ci(result.classNameDotfuncName, "line " + i + " " + fileCommonConfig.macroLine + ", Tokensize: " + fileCommonConfig.macroLineTokens.size());
        d.ci(result.classNameDotfuncName, "compileAll.After Tokenize");
        return result.mutate2True();
    }
}
