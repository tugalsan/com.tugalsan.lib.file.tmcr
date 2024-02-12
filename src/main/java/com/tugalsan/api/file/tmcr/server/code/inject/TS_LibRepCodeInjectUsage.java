package com.tugalsan.api.file.tmcr.server.code.inject;

import static com.tugalsan.api.file.tmcr.server.code.inject.TS_LibRepCodeInjectTags.CODE_INJECT_CODE;

public class TS_LibRepCodeInjectUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_INJECT_CODE()).append(" URL/implementsDefaultInputs\n");
        //sb.append("//  " + CODE_EXT + " ...\n");
    }
}
