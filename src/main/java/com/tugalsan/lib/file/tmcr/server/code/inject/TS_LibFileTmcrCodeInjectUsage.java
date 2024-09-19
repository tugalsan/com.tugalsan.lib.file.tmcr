package com.tugalsan.lib.file.tmcr.server.code.inject;

import static com.tugalsan.lib.file.tmcr.server.code.inject.TS_LibFileTmcrCodeInjectTags.CODE_INJECT_CODE;

public class TS_LibFileTmcrCodeInjectUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_INJECT_CODE()).append(" URL/implementsDefaultInputs\n");
        //sb.append("//  " + CODE_EXT + " ...\n");
    }
}
