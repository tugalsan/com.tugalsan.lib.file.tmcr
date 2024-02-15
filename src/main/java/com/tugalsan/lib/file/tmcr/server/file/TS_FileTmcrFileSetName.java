package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.api.file.server.TS_PathUtils;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.api.url.client.TGS_UrlQueryUtils;
import com.tugalsan.lib.acsrf.client.TGS_LibAcsrfParamUtils;
import com.tugalsan.lib.file.client.TGS_LibFileServletUtils;
import com.tugalsan.lib.file.server.TS_LibFilePathUtils;
import com.tugalsan.lib.table.client.TGS_LibTableServletUtils;
import com.tugalsan.lib.table.server.TS_LibTableFileDirUtils;
import com.tugalsan.lib.table.server.TS_LibTableFileGetUtils;
import java.nio.file.Path;

public class TS_FileTmcrFileSetName {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileSetName.class);

    public static Path path(TS_FileCommonBall fileCommonBall, String fileNameFull) {
        var tmpFolder = TS_LibFilePathUtils.datUsrNameTmp(fileCommonBall.dirDat, fileCommonBall.username);
        return Path.of(tmpFolder.toString(), fileNameFull);
    }

    public static TGS_Url urlUser(TS_FileCommonBall fileCommonBall, String fileNameFull, boolean forcedownload) {
        return TGS_Url.of(
                TS_LibTableFileGetUtils.urlUsrTmp(forcedownload, fileCommonBall.dirDat, fileCommonBall.url, fileCommonBall.username, fileNameFull)
        );
    }

    public static TGS_Url urlFromPath(TS_FileCommonBall fileCommonBall, String imageLoc_pathOrUrl) {
        //IF URL, RETURN
        if (imageLoc_pathOrUrl.startsWith("http")) {
            d.ci("convertLocalLocationToRemote", "nothing to do", imageLoc_pathOrUrl);
            return TGS_Url.of(imageLoc_pathOrUrl);
        }
        var file = TS_PathUtils.toPathOrError(imageLoc_pathOrUrl).value0;
        if (file == null) {
            d.ce("convertLocalLocationToRemote", "is it really path-able?", imageLoc_pathOrUrl);
            return null;
        }

        var dirDat = fileCommonBall.dirDat;
        var dirPub = TS_LibFilePathUtils.datPub(dirDat);
        var dirUsr = TS_LibFilePathUtils.datUsrName(dirDat, fileCommonBall.username);
        var dirTbl = TS_LibTableFileDirUtils.datTbl(dirDat);

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
            imageLoc_pathOrUrl = TGS_LibFileServletUtils.URL_SERVLET_FETCH_PUBLIC(false, pathSafe).toString();
            d.ci("convertLocalLocationToRemote", "isPubDir", "url", imageLoc_pathOrUrl);
        } else if (isUsrDir) {
            d.ci("convertLocalLocationToRemote", "isUsrDir", imageLoc_pathOrUrl);
            //D:\xampp_data\DAT\USR\admin\tmp\mesametal.jpg.190.30.0.true.80.jpg
            var path = file.toString().substring(dirUsr.toString().length() + 1).replace("\\", "/");
            d.ci("convertLocalLocationToRemote", "isUsrDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            var acsrfSafe = TGS_LibAcsrfParamUtils.acsrfSafe(fileCommonBall.url);
            imageLoc_pathOrUrl = TGS_LibFileServletUtils.URL_SERVLET_FETCH_USER(false, acsrfSafe, pathSafe).toString();
            d.ci("convertLocalLocationToRemote", "isUsrDir", "url", imageLoc_pathOrUrl);
        } else {//isTblDir
            d.ci("convertLocalLocationToRemote", "isTblDir", imageLoc_pathOrUrl);
            //D:\xampp_data\DAT\TBL\hamtedkart\STRFILEJPG_ID\540_r0_t20210120_s115610.JPG.135.150.270.true.80.JPG
            var path = file.toString().substring(dirTbl.toString().length() + 1).replace("\\", "/");
            d.ci("convertLocalLocationToRemote", "isTblDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            var acsrfSafe = TGS_LibAcsrfParamUtils.acsrfSafe(fileCommonBall.url);
            imageLoc_pathOrUrl = TGS_LibTableServletUtils.URL_SERVLET_FETCH_TBL_FILE(false, acsrfSafe, pathSafe).toString();
            d.ci("convertLocalLocationToRemote", "isTblDir", "url", imageLoc_pathOrUrl);
        }
        return TGS_Url.of(imageLoc_pathOrUrl);
    }
}
