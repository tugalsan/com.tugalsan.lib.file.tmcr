package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.file.common.server.TS_FileCommonInterface;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.lib.file.tmcr.client.TGS_FileTmcrTypes;
import com.tugalsan.api.file.common.server.TS_FileCommonBall;

public class TS_FileTmcrFileReName {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileReName.class);

    public static void renameLocalFileName2prefferedFileNameLabel_ifEnabled(TS_FileCommonInterface mif, TS_FileCommonBall fileCommonBall) {
        if (!mif.isEnabled()) {
            return;
        }
        var localFilePathStr = mif.getLocalFileName().toAbsolutePath().toString();
        var type = TGS_FileTmcrTypes.FILE_TYPE_HTML();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, false, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_HTM();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, true, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_DOCX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, true, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_XLSX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, true, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_PDF();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, true, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_TMCR();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, true, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_FileTmcrTypes.FILE_TYPE_ZIP();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(fileCommonBall, mif, true, fileCommonBall.prefferedFileNameLabel + type);
            return;
        }
    }

    private static void renameLocalFileName2prefferedFileNameLabel_ifEnabled_do(TS_FileCommonBall fileCommonBall, TS_FileCommonInterface mif, boolean forcedowload, String newFileNameFull) {
        d.cr("renameLocalFileName_do", "init", "localFile", mif.getLocalFileName(), "fileNameFull", newFileNameFull);
        var renamedLocalFile = TS_FileTmcrFileSetName.path(fileCommonBall, newFileNameFull);
        d.ci("renameLocalFileName_do", "renamedLocalFile", renamedLocalFile);
        var renamedRemoteFile = TS_FileTmcrFileSetName.urlUser(fileCommonBall, newFileNameFull, forcedowload);
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
