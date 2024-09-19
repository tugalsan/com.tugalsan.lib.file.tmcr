package com.tugalsan.lib.file.tmcr.server.code.map;

import static com.tugalsan.lib.file.tmcr.server.code.map.TS_LibFileTmcrCodeMapTags.CODE_MAPADD_FROMSQL;
import static com.tugalsan.lib.file.tmcr.server.code.map.TS_LibFileTmcrCodeMapTags.CODE_MAPGET;

public class TS_LibFileTmcrCodeMapUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(CODE_MAPADD_FROMSQL()).append("VAR ID ...\n");
        sb.append("//  ").append(CODE_MAPGET().substring(0, 3)).append(" ").append(CODE_MAPGET().substring(3)).append(".0/IDX/LAST (without space)...\n");
    }
}
