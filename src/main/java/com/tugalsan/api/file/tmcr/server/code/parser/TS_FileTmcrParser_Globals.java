package com.tugalsan.api.file.tmcr.server.code.parser;

import com.tugalsan.api.file.tmcr.server.code.font.TS_FileTmcrCodeFontTags;
import com.tugalsan.api.list.client.*;
import java.nio.file.*;
import com.tugalsan.api.time.client.*;
import java.util.*;
import com.tugalsan.api.sql.conn.server.*;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.api.file.tmcr.server.file.*;

public class TS_FileTmcrParser_Globals {

//    final private static TS_Log d = TS_Log.of(TS_FileTmcrParser_Globals.class);

    public String fontColor;
    public Path fontPathBold;
    public Path fontPathBoldItalic;
    public Path fontPathItalic;
    public Path fontPathRegular;
    public float fontHeightK;

    //FILE NAMES
    public String prefferedFileNameLabel = "";

    //AUTOMATION
    public TS_FileTmcrFileHandler mifHandler;
    public List<String> requestedFileTypes;
    public String fileNameLabel;
    public TS_SQLConnAnchor anchor;

    //MACRO BASE
    public TGS_Time now;
    public String doFind_gotoLabel = null;
    public String macroLine;
    public String macroLineUpperCase;
    public List<String> macroLineTokens;
    public List<String> mapVars;
    public boolean insertPageTriggeredBefore = false;

    //FONT
    public int fontHeight;
    public boolean fontBold, fontItalic, fontUnderlined;
    public boolean enableTableCellBorder = true;

    public String getHexColor() {
        if (fontColor == null) {
            return "000000";
        }
        if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_RED())) {
            return "FF0000";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_YELLOW())) {
            return "FFFF00";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_BLUE())) {
            return "0000FF";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_GREEN())) {
            return "00FF00";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_PINK())) {
            return "FFC0CB";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_ORANGE())) {
            return "FFA500";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_CYAN())) {
            return "00FFFF";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY())) {
            return "585858";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_GRAY())) {
            return "808080";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY())) {
            return "D3D3D3";
        } else if (Objects.equals(fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA())) {
            return "6D1717";
        } else/* if (Objects.equals(fontColor, CODE_TOKEN_FONT_COLOR_BLACK()))*/ {
            return "000000";
        }
    }

    //IMAGE WEB
    public long imageCounter = 0L;

    //GLOBALS
    public boolean runReport;
    public List<String> macroLines;
    public String username;
    public String tablename;
    public Long selectedId;
    public String funcName;
    public Integer cellHeight;
    public String userDotTablename;

    public TGS_Url url;
    public Path dirDat;

    public String customDomain;

    @Override
    public String toString() {
        return TS_FileTmcrParser_Globals.class.getSimpleName() + "{" + "runReport=" + runReport + ", username=" + username + ", tablename=" + tablename + ", selectedId=" + selectedId + ", funcName=" + funcName + ", userDotTablename=" + userDotTablename + ", url=" + url + ", dirDat=" + dirDat + '}';
    }

    public TS_FileTmcrParser_Globals(TS_SQLConnAnchor anchor,
            List<String> macroLines, String username,
            String tablename, Long selectedId,
            String funcName, String fileNameLabel, TGS_Url url,
            List<String> requestedFileTypes, Path dirDat,
            Path fontPathBold, Path fontPathBoldItalic, Path fontPathItalic, Path fontPathRegular,
            String customDomain
    ) {
        this.anchor = anchor;
        this.macroLines = macroLines;
        this.username = username;
        this.selectedId = selectedId;
        this.funcName = funcName;
        this.fileNameLabel = fileNameLabel;
        this.url = url;
        this.requestedFileTypes = requestedFileTypes;
        this.dirDat = dirDat;
        this.fontPathBold = fontPathBold;
        this.fontPathBoldItalic = fontPathBoldItalic;
        this.fontPathItalic = fontPathItalic;
        this.fontPathRegular = fontPathRegular;
        this.customDomain = customDomain;

        this.fontItalic = false;
        this.fontItalic = false;
        this.fontHeight = 12;
        this.fontColor = TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_BLACK();
        this.runReport = false;
        this.tablename = tablename;
        this.now = TGS_Time.of();
        this.mapVars = TGS_ListUtils.of();
        this.cellHeight = null;
        this.userDotTablename = this.username + "." + this.tablename;
    }

}
