package com.tugalsan.lib.file.tmcr.server.code.filename;

import com.tugalsan.api.file.common.server.TS_FileCommonConfig;

public class TS_FileTmcrCodeFileNameCompile {

//    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeFileNameCompile.class);

    public static boolean is_FILENAME_START(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileTmcrCodeFileNameTags.CODE_FILENAME_START());
    }

    public static boolean is_FILENAME_END(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileTmcrCodeFileNameTags.CODE_FILENAME_END());
    }
}
