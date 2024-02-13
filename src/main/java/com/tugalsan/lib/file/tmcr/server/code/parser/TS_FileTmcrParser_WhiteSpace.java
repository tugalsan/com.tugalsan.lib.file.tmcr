package com.tugalsan.lib.file.tmcr.server.code.parser;

import com.tugalsan.api.file.common.server.TS_FileCommonBall;

public class TS_FileTmcrParser_WhiteSpace {

    public static boolean is_WHITE_SPACE(TS_FileCommonBall macroGlobals) {
        return macroGlobals.macroLine.startsWith("//") || macroGlobals.macroLine.isEmpty();
    }
}
