package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonBall;

public class TS_FileTmcrParser_WhiteSpace {

    public static boolean is_WHITE_SPACE(TS_FileCommonBall fileCommonBall) {
        return fileCommonBall.macroLine.startsWith("//") || fileCommonBall.macroLine.isEmpty();
    }
}
