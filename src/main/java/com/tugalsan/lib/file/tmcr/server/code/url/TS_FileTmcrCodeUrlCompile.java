package com.tugalsan.lib.file.tmcr.server.code.url;

import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlTags.CODE_URL_SH_NEW;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlTags.CODE_URL_SH_OLD;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlWriter.CODE_URL_LOCALHOST;

public class TS_FileTmcrCodeUrlCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeUrlCompile.class);

    public static boolean is_CODE_URL_SH_OLD(TS_FileCommonBall fileCommonBall, int idx) {
        return TGS_CharSetCast.containsLocaleIgnoreCase(fileCommonBall.macroLines.get(idx), CODE_URL_SH_OLD());
    }

    public static boolean is_CODE_URL_LOCALHOST(TS_FileCommonBall fileCommonBall, int idx) {
        return TGS_CharSetCast.containsLocaleIgnoreCase(fileCommonBall.macroLines.get(idx), CODE_URL_LOCALHOST());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_URL_SH_OLD(TS_FileCommonBall fileCommonBall, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_SH");
        d.ci("compile_CODE_URL_SH", "before", fileCommonBall.macroLines.get(idx));
        fileCommonBall.macroLines.set(idx, fileCommonBall.macroLines.get(idx).replace(CODE_URL_SH_OLD(), CODE_URL_SH_NEW()));
        d.ci("compile_CODE_URL_SH", "after", fileCommonBall.macroLines.get(idx));
        return d.returnTrue(result);
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_URL_LOCALHOST(TS_SQLConnAnchor anchor, TS_FileCommonBall fileCommonBall, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_LOCALHOST");
        d.ci("compile_CODE_URL_LOCALHOST", "before", fileCommonBall.macroLines.get(idx));
        d.ci("compile_CODE_URL_LOCALHOST", "host", fileCommonBall.domainName);
        if (TGS_CharSetCast.containsLocaleIgnoreCase(fileCommonBall.macroLines.get(idx), CODE_URL_LOCALHOST())) {
            fileCommonBall.macroLines.set(idx, fileCommonBall.macroLines.get(idx).replace(CODE_URL_LOCALHOST(), fileCommonBall.domainName));
        }
        var codeLocalHostLowerCase = TGS_CharSetCast.toLocaleLowerCase(CODE_URL_LOCALHOST());
        if (TGS_CharSetCast.containsLocaleIgnoreCase(fileCommonBall.macroLines.get(idx), codeLocalHostLowerCase)) {
            fileCommonBall.macroLines.set(idx, fileCommonBall.macroLines.get(idx).replace(codeLocalHostLowerCase, fileCommonBall.domainName));
        }
        d.ci("compile_CODE_URL_LOCALHOST", "after", fileCommonBall.macroLines.get(idx));
        return d.returnTrue(result);
    }
}
