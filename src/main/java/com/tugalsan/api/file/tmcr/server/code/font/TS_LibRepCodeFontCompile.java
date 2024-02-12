package com.tugalsan.api.file.tmcr.server.code.font;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.file.tmcr.server.code.parser.*;
import java.util.*;

public class TS_LibRepCodeFontCompile {

    final private static TS_Log d = TS_Log.of(TS_LibRepCodeFontCompile.class);

    public static boolean is_SET_FONT_COLOR(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_LibRepCodeFontTags.CODE_SET_FONT_COLOR());
    }

    public static boolean is_SET_FONT_SIZE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_LibRepCodeFontTags.CODE_SET_FONT_SIZE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_SET_FONT_COLOR(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_SET_FONT_COLOR");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 2)) {
            return d.returnError(result, "token size not 2");
        }
        macroGlobals.fontColor = TGS_CharSetCast.toLocaleUpperCase(macroGlobals.macroLineTokens.get(1));
        var comparison = TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_BLACK().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_BLUE().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_GRAY().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_GREEN().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_ORANGE().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_PINK().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_RED().equals(macroGlobals.fontColor)
                || TS_LibRepCodeFontTags.CODE_TOKEN_FONT_COLOR_YELLOW().equals(macroGlobals.fontColor);
        if (!comparison) {
            return d.returnError(result, "CODE_TOKEN_FONT_COLOR_XXX code token[1] error! should be " + TS_LibRepParser_Globals.class.getSimpleName() + ".CODE_TOKEN_FONT_COLOR_XXX");
        }
        if (!macroGlobals.mifHandler.setFontColor()) {
            return d.returnError(result, "Error: macroGlobals.mifHandler.setFontColor() == false");
        }
        return d.returnTrue(result);
    }

    public static TGS_Tuple3<String, Boolean, String> compile_SET_FONT_SIZE(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_SET_FONT_SIZE");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 2)) {
            return d.returnError(result, "token size is not 2");
        }
        var nfs = TGS_CastUtils.toInteger(macroGlobals.macroLineTokens.get(1));
        if (nfs == null || nfs < 1) {
            return d.returnError(result, TS_LibRepCodeFontTags.CODE_SET_FONT_SIZE() + " code token[1] error! size should be 1 or more");
        }
        macroGlobals.fontHeight = nfs;
        if (!macroGlobals.mifHandler.setFontHeight()) {
            return d.returnError(result, "Error: macroGlobals.mifHandler.setFontHeight() == false");
        }
        return d.returnTrue(result);
    }

    public static boolean is_SET_FONT_STYLE(TS_LibRepParser_Globals macroGlobals) {
        return macroGlobals.macroLineUpperCase.startsWith(TS_LibRepCodeFontTags.CODE_SET_FONT_STYLE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_SET_FONT_STYLE(TS_LibRepParser_Globals macroGlobals) {
        var result = d.createFuncBoolean("compile_SET_FONT_STYLE");
        if (!TS_LibRepParser_Assure.checkTokenSize(macroGlobals, 2)) {
            return d.returnError(result, "Error: Tokensize is not 2!");
        }
        macroGlobals.fontUnderlined = false;
        var code = TGS_CharSetCast.toLocaleUpperCase(macroGlobals.macroLineTokens.get(1));
        if (Objects.equals(code, TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_BOLD())) {
            macroGlobals.fontBold = true;
            macroGlobals.fontItalic = false;
        } else if (Objects.equals(code, TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_ITALIC())) {
            macroGlobals.fontBold = false;
            macroGlobals.fontItalic = true;
        } else if (Objects.equals(code, TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_BOLDITALIC())) {
            macroGlobals.fontBold = true;
            macroGlobals.fontItalic = true;
        } else if (Objects.equals(code, TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_PLAIN())) {
            macroGlobals.fontBold = false;
            macroGlobals.fontItalic = false;
        } else {
            return d.returnError(result, TS_LibRepCodeFontTags.CODE_SET_FONT_STYLE() + " code token[1] error! should be either: " + TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_BOLD() + ", " + TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_ITALIC() + ", " + TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_BOLDITALIC() + ", " + TS_LibRepCodeFontTags.CODE_TOKEN_FONT_STYLE_PLAIN());
        }
        if (!macroGlobals.mifHandler.setFontStyle()) {
            return d.returnError(result, "Error: macroGlobals.mifHandler.setFontStyle() == false");
        }
        return d.returnTrue(result);
    }
}
