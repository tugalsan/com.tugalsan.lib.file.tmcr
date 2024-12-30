package com.tugalsan.lib.file.tmcr.server.code.image;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.file.img.server.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.random.server.TS_RandomUtils;
import com.tugalsan.api.shape.client.*;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_LibFileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.thread.server.TS_ThreadWait;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_LibFileTmcrParser_SelectedId;
import com.tugalsan.lib.file.tmcr.server.file.TS_LibFileTmcrFileHandler;
import com.tugalsan.lib.rql.client.*;
import java.awt.image.*;
import java.nio.file.*;
import java.util.*;

public class TS_LibFileTmcrCodeImageCompile {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeImageCompile.class);

    public static int DEFAULT_QUALITY() {
        return 80;
    }

    public static float FULL_QUALITY_FILE_SIZE_THREASHOLD_AS_BYTES = 0.5f * 1024 * 1024;

    public static boolean is_INSERT_IMAGE(TS_FileCommonConfig fileCommonConfig) {
        return fileCommonConfig.macroLine.startsWith(TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE());
    }

    public static TS_Log.Result_withLog compile_INSERT_IMAGE(TS_FileCommonConfig fileCommonConfig, TS_LibFileTmcrFileHandler mifHandler) {
        var result = d.createFuncBoolean(TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE() + "/" + TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL());
        d.ci(result.classNameDotfuncName, "INFO: macroLine: " + fileCommonConfig.macroLineUpperCase);
        d.ci(result.classNameDotfuncName, "INFO: dirDat: " + fileCommonConfig.dirDat);

        //WHICH CODE
        boolean fromSQL;
        if (fileCommonConfig.macroLineUpperCase.startsWith(TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL())) {
            fromSQL = true;
            if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 10)) {
                return result.mutate2Error("ERROR: on code " + TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL() + ", tokensize should be 10");
            }
        } else {//CODE_INSERT_IMAGE
            fromSQL = false;
            if (!TS_LibFileTmcrParser_Assure.checkTokenSize(fileCommonConfig, 8)) {
                return result.mutate2Error("ERROR: on code " + TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE() + ", tokensize should be 8");
            }
        }
        d.ci(result.classNameDotfuncName, "INFO: fromSQL: " + fromSQL);

        //GET ROTATION
        var rotationIsLandscape = false;
        var rotationIsPortrait = false;
        var rotationTAG = fileCommonConfig.macroLineTokens.get(fromSQL ? 8 : 7);
        var r = TGS_CastUtils.toInteger(rotationTAG);
        if (r == null) {
            if (Objects.equals(rotationTAG, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LANDSCAPE())) {
                rotationIsLandscape = true;
                r = 0;
            } else if (Objects.equals(rotationTAG, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_PORTRAIT())) {
                rotationIsPortrait = true;
                r = 0;
            } else {
                return result.mutate2Error("ERROR: code token[" + rotationTAG + "] error! rotation -> [" + r + "]");
            }
        } else if (!(r == 0 || r == 90 || r == 180 || r == 270)) {
            return result.mutate2Error("ERROR: code token[" + rotationTAG + "] error! rotation is not !(r == 0 || r == 90 || r == 180 || r == 270) ->[" + r + "]");
        }
        d.ci(result.classNameDotfuncName, "INFO: rotation is : " + r);

        //GET TEXTWRAP
        boolean textWrap;
        var wrapText = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(5));
        if (Objects.equals(wrapText, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP())) {
            textWrap = true;
        } else if (Objects.equals(wrapText, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
            textWrap = false;
        } else {
            return result.mutate2Error("ERROR: code token[5] error! should be either" + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP() + " or " + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        }
        d.ci(result.classNameDotfuncName, "INFO: textWrap is : " + textWrap);

        //GET ALLIGN
        int left0_center1_right2;
        var allignText = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(4));
        if (Objects.equals(allignText, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LEFT())) {
            left0_center1_right2 = 0;
        } else if (Objects.equals(allignText, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CENTER())) {
            left0_center1_right2 = 1;
        } else if (Objects.equals(allignText, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LEFT())) {
            left0_center1_right2 = 2;
        } else {
            return result.mutate2Error("ERROR: code token[4] error! should be either" + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LEFT() + " or " + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CENTER() + " or " + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RIGHT());
        }
        d.ci(result.classNameDotfuncName, "INFO: left0_center1_right2 is : " + left0_center1_right2);

        //GET WIDTH
        var w = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(1));
        if (w == null && !TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLineTokens.get(1), TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
            return result.mutate2Error("ERROR: code token[1] error! width is : " + w);
        }
        d.ci(result.classNameDotfuncName, "INFO: width is : " + w);
