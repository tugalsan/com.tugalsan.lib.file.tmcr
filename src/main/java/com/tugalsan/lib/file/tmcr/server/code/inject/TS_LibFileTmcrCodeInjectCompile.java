package com.tugalsan.lib.file.tmcr.server.code.inject;

import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.string.client.*;

import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;
import static com.tugalsan.lib.file.tmcr.server.code.inject.TS_LibFileTmcrCodeInjectTags.CODE_INJECT_CODE;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.function.client.maythrowexceptions.checked.TGS_FuncMTCUtils;
import java.time.Duration;
//import com.tugalsan.lib.route.client.*;
import java.util.stream.*;

public class TS_LibFileTmcrCodeInjectCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeInjectCompile.class);

    public static TS_Log.Result_withLog compile_CODE_INJECT_CODE(TS_FileCommonConfig fileCommonConfig, Duration timeout) {
        var result = d.createFuncBoolean("compile_CODE_INJECT_CODE");
        d.ci("compile_CODE_INJECT_CODE", "welcome");
        return TGS_FuncMTCUtils.call(() -> {
            var injectCheckEnable = false;
            d.ci("compile_CODE_INJECT_CODE", "entered inject while with lineCount " + fileCommonConfig.macroLines.size());
            for (var i = 0; i < fileCommonConfig.macroLines.size(); i++) {
                var macroLine = TGS_StringUtils.cmn().removeConsecutive(fileCommonConfig.macroLines.get(i).trim(), " ");
                d.ci("compile_CODE_INJECT_CODE", "macroLine", macroLine);
                var macroLineUpperCase = TGS_CharSetCast.current().toUpperCase(macroLine);
                d.ci("compile_CODE_INJECT_CODE", "macroLineUpperCase", macroLineUpperCase);
                if (macroLineUpperCase.startsWith(CODE_INJECT_CODE())) {
                    d.ci("compile_CODE_INJECT_CODE", "inject found");
                    injectCheckEnable = true;
                    var url = TGS_Url.of(macroLine.substring(CODE_INJECT_CODE().length() + 1).trim());
                    d.ci("compile_CODE_INJECT_CODE", "url detected as ", url);

                    {//ADD TABLENAME AND SID IF NOT EXISTS
                        if (fileCommonConfig.manipulateInjectCode != null) {
                            url = fileCommonConfig.manipulateInjectCode.call(url);
                        }
                    }
                    d.ci("url parameter-ed as ", url);

                    d.ci("getting html context of", url);
                    var htmlCodeContent = TS_UrlDownloadUtils.toText(url, timeout);
                    if (htmlCodeContent == null) {
                        return result.mutate2Error("ERROR: htmlCodeContent return null. check " + url + " @compile_CODE_INJECT_CODE3");
                    }
                    d.ci("parsing html context");
                    var macroLines2 = TGS_StringUtils.jre().toList(htmlCodeContent, "\n");
                    if (macroLines2 == null) {
                        return result.mutate2Error("ERROR: macroLines2 == null @compile_CODE_INJECT_CODE4");
                    }
                    d.ci("compile_CODE_INJECT_CODE", "Injection content.size: " + macroLines2.size());
                    fileCommonConfig.macroLines.remove(i);

                    var offsetMacroLines = i;
                    d.ci("compile_CODE_INJECT_CODE", "Before Injection: " + fileCommonConfig.macroLines.size());
                    IntStream.range(0, macroLines2.size()).forEachOrdered(i2 -> fileCommonConfig.macroLines.add(offsetMacroLines + i2, macroLines2.get(i2)));
                    d.ci("compile_CODE_INJECT_CODE", "After Injection: " + fileCommonConfig.macroLines.size());
                    break;
                }
            }
            if (injectCheckEnable) {
                d.ci("compile_CODE_INJECT_CODE", "recursive inject loop" + fileCommonConfig.macroLines.size());
                return compile_CODE_INJECT_CODE(fileCommonConfig, timeout);
            }
            d.ci("compile_CODE_INJECT_CODE", "exit inject loop" + fileCommonConfig.macroLines.size());
            if (d.infoEnable) {
                IntStream.range(0, fileCommonConfig.macroLines.size()).forEachOrdered(i -> d.ci("compile_CODE_INJECT_CODE.AFTER_INJECT.macroLines[", i, "/", fileCommonConfig.macroLines.size(), "]-> [", fileCommonConfig.macroLines.get(i), "]"));
            }
            return result.mutate2True();
        }, e -> {
            return result.mutate2Error("ERROR: " + TGS_StringUtils.cmn().toString((Throwable) e) + " @compile_CODE_INJECT_CODE1");
        });
    }
}
