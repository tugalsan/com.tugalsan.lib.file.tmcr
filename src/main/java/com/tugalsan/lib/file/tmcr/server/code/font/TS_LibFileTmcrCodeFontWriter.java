package com.tugalsan.lib.file.tmcr.server.code.font;

import module com.tugalsan.api.file.common;
import module com.tugalsan.api.string;

public class TS_LibFileTmcrCodeFontWriter {

    public static String SET_FONT_SIZE(int size) {
        return TS_FileCommonFontTags.CODE_SET_FONT_SIZE() + " " + size + "\n";
    }

    public static String SET_FONT_STYLE_PLAIN() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_STYLE(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_PLAIN(), "\n");
    }

    public static String SET_FONT_STYLE_BOLD() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_STYLE(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLD(), "\n");
    }

    public static String SET_FONT_STYLE_ITALIC() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_STYLE(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_ITALIC(), "\n");
    }

    public static String SET_FONT_STYLE_BOLDITALIC() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_STYLE(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_STYLE_BOLDITALIC(), "\n");
    }

    public static String SET_FONT_COLOR_RED() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED(), "\n");
    }

    public static String SET_FONT_COLOR_BLACK() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLACK(), "\n");
    }

    public static String SET_FONT_COLOR_WHITE() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_WHITE(), "\n");
    }

    public static String SET_FONT_COLOR_YELLOW() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_YELLOW(), "\n");
    }

    public static String SET_FONT_COLOR_BLUE() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLUE(), "\n");
    }

    public static String SET_FONT_COLOR_GREEN() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GREEN(), "\n");
    }

    public static String SET_FONT_COLOR_PINK() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_PINK(), "\n");
    }

    public static String SET_FONT_COLOR_ORANGE() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_ORANGE(), "\n");
    }

    public static String SET_FONT_COLOR_CYAN() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_CYAN(), "\n");
    }

    public static String SET_FONT_COLOR_DARK_GRAY() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY(), "\n");
    }

    public static String SET_FONT_COLOR_GRAY() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GRAY(), "\n");
    }

    public static String SET_FONT_COLOR_LIGHT_GRAY() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY(), "\n");
    }

    public static String SET_FONT_COLOR_MAGENTA() {
        return TGS_StringUtils.cmn().concat(TS_FileCommonFontTags.CODE_SET_FONT_COLOR(), " ", TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA(), "\n");
    }
}
