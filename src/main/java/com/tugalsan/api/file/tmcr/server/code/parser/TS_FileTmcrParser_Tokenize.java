package com.tugalsan.api.file.tmcr.server.code.parser;

import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.tuple.client.TGS_Tuple3;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.string.server.TS_StringUtils;

public class TS_FileTmcrParser_Tokenize {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrParser_Tokenize.class);

    public static TGS_Tuple3<String, Boolean, String> compile_TOKENIZE(TS_FileTmcrParser_Globals macroGlobals, int i) {
        var result = d.createFuncBoolean("compile_TOKENIZE");
        macroGlobals.macroLine = TGS_StringUtils.removeConsecutive(macroGlobals.macroLines.get(i), " ").trim();
        macroGlobals.macroLineUpperCase = TGS_CharSetCast.toLocaleUpperCase(macroGlobals.macroLine);
        d.ci(result.value0, "compileAll.After Tokenize.trim");
        macroGlobals.macroLineTokens = TS_StringUtils.toList(macroGlobals.macroLine, " ");
        d.ci(result.value0, "compileAll.After Tokenize.parse");
        if (macroGlobals.macroLineTokens == null) {
            return d.returnError(result, "ERROR: macroGlobals.macroLineTokens == null for macroline[" + i + "]->[" + macroGlobals.macroLines.get(i) + "]");
        }
        d.ci(result.value0, "line " + i + " " + macroGlobals.macroLine + ", Tokensize: " + macroGlobals.macroLineTokens.size());
        d.ci(result.value0, "compileAll.After Tokenize");
        return d.returnTrue(result);
    }
}
