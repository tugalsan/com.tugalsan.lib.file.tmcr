package com.tugalsan.api.file.tmcr.server.code.parser;

public class TS_LibRepParser_WhiteSpace {

    public static boolean is_WHITE_SPACE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLine.startsWith("//") || macroGlobals.macroLine.isEmpty();
    }
}
