package com.tugalsan.lib.file.tmcr.server.code.font;

import com.tugalsan.api.file.common.server.TS_FileCommonFontTags;
import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.lib.file.tmcr.server.file.TS_FileTmcrFileHandler;
import java.util.*;

public class TS_FileTmcrCodeFontCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeFontCompile.class);

    public static boolean is_SET_FONT_COLOR(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileCommonFontTags.CODE_SET_FONT_COLOR());
    }

    public static boolean is_SET_FONT_SIZE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileCommonFontTags.CODE_SET_FONT_SIZE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_SET_FONT_COLOR(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_SET_FONT_COLOR");
        if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return d.returnError(result, "token size not 2");
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
            return d.returnError(result, "CODE_TOKEN_FONT_COLOR_XXX code token[1] error! should be " + TS_FileCommonConfig.class.getSimpleName() + ".CODE_TOKEN_FONT_COLOR_XXX");
        }
        if (!mifHandler.setFontColor()) {
            return d.returnError(result, "Error: fileCommonConfig.mifHandler.setFontColor() == false");
        }
        return d.returnTrue(result);
    }

    public static TGS_Tuple3<String, Boolean, String> compile_SET_FONT_SIZE(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_SET_FONT_SIZE");
        if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return d.returnError(result, "token size is not 2");
        }
        var nfs = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(1));
        if (nfs == null || nfs < 1) {
            return d.returnError(result, TS_FileCommonFontTags.CODE_SET_FONT_SIZE() + " code token[1] error! size should be 1 or more");
        }
        fileCommonConfig.fontHeight = nfs;
        if (!mifHandler.setFontHeight()) {
            return d.returnError(result, "Error: fileCommonConfig.mifHandler.setFontHeight() == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_SET_FONT_STYLE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLineUpperCase.startsWith(TS_FileCommonFontTags.CODE_SET_FONT_STYLE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_SET_FONT_STYLE(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_SET_FONT_STYLE");
        if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 2)) {
            return d.returnError(result, "Error: Tokensize is not 2!");
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
            return d.returnError(result, TS_FileCommonFontTags.CODE_SET_FONT_STYLE() + " code token[1] error! should be either: " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLD() + ", " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_ITALIC() + ", " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLDITALIC() + ", " + TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_PLAIN());
        }
        if (!mifHandler.setFontStyle()) {
            return d.returnError(result, "Error: fileCommonConfig.mifHandler.setFontStyle() == false");
        }
        return d.returnTrue(result);
    }
}
