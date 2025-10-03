package com.tugalsan.lib.file.tmcr.server.code.map;

public class TS_LibFileTmcrCodeMapUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(TS_LibFileTmcrCodeMapTags.CODE_MAPADD_FROMSQL()).append("VAR ID ...\n");
        sb.append("//  ").append(TS_LibFileTmcrCodeMapTags.CODE_MAPGET().substring(0, 3)).append(" ").append(TS_LibFileTmcrCodeMapTags.CODE_MAPGET().substring(3)).append(".0/IDX/LAST (without space)...\n");
    }
}
