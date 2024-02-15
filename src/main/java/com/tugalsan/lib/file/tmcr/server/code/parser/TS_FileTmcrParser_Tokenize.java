package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.tuple.client.TGS_Tuple3;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.string.server.TS_StringUtils;

public class TS_FileTmcrParser_Tokenize {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrParser_Tokenize.class);

    public static TGS_Tuple3<String, Boolean, String> compile_TOKENIZE(TS_FileCommonBall fileCommonBall, int i) {
        var result = d.createFuncBoolean("compile_TOKENIZE");
        fileCommonBall.macroLine = TGS_StringUtils.removeConsecutive(fileCommonBall.macroLines.get(i), " ").trim();
        fileCommonBall.macroLineUpperCase = TGS_CharSetCast.toLocaleUpperCase(fileCommonBall.macroLine);
        d.ci(result.value0, "compileAll.After Tokenize.trim");
        fileCommonBall.macroLineTokens = TS_StringUtils.toList(fileCommonBall.macroLine, " ");
        d.ci(result.value0, "compileAll.After Tokenize.parse");
        if (fileCommonBall.macroLineTokens == null) {
            return d.returnError(result, "ERROR: fileCommonBall.macroLineTokens == null for macroline[" + i + "]->[" + fileCommonBall.macroLines.get(i) + "]");
        }
        d.ci(result.value0, "line " + i + " " + fileCommonBall.macroLine + ", Tokensize: " + fileCommonBall.macroLineTokens.size());
        d.ci(result.value0, "compileAll.After Tokenize");
        return d.returnTrue(result);
    }
}
