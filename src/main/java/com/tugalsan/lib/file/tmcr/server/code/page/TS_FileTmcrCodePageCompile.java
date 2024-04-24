package com.tugalsan.lib.file.tmcr.server.code.page;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSet;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_INSERT_PAGE;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A0;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A1;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A2;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A3;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A4;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A5;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_A6;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_LAND;
import static com.tugalsan.lib.file.tmcr.server.code.page.TS_FileTmcrCodePageTags.CODE_TOKEN_PORT;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.lib.file.tmcr.server.file.TS_FileTmcrFileHandler;
import java.util.*;

public class TS_FileTmcrCodePageCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodePageCompile.class);

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
        return fileCommonConfig.macroLine.startsWith(CODE_INSERT_PAGE());
    }

    public static TGS_Tuple3<String, Boolean, String> compile_INSERT_PAGE(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean("compile_INSERT_PAGE");
        if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 7)) {
            return d.returnError(result, "Token size not 7 error @[" + fileCommonConfig.macroLine + "]");
        }
        boolean land;
        var landText = TGS_CharSet.cmn().languageDefault().toUpperCase(fileCommonConfig.macroLineTokens.get(2));
        if (Objects.equals(landText, CODE_TOKEN_LAND())) {
            land = true;
        } else if (Objects.equals(landText, CODE_TOKEN_LAND())) {
            land = false;
        } else {
            d.ci(result.value0, CODE_INSERT_PAGE() + " code token[2] error! -> SET Default as " + CODE_TOKEN_PORT());
            land = DEFAULT_PAGE_LAYOUT_LANDSCAPE();
        }
        var ml = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(3));
        if (ml == null) {
            d.ce(result.value0, CODE_INSERT_PAGE() + " code token[3] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_LFT());
            ml = DEFAULT_PAGE_MARGIN_LFT();
        }
        var mr = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(4));
        if (mr == null) {
            d.ce(result.value0, CODE_INSERT_PAGE() + " code token[4] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_RGT());
            mr = DEFAULT_PAGE_MARGIN_RGT();
        }
        var mt = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(5));
        if (mt == null) {
            d.ce(result.value0, CODE_INSERT_PAGE() + " code token[5] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_TOP());
            mt = DEFAULT_PAGE_MARGIN_TOP();
        }
        var mb = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(6));
        if (mb == null) {
            d.ce(result.value0, CODE_INSERT_PAGE() + " code token[6] error! -> SET Default as " + DEFAULT_PAGE_MARGIN_BTM());
            mb = DEFAULT_PAGE_MARGIN_BTM();
        }
        int pageSizeAX;
        var pageSizeText = TGS_CharSet.cmn().languageDefault().toUpperCase(fileCommonConfig.macroLineTokens.get(1));
        if (Objects.equals(pageSizeText, CODE_TOKEN_A0())) {
            pageSizeAX = 0;
        } else if (Objects.equals(pageSizeText, CODE_TOKEN_A1())) {
            pageSizeAX = 1;
        } else if (Objects.equals(pageSizeText, CODE_TOKEN_A2())) {
            pageSizeAX = 2;
        } else if (Objects.equals(pageSizeText, CODE_TOKEN_A3())) {
            pageSizeAX = 3;
        } else if (Objects.equals(pageSizeText, CODE_TOKEN_A4())) {
            pageSizeAX = 4;
        } else if (Objects.equals(pageSizeText, CODE_TOKEN_A5())) {
            pageSizeAX = 5;
        } else if (Objects.equals(pageSizeText, CODE_TOKEN_A6())) {
            pageSizeAX = 6;
        } else {
            d.ce(CODE_INSERT_PAGE() + " code token[1] error! -> SET Default as " + CODE_TOKEN_A4());
            pageSizeAX = DEFAULT_PAGE_SIZE_A();
        }
        if (!mifHandler.createNewPage(pageSizeAX, land, ml, mr, mt, mb)) {
            return d.returnError(result, "Cannot createNewPage->" + "fileCommonConfig.mifHandler.createNewPage(pageSizeAX:" + pageSizeAX + ", land, ml" + ml + ", mr" + mr + ", mt" + mt + ", mb" + mb + ")");
        }
        return d.returnTrue(result);
    }

    public static boolean createNewPageDefault(TS_FileCommonConfig fileCommonConfig, TS_FileTmcrFileHandler mifHandler) {
        return mifHandler.createNewPage(DEFAULT_PAGE_SIZE_A(), DEFAULT_PAGE_LAYOUT_LANDSCAPE(),
                DEFAULT_PAGE_MARGIN_LFT(), DEFAULT_PAGE_MARGIN_RGT(),
                DEFAULT_PAGE_MARGIN_TOP(), DEFAULT_PAGE_MARGIN_BTM());
    }
}
