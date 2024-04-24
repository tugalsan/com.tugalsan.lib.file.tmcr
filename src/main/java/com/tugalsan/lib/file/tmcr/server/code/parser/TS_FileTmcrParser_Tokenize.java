package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.charset.client.TGS_CharSet;
import com.tugalsan.api.tuple.client.TGS_Tuple3;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.string.server.TS_StringUtils;

public class TS_FileTmcrParser_Tokenize {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrParser_Tokenize.class);

    public static TGS_Tuple3<String, Boolean, String> compile_TOKENIZE(TS_FileCommonConfig fileCommonConfig, int i) {
        var result = d.createFuncBoolean("compile_TOKENIZE");
        fileCommonConfig.macroLine = TGS_StringUtils.removeConsecutive(fileCommonConfig.macroLines.get(i), " ").trim();
        fileCommonConfig.macroLineUpperCase = TGS_CharSet.cmn().languageDefault().toUpperCase(fileCommonConfig.macroLine);
        d.ci(result.value0, "compileAll.After Tokenize.trim");
        fileCommonConfig.macroLineTokens = TS_StringUtils.toList(fileCommonConfig.macroLine, " ");
        d.ci(result.value0, "compileAll.After Tokenize.parse");
        if (fileCommonConfig.macroLineTokens == null) {
            return d.returnError(result, "ERROR: fileCommonConfig.macroLineTokens == null for macroline[" + i + "]->[" + fileCommonConfig.macroLines.get(i) + "]");
        }
        d.ci(result.value0, "line " + i + " " + fileCommonConfig.macroLine + ", Tokensize: " + fileCommonConfig.macroLineTokens.size());
        d.ci(result.value0, "compileAll.After Tokenize");
        return d.returnTrue(result);
    }
}
