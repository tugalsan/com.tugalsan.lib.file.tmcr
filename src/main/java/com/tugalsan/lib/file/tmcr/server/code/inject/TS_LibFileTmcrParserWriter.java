package com.tugalsan.lib.file.tmcr.server.code.inject;

import module com.tugalsan.api.url;

public class TS_LibFileTmcrParserWriter {

    public static String INJECT_CODE(TGS_Url url) {
        return TS_LibFileTmcrCodeInjectTags.CODE_INJECT_CODE() + " " + url.url + "\n";
    }
}
