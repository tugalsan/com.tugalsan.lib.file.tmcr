package com.tugalsan.api.file.tmcr.server.code.filename;

import com.tugalsan.api.file.tmcr.server.code.parser.*;

public class TS_FileTmcrCodeFileNameCompile {

//    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeFileNameCompile.class);

    public static boolean is_FILENAME_START(TS_FileTmcrParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_FileTmcrCodeFileNameTags.CODE_FILENAME_START());
    }

    public static boolean is_FILENAME_END(TS_FileTmcrParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_FileTmcrCodeFileNameTags.CODE_FILENAME_END());
    }
}
