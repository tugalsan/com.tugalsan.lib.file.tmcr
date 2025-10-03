package com.tugalsan.lib.file.tmcr.server.code.url;

import module com.tugalsan.api.charset;
import module com.tugalsan.api.log;
import module com.tugalsan.api.sql.conn;
import module com.tugalsan.api.file.common;

public class TS_LibFileTmcrCodeUrlCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeUrlCompile.class);

    public static boolean is_CODE_URL_SH_OLD(TS_FileCommonConfig fileCommonConfig, int idx) {
        return TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), TS_LibFileTmcrCodeUrlTags.CODE_URL_SH_OLD());
    }

    public static boolean is_CODE_URL_LOCALHOST(TS_FileCommonConfig fileCommonConfig, int idx) {
        return TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), TS_LibFileTmcrCodeUrlWriter.CODE_URL_LOCALHOST());
    }

    public static TS_Log.Result_withLog compile_CODE_URL_SH_OLD(TS_FileCommonConfig fileCommonConfig, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_SH");
        d.ci("compile_CODE_URL_SH", "before", fileCommonConfig.macroLines.get(idx));
        fileCommonConfig.macroLines.set(idx, fileCommonConfig.macroLines.get(idx).replace(TS_LibFileTmcrCodeUrlTags.CODE_URL_SH_OLD(), TS_LibFileTmcrCodeUrlTags.CODE_URL_SH_NEW()));
        d.ci("compile_CODE_URL_SH", "after", fileCommonConfig.macroLines.get(idx));
        return result.mutate2True();
    }

    public static TS_Log.Result_withLog compile_CODE_URL_LOCALHOST(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_LOCALHOST");
        d.ci("compile_CODE_URL_LOCALHOST", "before", fileCommonConfig.macroLines.get(idx));
        d.ci("compile_CODE_URL_LOCALHOST", "host", fileCommonConfig.domainName);
        if (TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), TS_LibFileTmcrCodeUrlWriter.CODE_URL_LOCALHOST())) {
            fileCommonConfig.macroLines.set(idx, fileCommonConfig.macroLines.get(idx).replace(TS_LibFileTmcrCodeUrlWriter.CODE_URL_LOCALHOST(), fileCommonConfig.domainName));
        }
        var codeLocalHostLowerCase = TGS_CharSetCast.current().toLowerCase(TS_LibFileTmcrCodeUrlWriter.CODE_URL_LOCALHOST());
        if (TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), codeLocalHostLowerCase)) {
            fileCommonConfig.macroLines.set(idx, fileCommonConfig.macroLines.get(idx).replace(codeLocalHostLowerCase, fileCommonConfig.domainName));
        }
        d.ci("compile_CODE_URL_LOCALHOST", "after", fileCommonConfig.macroLines.get(idx));
        return result.mutate2True();
    }
}
