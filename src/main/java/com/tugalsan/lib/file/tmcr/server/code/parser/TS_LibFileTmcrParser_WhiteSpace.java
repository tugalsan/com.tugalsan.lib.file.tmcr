package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonConfig;

public class TS_LibFileTmcrParser_WhiteSpace {

    public static boolean is_WHITE_SPACE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith("//") || fileCommonConfig.macroLine.isEmpty();
    }
}
