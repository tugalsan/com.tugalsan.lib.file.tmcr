package com.tugalsan.api.file.tmcr.server.code.inject;

import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.string.server.*;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;
import com.tugalsan.lib.file.client.*;
import static com.tugalsan.api.file.tmcr.server.code.inject.TS_FileTmcrCodeInjectTags.CODE_INJECT_CODE;
import com.tugalsan.api.file.tmcr.server.code.parser.*;
import com.tugalsan.lib.route.client.*;
import java.util.stream.*;

public class TS_FileTmcrCodeInjectCompile {

    final private static TS_Log d = TS_Log.of( TS_FileTmcrCodeInjectCompile.class);

    public static TGS_Tuple3<String, Boolean, String> compile_CODE_INJECT_CODE(TS_FileTmcrParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_CODE_INJECT_CODE");
        d.ci("compile_CODE_INJECT_CODE", "welcome");
        return TGS_UnSafe.call(() -> {
            var injectCheckEnable = false;
            d.ci("compile_CODE_INJECT_CODE", "entered inject while with lineCount " + macroGlobals.macroLines.size());
            for (var i = 0; i < macroGlobals.macroLines.size(); i++) {
                var macroLine = TGS_StringUtils.removeConsecutive(macroGlobals.macroLines.get(i).trim(), " ");
                d.ci("compile_CODE_INJECT_CODE", "macroLine",macroLine);
                var macroLineUpperCase = TGS_CharSetCast.toLocaleUpperCase(macroLine);
                d.ci("compile_CODE_INJECT_CODE", "macroLineUpperCase",macroLineUpperCase);
                if (macroLineUpperCase.startsWith(CODE_INJECT_CODE())) {
                    d.ci("compile_CODE_INJECT_CODE", "inject found");
                    injectCheckEnable = true;
                    var urlStr = macroLine.substring(CODE_INJECT_CODE().length() + 1).trim();
                    d.ci("compile_CODE_INJECT_CODE", "url detected as ", urlStr);

                    {//ADD TABLENAME AND SID IF NOT EXISTS
                        var route = TGS_LibRoute.of(TGS_Url.of(urlStr));
                        var p_t = route.getParamStr(TGS_LibFileServletUtils.PARAM_FETCH_REPORT_CONFIG_TABLE());
                        if (TGS_StringUtils.isNullOrEmpty(p_t)) {
                            route.setParam(TGS_LibFileServletUtils.PARAM_FETCH_REPORT_CONFIG_TABLE(), macroGlobals.tablename);
                        }
                        var p_sid = route.getParamStr(TGS_LibFileServletUtils.PARAM_FETCH_REPORT_CONFIG_ID());
                        if (TGS_StringUtils.isNullOrEmpty(p_sid)) {
                            route.setParam(TGS_LibFileServletUtils.PARAM_FETCH_REPORT_CONFIG_ID(), macroGlobals.selectedId);
                        }
                        urlStr = route.toString();
                    }
                    d.ci("url parameter-ed as ", urlStr);

                    d.ci("getting html context of", urlStr);
                    var htmlCodeContent = TS_UrlDownloadUtils.toText(TGS_Url.of(urlStr));
                    if (htmlCodeContent == null) {
                        return d.returnError(result, "ERROR: htmlCodeContent return null. check " + urlStr + " @compile_CODE_INJECT_CODE3");
                    }
                    d.ci("parsing html context");
                    var macroLines2 = TS_StringUtils.toList(htmlCodeContent, "\n");
                    if (macroLines2 == null) {
                        return d.returnError(result, "ERROR: macroLines2 == null @compile_CODE_INJECT_CODE4");
                    }
                    d.ci("compile_CODE_INJECT_CODE", "Injection content.size: " + macroLines2.size());
                    macroGlobals.macroLines.remove(i);

                    var offsetMacroLines = i;
                    d.ci("compile_CODE_INJECT_CODE", "Before Injection: " + macroGlobals.macroLines.size());
                    IntStream.range(0, macroLines2.size()).forEachOrdered(i2 -> macroGlobals.macroLines.add(offsetMacroLines + i2, macroLines2.get(i2)));
                    d.ci("compile_CODE_INJECT_CODE", "After Injection: " + macroGlobals.macroLines.size());
                    break;
                }
            }
            if (injectCheckEnable) {
                d.ci("compile_CODE_INJECT_CODE", "recursive inject loop" + macroGlobals.macroLines.size());
                return compile_CODE_INJECT_CODE(macroGlobals);
            }
            d.ci("compile_CODE_INJECT_CODE", "exit inject loop" + macroGlobals.macroLines.size());
            if (d.infoEnable) {
                IntStream.range(0, macroGlobals.macroLines.size()).forEachOrdered(i -> d.ci("compile_CODE_INJECT_CODE.AFTER_INJECT.macroLines[", i, "/", macroGlobals.macroLines.size(), "]-> [", macroGlobals.macroLines.get(i), "]"));
            }
            return d.returnTrue(result);
        }, e -> {
            return d.returnError(result, "ERROR: " + TGS_StringUtils.toString((Throwable) e) + " @compile_CODE_INJECT_CODE1");
        });
    }
}
