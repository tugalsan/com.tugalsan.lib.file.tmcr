package com.tugalsan.api.file.tmcr.server.code.map;

import com.tugalsan.api.string.client.*;
import static com.tugalsan.api.file.tmcr.server.code.map.TS_LibRepCodeMapTags.CODE_MAPADD_FROMSQL;
import static com.tugalsan.api.file.tmcr.server.code.map.TS_LibRepCodeMapTags.CODE_MAPGET;

public class TS_LibRepCodeMapWriter {

    public static String MAPGET(String ID) {
        return TGS_StringUtils.concat(CODE_MAPGET(), ".", ID, "\n");
    }

    public static String MAPADD_FROMSQL(String VAR, String ID) {
        return TGS_StringUtils.concat(CODE_MAPADD_FROMSQL(), " ", VAR, " ", ID, "\n");
    }
}
