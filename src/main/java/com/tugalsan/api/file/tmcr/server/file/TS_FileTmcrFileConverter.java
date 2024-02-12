package com.tugalsan.api.file.tmcr.server.file;

import com.tugalsan.api.file.tmcr.server.code.parser.TS_FileTmcrParser_Globals;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.lib.acsrf.client.*;
import com.tugalsan.lib.boot.server.*;
import com.tugalsan.lib.file.client.*;
import com.tugalsan.lib.file.server.*;
import com.tugalsan.lib.table.client.*;
import com.tugalsan.lib.table.server.*;

public class TS_FileTmcrFileConverter {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileConverter.class);

    public static String convertLocalLocationToRemote(TS_FileTmcrParser_Globals macroGlobals, String imageLoc_pathOrUrl) {
        //IF URL, RETURN
        if (imageLoc_pathOrUrl.startsWith("http")) {
            d.ci("convertLocalLocationToRemote", "nothing to do", imageLoc_pathOrUrl);
            return imageLoc_pathOrUrl;
        }
        var file = TS_PathUtils.toPathOrError(imageLoc_pathOrUrl).value0;
        if (file == null) {
            d.ce("convertLocalLocationToRemote", "is it really path-able?", imageLoc_pathOrUrl);
            return null;
        }

        var dirDat = TS_LibBootUtils.pck.dirDAT;
        var dirPub = TS_LibFilePathUtils.datPub(dirDat);
        var dirUsr = TS_LibFilePathUtils.datUsrName(dirDat, macroGlobals.username);
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
            var acsrfSafe = TGS_LibAcsrfParamUtils.acsrfSafe(macroGlobals.url);
            imageLoc_pathOrUrl = TGS_LibFileServletUtils.URL_SERVLET_FETCH_USER(false, acsrfSafe, pathSafe).toString();
            d.ci("convertLocalLocationToRemote", "isUsrDir", "url", imageLoc_pathOrUrl);
        } else {//isTblDir
            d.ci("convertLocalLocationToRemote", "isTblDir", imageLoc_pathOrUrl);
            //D:\xampp_data\DAT\TBL\hamtedkart\STRFILEJPG_ID\540_r0_t20210120_s115610.JPG.135.150.270.true.80.JPG
            var path = file.toString().substring(dirTbl.toString().length() + 1).replace("\\", "/");
            d.ci("convertLocalLocationToRemote", "isTblDir", "path", path);
            var pathSafe = TGS_UrlQueryUtils.readable_2_Param64UrlSafe(path);
            var acsrfSafe = TGS_LibAcsrfParamUtils.acsrfSafe(macroGlobals.url);
            imageLoc_pathOrUrl = TGS_LibTableServletUtils.URL_SERVLET_FETCH_TBL_FILE(false, acsrfSafe, pathSafe).toString();
            d.ci("convertLocalLocationToRemote", "isTblDir", "url", imageLoc_pathOrUrl);
        }
        return imageLoc_pathOrUrl;
    }
}
