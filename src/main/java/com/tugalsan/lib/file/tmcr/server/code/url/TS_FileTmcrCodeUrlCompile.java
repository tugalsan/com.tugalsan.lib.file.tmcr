package com.tugalsan.lib.file.tmcr.server.code.url;

import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.lib.domain.server.*;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_Globals;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlTags.CODE_URL_SH_NEW;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlTags.CODE_URL_SH_OLD;
import static com.tugalsan.lib.file.tmcr.server.code.url.TS_FileTmcrCodeUrlWriter.CODE_URL_LOCALHOST;

public class TS_FileTmcrCodeUrlCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeUrlCompile.class);

    public static boolean is_CODE_URL_SH_OLD(TS_FileTmcrParser_Globals macroGlobals, int idx) {
        return TGS_CharSetCast.containsLocaleIgnoreCase(macroGlobals.macroLines.get(idx), CODE_URL_SH_OLD());
    }

    public static boolean is_CODE_URL_LOCALHOST(TS_FileTmcrParser_Globals macroGlobals, int idx) {
        return TGS_CharSetCast.containsLocaleIgnoreCase(macroGlobals.macroLines.get(idx), CODE_URL_LOCALHOST());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_URL_SH_OLD(TS_FileTmcrParser_Globals macroGlobals, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_SH");
        d.ci("compile_CODE_URL_SH", "before", macroGlobals.macroLines.get(idx));
        macroGlobals.macroLines.set(idx, macroGlobals.macroLines.get(idx).replace(CODE_URL_SH_OLD(), CODE_URL_SH_NEW()));
        d.ci("compile_CODE_URL_SH", "after", macroGlobals.macroLines.get(idx));
        return d.returnTrue(result);
    }

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_URL_LOCALHOST(TS_SQLConnAnchor anchor, TS_FileTmcrParser_Globals macroGlobals, int idx) {
        var result = d.createFuncBoolean("compile_CODE_URL_LOCALHOST");
        d.ci("compile_CODE_URL_LOCALHOST", "before", macroGlobals.macroLines.get(idx));
        var domain = TS_LibDomainCardUtils.get();
        if (domain == null) {
            return d.returnError(result, "domian pack == null for url:" + macroGlobals.url);
        }
        d.ci("compile_CODE_URL_LOCALHOST", "host", domain.domainName);
        if (TGS_CharSetCast.containsLocaleIgnoreCase(macroGlobals.macroLines.get(idx), CODE_URL_LOCALHOST())) {
            macroGlobals.macroLines.set(idx, macroGlobals.macroLines.get(idx).replace(CODE_URL_LOCALHOST(), domain.domainName));
        }
        var codeLocalHostLowerCase = TGS_CharSetCast.toLocaleLowerCase(CODE_URL_LOCALHOST());
        if (TGS_CharSetCast.containsLocaleIgnoreCase(macroGlobals.macroLines.get(idx), codeLocalHostLowerCase)) {
            macroGlobals.macroLines.set(idx, macroGlobals.macroLines.get(idx).replace(codeLocalHostLowerCase, domain.domainName));
        }
        d.ci("compile_CODE_URL_LOCALHOST", "after", macroGlobals.macroLines.get(idx));
        return d.returnTrue(result);
    }
}
