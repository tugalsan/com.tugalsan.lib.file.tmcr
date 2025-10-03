package com.tugalsan.lib.file.tmcr.server.file;

import module com.tugalsan.api.file;
import module com.tugalsan.api.log;
import module com.tugalsan.lib.file.tmcr;
import module com.tugalsan.api.file.common;
import module com.tugalsan.api.string;

public class TS_LibFileTmcrFilePrefferedFileName {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrFilePrefferedFileName.class);

    public static void renameFiles_ifEnabled(TS_FileCommonAbstract mif, TS_FileCommonConfig fileCommonConfig) {
        if (!mif.isEnabled()) {
            return;
        }
        var localFilePathStr = mif.getLocalFileName().toAbsolutePath().toString();
        var type = TGS_LibFileTmcrTypes.FILE_TYPE_HTML();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(fileCommonConfig, mif, false, fileCommonConfig.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_LibFileTmcrTypes.FILE_TYPE_HTM();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(fileCommonConfig, mif, true, fileCommonConfig.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_LibFileTmcrTypes.FILE_TYPE_DOCX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(fileCommonConfig, mif, true, fileCommonConfig.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_LibFileTmcrTypes.FILE_TYPE_XLSX();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(fileCommonConfig, mif, true, fileCommonConfig.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_LibFileTmcrTypes.FILE_TYPE_PDF();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(fileCommonConfig, mif, true, fileCommonConfig.prefferedFileNameLabel + type);
            return;
        }
        type = TGS_LibFileTmcrTypes.FILE_TYPE_TMCR();
        if (localFilePathStr.endsWith(type)) {
            renameLocalFileName_do(fileCommonConfig, mif, true, fileCommonConfig.prefferedFileNameLabel + type);
            return;
        }
//        type = TGS_FileTmcrTypes.FILE_TYPE_ZIP();
//        if (localFilePathStr.endsWith(type)) {
//            renameLocalFileName_do(fileCommonConfig, mif, true, fileCommonConfig.prefferedFileNameLabel + type);
//            return;
//        }
        d.ce("renameLocalFileName2prefferedFileNameLabel_ifEnabled", "WHAT TODO WITH IT", mif.getLocalFileName());
    }

    private static void renameLocalFileName_do(TS_FileCommonConfig fileCommonConfig, TS_FileCommonAbstract mif, boolean forcedowload, String newFileNameFull) {
        d.cr("renameLocalFileName_do", "init", "localFile", mif.getLocalFileName(), "fileNameFull", newFileNameFull);
        var renamedLocalFile = TS_LibFileTmcrFileSetName.path(fileCommonConfig, newFileNameFull);
        d.ci("renameLocalFileName_do", "renamedLocalFile", renamedLocalFile);
        var renamedRemoteFile = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, newFileNameFull, forcedowload);
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

    public static void renameZip(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler fh) {
        if (TGS_StringUtils.cmn().isNullOrEmpty(fileCommonConfig.prefferedFileNameLabel)) {
            return;
        }
        var type = TGS_LibFileTmcrTypes.FILE_TYPE_ZIP();
        var newFileNameFull = fileCommonConfig.prefferedFileNameLabel + type;
        d.cr("renameZip", "init", "localFile", fh.localfileZIP, "fileNameFull", newFileNameFull);
        var renamedLocalFile = TS_LibFileTmcrFileSetName.path(fileCommonConfig, newFileNameFull);
        d.ci("renameZip", "renamedLocalFile", renamedLocalFile);
        var renamedRemoteFile = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, newFileNameFull, true);
        d.ci("renameZip", "renamedRemoteFile", renamedRemoteFile);
        if (!TS_FileUtils.isExistFile(fh.localfileZIP)) {
            d.ce("renameZip", "ERROR: File not exists", "localFile", fh.localfileZIP);
            return;
        }
        if (TS_FileUtils.moveAs(fh.localfileZIP, renamedLocalFile, true) != null) {
            fh.localfileZIP = renamedLocalFile;
            fh.remotefileZIP = renamedRemoteFile;
        }
    }

}
