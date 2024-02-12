package com.tugalsan.lib.file.tmcr.server.code.parser;

public class TS_FileTmcrParser_WhiteSpace {

    public static boolean is_WHITE_SPACE(TS_FileTmcrParser_Globals macroGlobals) {
        return macroGlobals.macroLine.startsWith("//") || macroGlobals.macroLine.isEmpty();
    }
}
