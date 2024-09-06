package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.api.url.client.TGS_UrlQueryUtils;
import java.nio.file.Path;

public class TS_FileTmcrFileSetName {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileSetName.class);

    public static Path path(TS_FileCommonConfig fileCommonConfig, String fileNameFull) {
        return fileCommonConfig.dirDatUsrTmp.resolve(fileNameFull);
    }

    public static TGS_Url urlUser(TS_FileCommonConfig fileCommonConfig, String fileNameFull, boolean forcedownload) {
        return fileCommonConfig.libTableFileGetUtils_urlUsrTmp.call(fileNameFull, forcedownload);
    }

    public static TGS_Url urlFromPath(TS_FileCommonConfig fileCommonConfig, String imageLoc_pathOrUrl) {
        //IF URL, RETURN
        if (imageLoc_pathOrUrl.startsWith("http")) {
            d.ci("urlFromPath", "nothing to do", imageLoc_pathOrUrl);
            return TGS_Url.of(imageLoc_pathOrUrl);
        }
        var u_file = TS_PathUtils.toPath(imageLoc_pathOrUrl);
        if (u_file.isExcuse()) {
            d.ce("urlFromPath", "is it really path-able?", imageLoc_pathOrUrl, u_file.excuse().getMessage());
            return null;
        }
        var file = u_file.value();

        var dirPub = fileCommonConfig.dirDatPub;
        var dirUsr = fileCommonConfig.dirDatUsr;
        var dirTbl = fileCommonConfig.dirDatTbl;

        var isPubDir = file.startsWith(dirPub);
        var isUsrDir = file.startsWith(dirUsr);
        var isTblDir = file.startsWith(dirTbl);
        if (!isPubDir && !isUsrDir && !isTblDir) {
            d.ce("urlFromPath", "!isPubDir && !isUsrDir && !isTblDir", imageLoc_pathOrUrl);
            return null;
        }

        if (isPubDir) {
            d.ci("urlFromPath", "isPubDir", imageLoc_pathOrUrl);
            //??
            var path = file.toString().substring(dirPub.toString().length() + 1).replace("\\", "/");
            d.ci("urlFromPath", "isPubDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            imageLoc_pathOrUrl = fileCommonConfig.libFileServletUtils_URL_SERVLET_FETCH_PUBLIC.call(pathSafe);
            d.ci("urlFromPath", "isPubDir", "url", imageLoc_pathOrUrl);
        } else if (isUsrDir) {
            d.ci("urlFromPath", "isUsrDir", imageLoc_pathOrUrl);
            var path = file.toString().substring(dirUsr.toString().length() + 1).replace("\\", "/");
            d.ci("urlFromPath", "isUsrDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            imageLoc_pathOrUrl = fileCommonConfig.libFileServletUtils_URL_SERVLET_FETCH_USER.call(pathSafe);
            d.ci("urlFromPath", "isUsrDir", "url", imageLoc_pathOrUrl);
        } else {//isTblDir
            d.ci("urlFromPath", "isTblDir", imageLoc_pathOrUrl);
            //D:\dat\dat\tbl\hamtedkart\STRFILEJPG_ID\540_r0_t20210120_s115610.JPG.135.150.270.true.80.JPG
            var path = file.toString().substring(dirTbl.toString().length() + 1).replace("\\", "/");
            d.ci("urlFromPath", "isTblDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            imageLoc_pathOrUrl = fileCommonConfig.libTableServletUtils_URL_SERVLET_FETCH_TBL_FILE.call(pathSafe);
            d.ci("urlFromPath", "isTblDir", "url", imageLoc_pathOrUrl);
        }
        return TGS_Url.of(imageLoc_pathOrUrl);
    }
}
