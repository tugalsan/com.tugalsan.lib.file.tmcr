package com.tugalsan.api.file.tmcr.server.file;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.txt.server.TS_FileTxtUtils;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.file.tmcr.server.code.parser.TS_FileTmcrParser_Globals;

public class TS_FileTmcrFileTMCR extends TS_FileTmcrFileInterface {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileTMCR.class);

    private TS_FileTmcrParser_Globals macroGlobals;

    private TS_FileTmcrFileTMCR(boolean enabled, Path localFile, TGS_Url remoteFile) {
        super(enabled, localFile, remoteFile);
    }

    public static void use(boolean enabled, TS_FileTmcrParser_Globals macroGlobals, Path localFile, TGS_Url remoteFile, TGS_RunnableType1<TS_FileTmcrFileTMCR> tmcr) {
        var instance = new TS_FileTmcrFileTMCR(enabled, localFile, remoteFile);
        try {
            instance.use_init(macroGlobals);
            tmcr.run(instance);
        } catch (Exception e) {
            instance.saveFile(e.getMessage());
            throw e;
        } finally {
            instance.saveFile(null);
        }

    }

    private void use_init(TS_FileTmcrParser_Globals macroGlobals) {
        this.macroGlobals = macroGlobals;
        if (isClosed()) {
            return;
        }
    }

    @Override
    public boolean saveFile(String errorSource) {
        if (isClosed()) {
            return true;
        }
        setClosed();
        d.ci("saveFile.MACRO->");
        TS_FileTxtUtils.toFile(macroGlobals.macroLines, localFile, false);
        if (TS_FileUtils.isExistFile(getLocalFileName())) {
            d.ci("saveFile.FIX: MACRO File save", getLocalFileName(), "successfull");
        } else {
            d.ce("saveFile.FIX: MACRO File save", getLocalFileName(), "failed");
        }
        return errorSource == null;
    }

    @Override
    public boolean createNewPage(int pageSizeAX, boolean landscape, Integer marginLeft, Integer marginRight, Integer marginTop, Integer marginBottom) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean addImage(BufferedImage pstImage, Path pstImageLoc, boolean textWrap, int left0_center1_right2, long imageCounter) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean beginTableCell(int rowSpan, int colSpan, Integer cellHeight) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean endTableCell(int rotationInDegrees_0_90_180_270) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean beginTable(int[] relColSizes) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean endTable() {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean beginText(int allign_Left0_center1_right2_just3) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean endText() {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean addText(String text) {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean addLineBreak() {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean setFontStyle() {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean setFontHeight() {
        if (isClosed()) {
            return true;
        }
        return true;
    }

    @Override
    public boolean setFontColor() {
        if (isClosed()) {
            return true;
        }
        return true;
    }

}
