package com.tugalsan.lib.file.tmcr.server.code.map;

import module com.tugalsan.api.string;

public class TS_LibFileTmcrCodeMapWriter {

    public static String MAPGET(String ID) {
        return TGS_StringUtils.cmn().concat(TS_LibFileTmcrCodeMapTags.CODE_MAPGET(), ".", ID, "\n");
    }

    public static String MAPADD_FROMSQL(String VAR, String ID) {
        return TGS_StringUtils.cmn().concat(TS_LibFileTmcrCodeMapTags.CODE_MAPADD_FROMSQL(), " ", VAR, " ", ID, "\n");
    }
}
