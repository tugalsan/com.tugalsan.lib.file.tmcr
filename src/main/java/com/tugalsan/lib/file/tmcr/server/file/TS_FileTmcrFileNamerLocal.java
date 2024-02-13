package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.file.common.server.TS_FileCommonInterface;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.lib.file.server.TS_LibFilePathUtils;
import com.tugalsan.lib.file.tmcr.client.TGS_FileTmcrTypes;
import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.lib.table.server.TS_LibTableFileGetUtils;
import java.nio.file.Path;

public class TS_FileTmcrFileNamerLocal {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileNamerLocal.class);

    public static Path constructPathForLocalFile(Path dirDat, String fileNameFull, String username) {
        var tmpFolder = TS_LibFilePathUtils.datUsrNameTmp(dirDat, username);
        return Path.of(tmpFolder.toString(), fileNameFull);
    }

    public static TGS_Url constructURLForRemoteFile(boolean forcedownload, Path dirDat, String fileNameFull, String username, TGS_Url url) {
        return TGS_Url.of(
                TS_LibTableFileGetUtils.urlUsrTmp(forcedownload, dirDat, url, username, fileNameFull)
        );
    }

    static public void renameLocalFileName_ifEnabled(TS_FileCommonInterface mif, Path dirDat, TS_FileCommonBall macroGlobals) {
        if (!mif.isEnabled()) {
            return;
        }
        var localFilePathStr = mif.getLocalFileName().toAbsolutePath().toString();
        var type = TGS_FileTmcrTypes.FILE_TYPE_HTML();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, false, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_HTM();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_DOCX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_XLSX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_PDF();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_TMCR();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_ZIP();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(mif, true, dirDat, macroGlobals.prefferedFileNameLabel + type, macroGlobals.username, macroGlobals.url);
            return;
        }
    }

    private static void renameLocalFileName_do(TS_FileCommonInterface mif, boolean forcedowload, Path dirDat, String newFileNameFull, String username, TGS_Url url) {
        d.cr("renameLocalFileName_do", "init", "localFile", mif.getLocalFileName(), "fileNameFull", newFileNameFull);
        var renamedLocalFile = constructPathForLocalFile(dirDat, newFileNameFull, username);
        d.ci("renameLocalFileName_do", "renamedLocalFile", renamedLocalFile);
        var renamedRemoteFile = constructURLForRemoteFile(forcedowload, dirDat, newFileNameFull, username, url);
        d.ci("renameLocalFileName_do", "renamedRemoteFile", renamedRemoteFile);
        if (!TS_FileUtils.isExistFile(mif.getLocalFileName())) {
            d.ce("renameLocalFileName_do", "ERROR: File not exists", "localFile", mif.getLocalFileName());
            return;
        }
        if (TS_FileUtils.moveAs(mif.getLocalFileName(), renamedLocalFile, true) != null) {
            mif.setLocalFileName(renamedLocalFile);
            mif.setRemoteFileName(renamedRemoteFile);
        }
    }
}
