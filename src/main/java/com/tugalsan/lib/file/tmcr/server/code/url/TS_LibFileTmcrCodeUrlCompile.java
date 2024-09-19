package com.tugalsan.lib.file.tmcr.server.code.url;

import com.tugalsan.api.charset.client.TGS_CharSet;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_LibFileTmcrCodeUrlTags.CODE_URL_SH_NEW;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_LibFileTmcrCodeUrlTags.CODE_URL_SH_OLD;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_LibFileTmcrCodeUrlWriter.CODE_URL_LOCALHOST;

public class TS_LibFileTmcrCodeUrlCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeUrlCompile.class);

    public static boolean is_CODE_URL_SH_OLD(TS_FileCommonConfig fileCommonConfig, int idx) {
        return TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), CODE_URL_SH_OLD());
    }

    public static boolean is_CODE_URL_LOCALHOST(TS_FileCommonConfig fileCommonConfig, int idx) {
        return TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), CODE_URL_LOCALHOST());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_URL_SH_OLD(TS_FileCommonConfig fileCommonConfig, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_SH");
        d.ci("compile_CODE_URL_SH", "before", fileCommonConfig.macroLines.get(idx));
        fileCommonConfig.macroLines.set(idx, fileCommonConfig.macroLines.get(idx).replace(CODE_URL_SH_OLD(), CODE_URL_SH_NEW()));
        d.ci("compile_CODE_URL_SH", "after", fileCommonConfig.macroLines.get(idx));
        return d.returnTrue(result);
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_URL_LOCALHOST(TS_SQLConnAnchor anchor, TS_FileCommonConfig fileCommonConfig, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_LOCALHOST");
        d.ci("compile_CODE_URL_LOCALHOST", "before", fileCommonConfig.macroLines.get(idx));
        d.ci("compile_CODE_URL_LOCALHOST", "host", fileCommonConfig.domainName);
        if (TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), CODE_URL_LOCALHOST())) {
            fileCommonConfig.macroLines.set(idx, fileCommonConfig.macroLines.get(idx).replace(CODE_URL_LOCALHOST(), fileCommonConfig.domainName));
        }
        var codeLocalHostLowerCase = TGS_CharSetCast.current().toLowerCase(CODE_URL_LOCALHOST());
        if (TGS_CharSetCast.current().containsIgnoreCase(fileCommonConfig.macroLines.get(idx), codeLocalHostLowerCase)) {
            fileCommonConfig.macroLines.set(idx, fileCommonConfig.macroLines.get(idx).replace(codeLocalHostLowerCase, fileCommonConfig.domainName));
        }
        d.ci("compile_CODE_URL_LOCALHOST", "after", fileCommonConfig.macroLines.get(idx));
        return d.returnTrue(result);
    }
}
