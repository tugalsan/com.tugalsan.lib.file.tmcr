package com.tugalsan.api.file.tmcr.server.code.map;

import static com.tugalsan.api.file.tmcr.server.code.map.TS_LibRepCodeMapTags.CODE_MAPADD_FROMSQL;
import static com.tugalsan.api.file.tmcr.server.code.map.TS_LibRepCodeMapTags.CODE_MAPGET;

public class TS_LibRepCodeMapUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_MAPADD_FROMSQL()).append("VAR ID ...\n");
        sb.append("//  ").append(CODE_MAPGET().substring(0, 3)).append(" ").append(CODE_MAPGET().substring(3)).append(".0/IDX/LAST (without space)...\n");
    }
}
