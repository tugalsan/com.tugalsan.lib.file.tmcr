package com.tugalsan.api.file.tmcr.server.code.filename;

import com.tugalsan.api.file.tmcr.server.code.parser.*;

public class TS_LibRepCodeFileNameCompile {

//    final private static TS_Log d = TS_Log.of(TS_LibRepCodeFileNameCompile.class);

    public static boolean is_FILENAME_START(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_LibRepCodeFileNameTags.CODE_FILENAME_START());
    }

    public static boolean is_FILENAME_END(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_LibRepCodeFileNameTags.CODE_FILENAME_END());
    }
}
