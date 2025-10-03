package com.tugalsan.lib.file.tmcr.server.code.page;

import module com.tugalsan.api.cast;
import module com.tugalsan.api.charset;
import module com.tugalsan.api.log;
import module com.tugalsan.lib.file.tmcr;
import module com.tugalsan.api.file.common;
import java.util.*;

public class TS_LibFileTmcrCodePageCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodePageCompile.class);

    private static int DEFAULT_PAGE_SIZE_A() {
        return 4;
    }

    private static boolean DEFAULT_PAGE_LAYOUT_LANDSCAPE() {
        return false;
    }

    private static int DEFAULT_PAGE_MARGIN_LFT() {
        return 50;
    }

    private static int DEFAULT_PAGE_MARGIN_RGT() {
        return 10;
    }

    private static int DEFAULT_PAGE_MARGIN_TOP() {
        return 10;
    }

    private static int DEFAULT_PAGE_MARGIN_BTM() {
        return 10;
    }

    public static boolean is_INSERT_PAGE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE());
    }

    public static TS_Log.Result_withLog compile_INSERT_PAGE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_INSERT_PAGE");
        if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 7)) {
            return result.mutate2Error("Token size not 7 error @[" + fileCommonConfig.macroLine + "]");
        }
        boolean land;
        var landText = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(2));
        if (Objects.equals(landText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_LAND())) {
            land = true;
        } else if (Objects.equals(landText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_LAND())) {
            land = false;
        } else {
            d.ci(result.classNameDotfuncName, TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " code token[2] error! -> SET Default as " + TS_LibFileTmcrCodePageTags.CODE_TOKEN_PORT());
            land = DEFAULT_PAGE_LAYOUT_LANDSCAPE();
        }
        var ml = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(3)).orElse(null);
        if (ml == null) {
            d.ce(result.classNameDotfuncName, TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " code token[3] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_LFT());
            ml = DEFAULT_PAGE_MARGIN_LFT();
        }
        var mr = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(4)).orElse(null);
        if (mr == null) {
            d.ce(result.classNameDotfuncName, TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " code token[4] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_RGT());
            mr = DEFAULT_PAGE_MARGIN_RGT();
        }
        var mt = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(5)).orElse(null);
        if (mt == null) {
            d.ce(result.classNameDotfuncName, TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " code token[5] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_TOP());
            mt = DEFAULT_PAGE_MARGIN_TOP();
        }
        var mb = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(6)).orElse(null);
        if (mb == null) {
            d.ce(result.classNameDotfuncName, TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " code token[6] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_BTM());
            mb = DEFAULT_PAGE_MARGIN_BTM();
        }
        int pageSizeAX;
        var pageSizeText = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(1));
        if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A0())) {
            pageSizeAX = 0;
        } else if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A1())) {
            pageSizeAX = 1;
        } else if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A2())) {
            pageSizeAX = 2;
        } else if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A3())) {
            pageSizeAX = 3;
        } else if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A4())) {
            pageSizeAX = 4;
        } else if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A5())) {
            pageSizeAX = 5;
        } else if (Objects.equals(pageSizeText, TS_LibFileTmcrCodePageTags.CODE_TOKEN_A6())) {
            pageSizeAX = 6;
        } else {
            d.ce(TS_LibFileTmcrCodePageTags.CODE_INSERT_PAGE() + " code token[1] error! -> SET Default as " + TS_LibFileTmcrCodePageTags.CODE_TOKEN_A4());
            pageSizeAX = DEFAULT_PAGE_SIZE_A();
        }
        if (!mifHandler.createNewPage(pageSizeAX, land, ml, mr, mt, mb)) {
            return result.mutate2Error("Cannot createNewPage->" + "fileCommonConfig.mifHandler.createNewPage(pageSizeAX:" + pageSizeAX + ", land, ml" + ml + ", mr" + mr + ", mt" + mt + ", mb" + mb + ")");
        }
        return result.mutate2True();
    }

    public static boolean createNewPageDefault(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        return mifHandler.createNewPage(DEFAULT_PAGE_SIZE_A(), DEFAULT_PAGE_LAYOUT_LANDSCAPE(),
                DEFAULT_PAGE_MARGIN_LFT(), DEFAULT_PAGE_MARGIN_RGT(),
                DEFAULT_PAGE_MARGIN_TOP(), DEFAULT_PAGE_MARGIN_BTM());
    }
}
