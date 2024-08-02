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
            d.ci("convertLocalLocationToRemote", "nothing to do", imageLoc_pathOrUrl);
            return TGS_Url.of(imageLoc_pathOrUrl);
        }
        var file = TS_PathUtils.toPath(imageLoc_pathOrUrl).value();
        if (file == null) {
            d.ce("convertLocalLocationToRemote", "is it really path-able?", imageLoc_pathOrUrl);
            return null;
        }

        var dirPub = fileCommonConfig.dirDatPub;
        var dirUsr = fileCommonConfig.dirDatUsr;
        var dirTbl = fileCommonConfig.dirDatTbl;

        var isPubDir = file.startsWith(dirPub);
        var isUsrDir = file.startsWith(dirUsr);
        var isTblDir = file.startsWith(dirTbl);
        if (!isPubDir && !isUsrDir && !isTblDir) {
            d.ce("convertLocalLocationToRemote", "!isPubDir && !isUsrDir && !isTblDir", imageLoc_pathOrUrl);
            return null;
        }

        if (isPubDir) {
            d.ci("convertLocalLocationToRemote", "isPubDir", imageLoc_pathOrUrl);
            //??
            var path = file.toString().substring(dirPub.toString().length() + 1).replace("\\", "/");
            d.ci("convertLocalLocationToRemote", "isPubDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            imageLoc_pathOrUrl = fileCommonConfig.libFileServletUtils_URL_SERVLET_FETCH_PUBLIC.call(pathSafe);
            d.ci("convertLocalLocationToRemote", "isPubDir", "url", imageLoc_pathOrUrl);
        } else if (isUsrDir) {
            d.ci("convertLocalLocationToRemote", "isUsrDir", imageLoc_pathOrUrl);
            var path = file.toString().substring(dirUsr.toString().length() + 1).replace("\\", "/");
            d.ci("convertLocalLocationToRemote", "isUsrDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            imageLoc_pathOrUrl = fileCommonConfig.libFileServletUtils_URL_SERVLET_FETCH_USER.call(pathSafe);
            d.ci("convertLocalLocationToRemote", "isUsrDir", "url", imageLoc_pathOrUrl);
        } else {//isTblDir
            d.ci("convertLocalLocationToRemote", "isTblDir", imageLoc_pathOrUrl);
            //D:\xampp_data\DAT\TBL\hamtedkart\STRFILEJPG_ID\540_r0_t20210120_s115610.JPG.135.150.270.true.80.JPG
            var path = file.toString().substring(dirTbl.toString().length() + 1).replace("\\", "/");
            d.ci("convertLocalLocationToRemote", "isTblDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            imageLoc_pathOrUrl = fileCommonConfig.libTableServletUtils_URL_SERVLET_FETCH_TBL_FILE.call(pathSafe);
            d.ci("convertLocalLocationToRemote", "isTblDir", "url", imageLoc_pathOrUrl);
        }
        return TGS_Url.of(imageLoc_pathOrUrl);
    }
}
