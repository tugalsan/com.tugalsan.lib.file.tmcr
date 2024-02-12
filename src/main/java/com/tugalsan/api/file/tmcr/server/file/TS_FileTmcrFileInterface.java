package com.tugalsan.api.file.tmcr.server.file;

import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.url.client.*;
import com.tugalsan.lib.file.server.*;
import com.tugalsan.api.file.tmcr.client.TGS_FileTmcrTypes;
import com.tugalsan.api.file.tmcr.server.code.parser.*;
import com.tugalsan.lib.table.server.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Objects;

public abstract class TS_FileTmcrFileInterface {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileInterface.class);
    final public static boolean FILENAME_CHAR_SUPPORT_TURKISH = true;
    final public static boolean FILENAME_CHAR_SUPPORT_SPACE = true;

    @Override
    public int hashCode() {
        var hash = 5;
        hash = 73 * hash + Objects.hashCode(this.localFile);
        hash = 73 * hash + Objects.hashCode(this.remoteFile);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        var other = (TS_FileTmcrFileInterface) obj;
        if (!Objects.equals(this.localFile, other.localFile)) {
            return false;
        }
        return Objects.equals(this.remoteFile, other.remoteFile);
    }

    public static Path constructPathForLocalFile(Path dirDat, String fileNameFull, String username) {
        var tmpFolder = TS_LibFilePathUtils.datUsrNameTmp(dirDat, username);
        return Path.of(tmpFolder.toString(), fileNameFull);
    }

    public static TGS_Url constructURLForRemoteFile(boolean forcedownload, Path dirDat, String fileNameFull, String username, TGS_Url url) {
        return TGS_Url.of(
                TS_LibTableFileGetUtils.urlUsrTmp(forcedownload, dirDat, url, username, fileNameFull)
        );
    }

    public TS_FileTmcrFileInterface(boolean enabled, Path localFile, TGS_Url remoteFile) {
        this.localFile = localFile;
        this.remoteFile = remoteFile;
        isEnabled = enabled;
        if (!enabled) {
            setClosed();
        }
    }

    public boolean isEnabled() {
        return isEnabled;
    }
    private boolean isEnabled = false;

    public boolean isClosed() {
        return isClosed;
    }
    private boolean isClosed = false;

    final protected void setClosed() {
        isClosed = true;
    }

    final public void renameLocalFileName_ifEnabled(Path dirDat, TS_FileTmcrParser_Globals macroGlobals) {
        if (!isEnabled()) {
            return;
        }
        var localFilePathStr = localFile.toAbsolutePath().toString();
        var type = TGS_FileTmcrTypes.FILE_TYPE_HTML();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(false, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_HTM();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_DOCX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_XLSX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_PDF();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_TMCR();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_ZIP();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
    }

    private void renameLocalFileName_do(boolean forcedowload, Path dirDat, String newFileNameFull, String username, TGS_Url url) {
        d.cr("renameLocalFileName_do", "init", "localFile", localFile, "fileNameFull", newFileNameFull);
        var renamedLocalFile = constructPathForLocalFile(dirDat, newFileNameFull, username);
        d.ci("renameLocalFileName_do", "renamedLocalFile", renamedLocalFile);
        var renamedRemoteFile = constructURLForRemoteFile(forcedowload, dirDat, newFileNameFull, username, url);
        d.ci("renameLocalFileName_do", "renamedRemoteFile", renamedRemoteFile);
        if (!TS_FileUtils.isExistFile(localFile)) {
            d.ce("renameLocalFileName_do", "ERROR: File not exists", "localFile", localFile);
            return;
        }
        if (TS_FileUtils.moveAs(localFile, renamedLocalFile, true) != null) {
            localFile = renamedLocalFile;
            remoteFile = renamedRemoteFile;
        }
    }

    final public Path getLocalFileName() {
        return localFile;
    }
    protected Path localFile;

    final public TGS_Url getRemoteFileName() {
        return remoteFile;
    }
    protected TGS_Url remoteFile;

    public abstract boolean saveFile(String errorSource);

    public abstract boolean createNewPage(int pageSizeAX, boolean landscape, Integer marginLeft, Integer marginRight, Integer marginTop, Integer marginBottom);

    public abstract boolean addImage(BufferedImage pstImage, Path pstImageLoc, boolean textWrap, int left0_center1_right2, long imageCounter);

    public abstract boolean beginTableCell(int rowSpan, int colSpan, Integer cellHeight);

    public abstract boolean endTableCell(int rotationInDegrees_0_90_180_270);

    public abstract boolean beginTable(int[] relColSizes);

    public abstract boolean endTable();

    public abstract boolean beginText(int allign_Left0_center1_right2_just3);

    public abstract boolean endText();

    public abstract boolean addText(String text);

    public abstract boolean addLineBreak();

    public abstract boolean setFontStyle();

    public abstract boolean setFontHeight();

    public abstract boolean setFontColor();

    public void skipCloseFix() {

    }
}