//        final var final_w = w;

        //GET HEIGHT
        var h = TGS_CastUtils.toInteger(fileCommonConfig.macroLineTokens.get(2));
        if (h == null && !TGS_CharSetCast.current().equalsIgnoreCase(fileCommonConfig.macroLineTokens.get(2), TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
            return result.mutate2Error("ERROR: code token[2] error! height is : " + h);
        }
        d.ci(result.classNameDotfuncName, "INFO: height is : " + h);
        final var final_h = h;

        //GET RESPECT
        boolean respect;
        var respectTXT = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(3));
        if (Objects.equals(respectTXT, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RESPECT())) {
            respect = true;
        } else if (Objects.equals(respectTXT, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RESPECT())) {
            respect = false;
        } else {
            return result.mutate2Error("ERROR: code token[3] error! should be either" + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RESPECT() + " or " + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        }
        d.ci(result.classNameDotfuncName, "INFO: respect is : " + respect);

        //GET PREIMAGE
        Path preImageLoc = null;
        if (fromSQL) {//FROM SQL
            //GET CREATE
            boolean create;
            var createTXT = TGS_CharSetCast.current().toUpperCase(fileCommonConfig.macroLineTokens.get(9));
            if (Objects.equals(createTXT, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CREATE())) {
                create = true;
            } else if (Objects.equals(createTXT, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
                create = false;
            } else {
                return result.mutate2Error("ERROR: code token[9] error! should be either" + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CREATE() + " or " + TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
            }
            d.ci(result.classNameDotfuncName, "INFO: create is : " + create);

            //GET imageTable AND imgColIdx
            TGS_LibRqlTbl imageTable;
            Integer imgColIdx;
            {
                //GET tableAndColname
                var tableAndColname = TS_LibFileTmcrParser_Assure.splitTableDotColname(fileCommonConfig, 6);
                if (tableAndColname == null) {
                    return result.mutate2Error("ERROR: fromSQL.code token[6] error! tableAndColname:" + fileCommonConfig.macroLineTokens.get(6));
                }
                d.ci(result.classNameDotfuncName, "INFO: fromSQL.tableAndColname is : " + tableAndColname.toString());

                imageTable = TS_LibFileTmcrParser_Assure.getTable(fileCommonConfig, tableAndColname.tableName());
                if (imageTable == null) {
                    return result.mutate2Error("ERROR: fromSQL.code token[6] error! imageTable == null: " + fileCommonConfig.macroLineTokens.get(6));
                }
                if (d.infoEnable) {
                    var imageTableName = imageTable.nameSql;
                    d.ci(result.classNameDotfuncName, "INFO: fromSQL.imageTable", imageTableName);
                }

                imgColIdx = TS_LibFileTmcrParser_Assure.getColumnIndex(fileCommonConfig, imageTable, tableAndColname.colname());
                if (imgColIdx == null) {
                    return result.mutate2Error("ERROR: fromSQL.code token[6] error! imgColIdx == null: " + fileCommonConfig.macroLineTokens.get(6));
                }
                d.ci(result.classNameDotfuncName, "INFO: fromSQL.imageColumnname and idx is : " + tableAndColname.colname() + " - " + imgColIdx);
            }
            var imageTableName = imageTable.nameSql;
            var imageTableColumn = imageTable.columns.get(imgColIdx);
            d.ci(result.classNameDotfuncName, "INFO: fromSQL.imageTableColumn.getColumnName() is : " + imageTableColumn.getColumnName());

            //GET ID
            Long id;
            if (fileCommonConfig.macroLineTokens.get(7).equals(TS_LibFileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
                id = fileCommonConfig.selectedId;
                if (id == null) {
                    return result.mutate2Error("ERROR: fromSQL.code token[7] error! SATIR SEÇİLMEDİ HATASI");
                }
            } else {
                id = TGS_CastUtils.toLong(fileCommonConfig.macroLineTokens.get(7));
                if (id == null) {
                    return result.mutate2Error("ERROR: fromSQL.code token[7] should be a number");
                }
            }
            d.ci(result.classNameDotfuncName, "INFO: fromSQL.id is : " + id);

            //GET FN
            String fn = null;
            {
                var fns = fileCommonConfig.libTableFileList_getFileNames_DataIn.call(
                        imageTableName, imageTableColumn.getColumnName(), id,
                        imageTableColumn.getDataString1_LnkTargetTableName(), create
                );
                if (fns.isEmpty()) {
                    d.ci(result.classNameDotfuncName, "INFO fromSQL.TS_AppFileListUtils.getFileNames_DataIn.isEmpty()");
                    if (create) {
                        return result.mutate2Error("ERROR: fromSQL.RESİM BULUNAMADI HATASI @create");
                    } else {
                        d.ci(result.classNameDotfuncName, "INFO fromSQL.ATLANDI: RESİM BULUNAMADI HATASI");
                    }
                } else {
                    d.ci(result.classNameDotfuncName, "INFO fromSQL.TS_AppFileListUtils.getFileNames_DataIn.isEmpty() == false");
                    fn = null;
                    String newFn;
                    for (var i = 0; i < fns.size(); i++) {//skip the already resized ones
                        newFn = fns.get(i);
                        if (fn != null && Math.abs(newFn.length() - fn.length()) > 5) {//time can be one or two digits
                            continue;
                        }
                        fn = newFn;
                    }
                    if (fn == null) {
                        return result.mutate2Error("ERROR: fromSQL.skip alredy resized onces->fn==null");
                    }
                }
            }
            d.ci(result.classNameDotfuncName, "INFO: fromSQL.fn is : " + fn);

            //GET preImageLoc
            if (fn != null) {
                var getInBox = fileCommonConfig.libTableFileDir_datTblTblnameColname.call(
                        imageTableName, imageTableColumn.getColumnName()
                );
                preImageLoc = Path.of(getInBox.toString(), fn);
            }
            d.ci(result.classNameDotfuncName, "INFO: fromSQL.preImageLoc: " + preImageLoc);
        } else {//FROM URL
            var ref = fileCommonConfig.macroLineTokens.get(6);
            d.ci(result.classNameDotfuncName, "fromUrl", "ref.init", ref);
            var u_file = TS_PathUtils.toPath(ref);
            if (u_file.isExcuse()) {
                d.ci(result.classNameDotfuncName, "warning", "ref.macro", "is it really path-able?", ref, u_file.excuse().getMessage());
                preImageLoc = null;
            } else {
                preImageLoc = u_file.value();
            }
            d.ci(result.classNameDotfuncName, "fromUrl", "preImageLoc.try", preImageLoc);
            if (preImageLoc == null) {//FIX: TRY TO DOWNLOAD URL TO TEMP
                var idxLastSlash = ref.lastIndexOf("/");
                if (idxLastSlash == -1) {
                    return result.mutate2Error("ERROR: preImageLoc ->  idxLastSlash == -1 -> fromFile.ref :[" + ref + "]");
                }
                var filename = ref.substring(idxLastSlash + 1);
                d.ci(result.classNameDotfuncName, "fromUrl", "filename", filename);
                var randomStr = TS_RandomUtils.nextString(10, true, true, false, false, null);
                var fileTmp = fileCommonConfig.dirDatUsrTmp.resolve(randomStr + filename);
                d.ci(result.classNameDotfuncName, "fromUrl", "fileTmp", fileTmp);
                var u_refTmp = TS_UrlDownloadUtils.toFile(TGS_Url.of(ref), fileTmp);
                if (u_refTmp.isExcuse()) {
                    return result.mutate2Error("ERROR: preImageLoc ->  refTmp says " + u_refTmp.excuse().getMessage() + " -> fromFile.ref :[" + ref + "]");
                }
                ref = fileTmp.toString();
                d.ci(result.classNameDotfuncName, "fromUrl", "ref.updated", ref);
            }
            fileCommonConfig.macroLineTokens.set(6, ref);

            u_file = TS_PathUtils.toPath(ref);
            if (u_file.isExcuse()) {
                d.ce(result.classNameDotfuncName, "warning", "convertLocalLocationToRemote#2", "is it really path-able?", ref, u_file.excuse().getMessage());
                preImageLoc = null;
            } else {
                preImageLoc = u_file.value();
            }
            d.ci(result.classNameDotfuncName, "fromUrl", "preImageLoc.updated", preImageLoc);
        }

        //CHECK preImageLoc
        if (preImageLoc == null) {
            return result.mutate2Error("ERROR: preImageLoc == null");
        }
        if (!TS_FileUtils.isExistFile(preImageLoc)) {
            return result.mutate2Error("ERROR: preImageLoc -> !TS_FileUtils.isExistFile(preImageLoc):[" + preImageLoc + "]");
        }

        //GET preImage
        BufferedImage preImage;
        try {
            preImage = TS_FileImageUtils.toImage(preImageLoc);
            if (preImage == null) {
                return result.mutate2Error("ERROR: preImage == null @ preImageLoc:" + preImageLoc);
            }
        } catch (Exception e) {
            TGS_UnSafe.throwIfInterruptedException(e);
            mifHandler.addText("ERROR: preImage == e @ preImageLoc:" + preImageLoc + ", e:" + e.getMessage());
            d.ci(result.classNameDotfuncName, "  processed.");
            d.ci(result.classNameDotfuncName, "compile_INSERT_IMAGE_COMMON  processed. ");
            return result.mutate2True();
        }

        //INIT W and H
        {
            var preImageW = preImage.getWidth();
            var preImageH = preImage.getHeight();
            d.ci(result.classNameDotfuncName, "INFO: preImageW/H is : " + preImageW + "/" + preImageH);
            d.ci(result.classNameDotfuncName, "INFO: #1 w/h is : " + w + "/" + h);
            if (w == null && h == null) {
                d.ci(result.classNameDotfuncName, "INFO: will set w/h");
                w = preImageW;
                h = preImageH;
            } else if (w == null) {
                d.ci(result.classNameDotfuncName, "INFO: will set w");
                w = (int) (1d * preImageW * h / preImageH);
            } else if (h == null) {
                d.ci(result.classNameDotfuncName, "INFO: will set h");
                h = (int) (1d * preImageH * w / preImageW);
            }
            d.ci(result.classNameDotfuncName, "INFO: #2 w/h is : " + w + "/" + h);

            d.ci(result.classNameDotfuncName, "INFO: #4: CHANGE W and H by rotate", "w,h", w, h);
            if (r == 90 || r == 270) {//rotate
                d.ci(result.classNameDotfuncName, "INFO: #4.1 DETECTED r == 90 || r == 270");
                Integer tmp = w;//var is problematic!!
                w = h;
                h = tmp;
            }

            d.ci(result.classNameDotfuncName, "INFO: #4: CHANGE W and H by rotationIsLandscape", "w,h", w, h);
            if (rotationIsLandscape && w < h) {
                d.ci(result.classNameDotfuncName, "INFO: #4.2 DETECTED rotationIsLandscape && w < h");
                r = 90;
                Integer tmp = w;//var is problematic!!
                w = h;
                h = tmp;
            }

            d.ci(result.classNameDotfuncName, "INFO: #4: CHANGE W and H by rotationIsPortrait", "w,h", w, h);
            if (rotationIsPortrait && w > h) {
                d.ci(result.classNameDotfuncName, "INFO: #4.3 DETECTED rotationIsPortrait && w > h");
                r = 90;
                Integer tmp = w;//var is problematic!!
                w = h;
                h = tmp;
            }

            //CHANGE W and H by cellHeight
            d.ci(result.classNameDotfuncName, "INFO: #5.0: CHANGE W and H by cellHeight", "w,h", w, h);
            if (final_h == null) {
                if (fileCommonConfig.cellHeight == null) {
                    d.ci(result.classNameDotfuncName, "INFO: #5.1 SKIP fileCommonConfig.cellHeight == null");
                } else if (fileCommonConfig.cellHeight >= h) {
                    d.ci(result.classNameDotfuncName, "INFO: #5.2 DETECTED fileCommonConfig.cellHeight >= h", "while cellHeight", fileCommonConfig.cellHeight);
                    var newImageWidth = 1d * fileCommonConfig.cellHeight * w / h;
                    w = (int) Math.round(newImageWidth);
                    h = fileCommonConfig.cellHeight;
                } else {
                    d.ci(result.classNameDotfuncName, "INFO: #5.3 DETECTED fileCommonConfig.cellHeight < h", "while cellHeight", fileCommonConfig.cellHeight);
                    var newImageWidth = 1d * fileCommonConfig.cellHeight * w / h;
                    w = (int) Math.round(newImageWidth);
                    h = fileCommonConfig.cellHeight;
                }
                d.ci(result.classNameDotfuncName, "INFO: #5.4 w/h is : " + w + "/" + h);
            } else {
                d.ci(result.classNameDotfuncName, "INFO: #5.4 w/h is (skipped): " + w + "/" + h);
            }

            d.ci(result.classNameDotfuncName, "INFO: #6 w/h is : " + w + "/" + h);
        }

        var quality = DEFAULT_QUALITY();
        var pstImageLoc = Path.of(preImageLoc.toString() + "." + w + "." + h + "." + r + "." + respect + "." + quality + "." + TS_FileUtils.getNameType(preImageLoc));
        d.ci(result.classNameDotfuncName, "INFO: pstImageLoc is : " + pstImageLoc);

        BufferedImage pstImage;
        if (TS_FileUtils.isExistFile(pstImageLoc)) {
            d.ci(result.classNameDotfuncName, "INFO: pstImageLoc found -> notProcessed :)");
            pstImage = TS_FileImageUtils.toImage(pstImageLoc);
        } else {
            d.ci(result.classNameDotfuncName, "INFO: pstImageLoc notFOund -> processing...");
            pstImage = TS_FileImageUtils.resize_and_rotate(preImage, new TGS_ShapeDimension(w, h), r, respect);
            w = pstImage.getWidth();
            h = pstImage.getHeight();
//            System.out.println("AAAAAAAAA- AYARLI QUALITY:" + quality);
            TS_FileImageUtils.toFile(pstImage, pstImageLoc, 1);
            TS_ThreadWait.milliseconds200();
            if (TS_FileUtils.getFileSizeInBytes(pstImageLoc) > FULL_QUALITY_FILE_SIZE_THREASHOLD_AS_BYTES) {
                TS_FileImageUtils.toFile(pstImage, pstImageLoc, quality / 100f);
            }
        }
        if (pstImage == null) {
            return result.mutate2Error("ERROR: pstImage == null @ pstImageLoc:" + pstImageLoc);
        }

        d.ci(result.classNameDotfuncName, "INFO: pstImage w/h is : " + w + "/" + h);
        d.ci(result.classNameDotfuncName, "INFO: pstImageLoc: " + pstImageLoc);
        d.ci(result.classNameDotfuncName, "INFO: pstImageType: " + pstImage.getType());
        d.ci(result.classNameDotfuncName, "INFO: END");

        if (!mifHandler.addImage(pstImage, pstImageLoc, textWrap, left0_center1_right2, fileCommonConfig.imageCounter++)) {
            return result.mutate2Error("ERR@ " + pstImageLoc);
        }
        d.ci(result.classNameDotfuncName, "compile_INSERT_IMAGE_COMMON  processed. ");
        return result.mutate2True();
    }
}
