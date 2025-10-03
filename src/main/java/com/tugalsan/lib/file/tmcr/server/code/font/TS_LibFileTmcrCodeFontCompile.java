package com.tugalsan.lib.file.tmcr.server.code.font;

import module com.tugalsan.api.cast;
import module com.tugalsan.api.charset;
import module com.tugalsan.api.log;
import module com.tugalsan.api.file.common;
import module com.tugalsan.lib.file.tmcr;
import java.util.*;

public class TS_LibFileTmcrCodeFontCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeFontCompile.class);

    public static boolean is_SET_FONT_COLOR(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileCommonFontTags.CODE_SET_FONT_COLOR());
    }

    public static boolean is_SET_FONT_SIZE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileCommonFontTags.CODE_SET_FONT_SIZE());
    }

    public static TS_Log.Result_withLog compile_SET_FONT_COLOR(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_SET_FONT_COLOR");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("token size not 2");
        }
        fileCommonConfig.fontColor = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(1));
        var comparison = TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLACK().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLUE().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GRAY().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GREEN().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_ORANGE().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_PINK().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED().equals(fileCommonConfig.fontColor)
                || TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_YELLOW().equals(fileCommonConfig.fontColor);
        if (!comparison) {
            return result.mutate2Error("CODE_TOKEN_FONT_COLOR_XXX code token[1] error! should be " + TS_FileCommonConfig.class.getSimpleName() + ".CODE_TOKEN_FONT_COLOR_XXX");
        }
        if (!mifHandler.setFontColor()) {
            return result.mutate2Error("Error: fileCommonConfig.mifHandler.setFontColor() == false");
        }
        return result.mutate2True();
    }

    public static TS_Log.Result_withLog compile_SET_FONT_SIZE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_SET_FONT_SIZE");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("token size is not 2");
        }
        var nfs = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(1)).orElse(null);
        if (nfs == null || nfs < 1) {
            return result.mutate2Error(TS_FileCommonFontTags.CODE_SET_FONT_SIZE() + " code token[1] error! size should be 1 or more");
        }
        fileCommonConfig.fontHeight = nfs;
        if (!mifHandler.setFontHeight()) {
            return result.mutate2Error("Error: fileCommonConfig.mifHandler.setFontHeight() == false");
        }
        return result.mutate2True();
    }

    public static boolean is_SET_FONT_STYLE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileCommonFontTags.CODE_SET_FONT_STYLE());
    }

    public static TS_Log.Result_withLog compile_SET_FONT_STYLE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_SET_FONT_STYLE");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return result.mutate2Error("Error: Tokensize is not 2!");
        }
        fileCommonConfig.fontUnderlined = false;
        var code = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(1));
        if (Objects.equals(code, TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLD())) {
            fileCommonConfig.fontBold = true;
            fileCommonConfig.fontItalic = false;
        } else if (Objects.equals(code, TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_ITALIC())) {
            fileCommonConfig.fontBold = false;
            fileCommonConfig.fontItalic = true;
        } else if (Objects.equals(code, TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLDITALIC())) {
            fileCommonConfig.fontBold = true;
            fileCommonConfig.fontItalic = true;
        } else if (Objects.equals(code, TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_PLAIN())) {
            fileCommonConfig.fontBold = false;
            fileCommonConfig.fontItalic = false;
        } else {
            return result.mutate2Error(TS_FileCommonFontTags.CODE_SET_FONT_STYLE() + " code token[1] error! should be either: " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLD() + ", " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_ITALIC() + ", " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLDITALIC() + ", " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_PLAIN());
        }
        if (!mifHandler.setFontStyle()) {
            return result.mutate2Error("Error: fileCommonConfig.mifHandler.setFontStyle() == false");
        }
        return result.mutate2True();
    }
}
