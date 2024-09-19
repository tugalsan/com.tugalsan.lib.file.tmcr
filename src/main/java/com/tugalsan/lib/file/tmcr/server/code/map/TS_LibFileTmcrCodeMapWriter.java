package com.tugalsan.lib.file.tmcr.server.code.map;

import com.tugalsan.api.string.client.*;
import static com.tugalsan.lib.file.tmcr.server.code.map.TS_LibFileTmcrCodeMapTags.CODE_MAPADD_FROMSQL;
import static com.tugalsan.lib.file.tmcr.server.code.map.TS_LibFileTmcrCodeMapTags.CODE_MAPGET;

public class TS_LibFileTmcrCodeMapWriter {

    public static String MAPGET(String ID) {
        return TGS_StringUtils.cmn().concat(CODE_MAPGET(), ".", ID, "\n");
    }

    public static String MAPADD_FROMSQL(String VAR, String ID) {
        return TGS_StringUtils.cmn().concat(CODE_MAPADD_FROMSQL(), " ", VAR, " ", ID, "\n");
    }
}
