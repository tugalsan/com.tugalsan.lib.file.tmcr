package com.tugalsan.lib.file.tmcr.server.code.inject;

import com.tugalsan.api.url.client.TGS_Url;

public class TS_FileTmcrParserWriter {

    public static String INJECT_CODE(TGS_Url url) {
        return TS_FileTmcrCodeInjectTags.CODE_INJECT_CODE() + " " + url.url + "\n";
    }
}
