package com.tugalsan.lib.file.tmcr.server.code.image;

import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.file.img.server.*;
import com.tugalsan.api.file.server.*;
import com.tugalsan.api.list.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.random.server.TS_RandomUtils;
import com.tugalsan.api.shape.client.*;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.url.client.*;
import com.tugalsan.api.url.server.*;
import com.tugalsan.lib.file.server.*;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_Assure;
import com.tugalsan.api.file.common.server.TS_FileCommonBall;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser_SelectedId;
import com.tugalsan.lib.file.tmcr.server.file.TS_FileTmcrFileHandler;
import com.tugalsan.lib.resource.client.*;
import com.tugalsan.lib.rql.client.*;
import com.tugalsan.lib.table.server.*;
import java.awt.image.*;
import java.nio.file.*;
import java.util.*;

public class TS_FileTmcrCodeImageCompile {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrCodeImageCompile.class);

    public static int DEFAULT_QUALITY() {
        return 80;
    }

    public static boolean is_INSERT_IMAGE(TS_FileCommonBall fileCommonBall) {
        return fileCommonBall.macroLine.startsWith(TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE());
    }

    /*
    
    TODO ITEXT IMAGE
    String imageFile = "C:/itextExamples/javafxLogo.jpg";       
      ImageData data = ImageDataFactory.create(imageFile);        

      // Creating the image       
      Image img = new Image(data);              

      // Adding image to the cell10       
      cell10.add(img.setAutoScale(true));        

      // Adding cell110 to the table       
      table.addCell(cell10);                 
    
     */
    public static TGS_Tuple3<String, Boolean, String> compile_INSERT_IMAGE(TS_FileCommonBall fileCommonBall, TS_FileTmcrFileHandler mifHandler, Path dirDat) {
        var result = d.createFuncBoolean(TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE() + "/" + TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL());
        d.ci(result.value0, "INFO: macroLine: " + fileCommonBall.macroLineUpperCase);
        d.ci(result.value0, "INFO: dirDat: " + dirDat.toString());

        //WHICH CODE
        boolean fromSQL;
        if (fileCommonBall.macroLineUpperCase.startsWith(TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL())) {
            fromSQL = true;
            if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonBall, 10)) {
                return d.returnError(result, "ERROR: on code " + TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL() + ", tokensize should be 10");
            }
        } else {//CODE_INSERT_IMAGE
            fromSQL = false;
            if (!TS_FileTmcrParser_Assure.checkTokenSize(fileCommonBall, 8)) {
                return d.returnError(result, "ERROR: on code " + TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE() + ", tokensize should be 8");
            }
        }
        d.ci(result.value0, "INFO: fromSQL: " + fromSQL);

        //GET ROTATION
        var rotationIsLandscape = false;
        var rotationIsPortrait = false;
        var rotationTAG = fileCommonBall.macroLineTokens.get(fromSQL ? 8 : 7);
        var r = TGS_CastUtils.toInteger(rotationTAG);
        if (r == null) {
            if (Objects.equals(rotationTAG, TS_FileTmcrCodeImageTags.CODE_TOKEN_LANDSCAPE())) {
                rotationIsLandscape = true;
                r = 0;
            } else if (Objects.equals(rotationTAG, TS_FileTmcrCodeImageTags.CODE_TOKEN_PORTRAIT())) {
                rotationIsPortrait = true;
                r = 0;
            } else {
                return d.returnError(result, "ERROR: code token[" + rotationTAG + "] error! rotation -> [" + r + "]");
            }
        } else if (!(r == 0 || r == 90 || r == 180 || r == 270)) {
            return d.returnError(result, "ERROR: code token[" + rotationTAG + "] error! rotation is not !(r == 0 || r == 90 || r == 180 || r == 270) ->[" + r + "]");
        }
        d.ci(result.value0, "INFO: rotation is : " + r);

        //GET TEXTWRAP
        boolean textWrap;
        var wrapText = TGS_CharSetCast.toLocaleUpperCase(fileCommonBall.macroLineTokens.get(5));
        if (Objects.equals(wrapText, TS_FileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP())) {
            textWrap = true;
        } else if (Objects.equals(wrapText, TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
            textWrap = false;
        } else {
            return d.returnError(result, "ERROR: code token[5] error! should be either" + TS_FileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP() + " or " + TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL());
        }
        d.ci(result.value0, "INFO: textWrap is : " + textWrap);

        //GET ALLIGN
        int left0_center1_right2;
        var allignText = TGS_CharSetCast.toLocaleUpperCase(fileCommonBall.macroLineTokens.get(4));
        if (Objects.equals(allignText, TS_FileTmcrCodeImageTags.CODE_TOKEN_LEFT())) {
            left0_center1_right2 = 0;
        } else if (Objects.equals(allignText, TS_FileTmcrCodeImageTags.CODE_TOKEN_CENTER())) {
            left0_center1_right2 = 1;
        } else if (Objects.equals(allignText, TS_FileTmcrCodeImageTags.CODE_TOKEN_LEFT())) {
            left0_center1_right2 = 2;
        } else {
            return d.returnError(result, "ERROR: code token[4] error! should be either" + TS_FileTmcrCodeImageTags.CODE_TOKEN_LEFT() + " or " + TS_FileTmcrCodeImageTags.CODE_TOKEN_CENTER() + " or " + TS_FileTmcrCodeImageTags.CODE_TOKEN_RIGHT());
        }
        d.ci(result.value0, "INFO: left0_center1_right2 is : " + left0_center1_right2);

        //GET WIDTH
        var w = TGS_CastUtils.toInteger(fileCommonBall.macroLineTokens.get(1));
        if (w == null && !TGS_CharSetCast.equalsLocaleIgnoreCase(fileCommonBall.macroLineTokens.get(1), TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
            return d.returnError(result, "ERROR: code token[1] error! width is : " + w);
        }
        d.ci(result.value0, "INFO: width is : " + w);

        //GET HEIGHT
        var h = TGS_CastUtils.toInteger(fileCommonBall.macroLineTokens.get(2));
        if (h == null && !TGS_CharSetCast.equalsLocaleIgnoreCase(fileCommonBall.macroLineTokens.get(2), TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
            return d.returnError(result, "ERROR: code token[2] error! height is : " + h);
        }
        d.ci(result.value0, "INFO: height is : " + h);

        //GET RESPECT
        boolean respect;
        var respectTXT = TGS_CharSetCast.toLocaleUpperCase(fileCommonBall.macroLineTokens.get(3));
        if (Objects.equals(respectTXT, TS_FileTmcrCodeImageTags.CODE_TOKEN_RESPECT())) {
            respect = true;
        } else if (Objects.equals(respectTXT, TS_FileTmcrCodeImageTags.CODE_TOKEN_RESPECT())) {
            respect = false;
        } else {
            return d.returnError(result, "ERROR: code token[3] error! should be either" + TS_FileTmcrCodeImageTags.CODE_TOKEN_RESPECT() + " or " + TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL());
        }
        d.ci(result.value0, "INFO: respect is : " + respect);

        //GET PREIMAGE
        Path preImageLoc = null;
        if (fromSQL) {//FROM SQL
            //GET CREATE
            boolean create;
            var createTXT = TGS_CharSetCast.toLocaleUpperCase(fileCommonBall.macroLineTokens.get(9));
            if (Objects.equals(createTXT, TS_FileTmcrCodeImageTags.CODE_TOKEN_CREATE())) {
                create = true;
            } else if (Objects.equals(createTXT, TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())) {
                create = false;
            } else {
                return d.returnError(result, "ERROR: code token[9] error! should be either" + TS_FileTmcrCodeImageTags.CODE_TOKEN_CREATE() + " or " + TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL());
            }
            d.ci(result.value0, "INFO: create is : " + create);

            //GET imageTable AND imgColIdx
            TGS_LibRqlTbl imageTable;
            Integer imgColIdx;
            {
                //GET tableAndColname
                var tableAndColname = TS_FileTmcrParser_Assure.splitTableDotColname(fileCommonBall, 6);
                if (tableAndColname == null) {
                    return d.returnError(result, "ERROR: fromSQL.code token[6] error! tableAndColname:" + fileCommonBall.macroLineTokens.get(6));
                }
                d.ci(result.value0, "INFO: fromSQL.tableAndColname is : " + TGS_StringUtils.toString(tableAndColname, ","));

                imageTable = TS_FileTmcrParser_Assure.getTable(fileCommonBall, tableAndColname[0]);
                if (imageTable == null) {
                    return d.returnError(result, "ERROR: fromSQL.code token[6] error! imageTable == null: " + fileCommonBall.macroLineTokens.get(6));
                }
                if (d.infoEnable) {
                    var imageTableName = imageTable.nameSql;
                    d.ci(result.value0, "INFO: fromSQL.imageTable", imageTableName);
                }

                imgColIdx = TS_FileTmcrParser_Assure.getColumnIndex(fileCommonBall, imageTable, tableAndColname[1]);
                if (imgColIdx == null) {
                    return d.returnError(result, "ERROR: fromSQL.code token[6] error! imgColIdx == null: " + fileCommonBall.macroLineTokens.get(6));
                }
                d.ci(result.value0, "INFO: fromSQL.imageColumnname and idx is : " + tableAndColname[1] + " - " + imgColIdx);
            }
            var imageTableName = imageTable.nameSql;
            var imageTableColumn = imageTable.columns.get(imgColIdx);
            d.ci(result.value0, "INFO: fromSQL.imageTableColumn.getColumnName() is : " + imageTableColumn.getColumnName());

            //GET ID
            Long id;
            if (fileCommonBall.macroLineTokens.get(7).equals(TS_FileTmcrParser_SelectedId.CODE_TOKEN_SELECTED_ID())) {
                id = fileCommonBall.selectedId;
                if (id == null) {
                    return d.returnError(result, "ERROR: fromSQL.code token[7] error! SATIR SEÇİLMEDİ HATASI");
                }
            } else {
                id = TGS_CastUtils.toLong(fileCommonBall.macroLineTokens.get(7));
                if (id == null) {
                    return d.returnError(result, "ERROR: fromSQL.code token[7] should be a number");
                }
            }
            d.ci(result.value0, "INFO: fromSQL.id is : " + id);

            //GET FN
            String fn = null;
            {
                var fns = TS_LibTableFileListUtils.getFileNames_DataIn(fileCommonBall.url,
                        fileCommonBall.username,
                        dirDat,
                        imageTableName, imageTableColumn.getColumnName(), id, imageTableColumn.getDataString1_LnkTargetTableName(), create
                );
                if (fns.isEmpty()) {
                    d.ci(result.value0, "INFO fromSQL.TS_AppFileListUtils.getFileNames_DataIn.isEmpty()");
                    if (create) {
                        return d.returnError(result, "ERROR: fromSQL.RESİM BULUNAMADI HATASI @create");
                    } else {
                        d.ci(result.value0, "INFO fromSQL.ATLANDI: RESİM BULUNAMADI HATASI");
                    }
                } else {
                    d.ci(result.value0, "INFO fromSQL.TS_AppFileListUtils.getFileNames_DataIn.isEmpty() == false");
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
                        return d.returnError(result, "ERROR: fromSQL.skip alredy resized onces->fn==null");
                    }
                }
            }
            d.ci(result.value0, "INFO: fromSQL.fn is : " + fn);

            //GET preImageLoc
            if (fn != null) {
                var getInBox = TS_LibTableFileDirUtils.datTblTblnameColname(
                        dirDat,
                        imageTableName, imageTableColumn.getColumnName()
                );
                preImageLoc = Path.of(getInBox.toString(), fn);
            }
            d.ci(result.value0, "INFO: fromSQL.preImageLoc: " + preImageLoc);
        } else {//FROM URL
            var ref = fileCommonBall.macroLineTokens.get(6);
            d.ci(result.value0, "fromUrl", "ref.init", ref);
            preImageLoc = TS_PathUtils.toPathOrError(ref).value0;
            d.ci(result.value0, "fromUrl", "preImageLoc.try", preImageLoc);
            if (!TS_FileUtils.isExistFile(preImageLoc)) {//FIX: TRY TO UPGRADE OLD REF CODE
                d.ci(result.value0, "fromUrl", "preImageLoc.upgrade.mode", ref);
                List<TGS_Tuple2<String, String>> upgrade = TGS_ListUtils.of(
                        new TGS_Tuple2(
                                "D:/TOMCAT/webapps/AutoSQLWebInBox/TEMPLATES/mesametal.jpg",
                                TGS_LibResourceUtils.other.res.mesametal_com.logo.mesametal_jpg().toString()
                        ),
                        new TGS_Tuple2(
                                "D:/TOMCAT/webapps/AutoSQLWebInBox/TEMPLATES/mebosa.jpg",
                                TGS_LibResourceUtils.other.res.mebosa_com.logo.mebosa_jpg().toString()
                        )
                );
                var fref = ref.replace("\\", "/");
                var found = upgrade.stream().filter(p -> Objects.equals(p.value0, fref)).findAny().orElse(null);
                if (found == null) {
                    d.ci(result.value0, "SKIP: preImageLoc -> not upgrade-able", ref);
                } else {
                    ref = found.value1;
                    d.ci(result.value0, "fromUrl", "ref.found.url", ref);
                    fileCommonBall.macroLineTokens.set(6, ref);
                    preImageLoc = TS_PathUtils.toPathOrError(ref).value0;
                    d.ci(result.value0, "fromUrl", "preImageLoc.try2.purposely", preImageLoc);
                }
            }
            if (preImageLoc == null) {//FIX: TRY TO DOWNLOAD URL TO TEMP
                var idxLastSlash = ref.lastIndexOf("/");
                if (idxLastSlash == -1) {
                    return d.returnError(result, "ERROR: preImageLoc ->  idxLastSlash == -1 -> fromFile.ref :[" + ref + "]");
                }
                var filename = ref.substring(idxLastSlash + 1);
                d.ci(result.value0, "fromUrl", "filename", filename);
                var randomStr = TS_RandomUtils.nextString(10, true, true, false, false, null);
                var fileTmp = TS_LibFilePathUtils.datUsrNameTmp(dirDat, fileCommonBall.username).resolve(randomStr + filename);
                d.ci(result.value0, "fromUrl", "fileTmp", fileTmp);
                var refTmp = TS_UrlDownloadUtils.toFile(TGS_Url.of(ref), fileTmp);
                d.ci(result.value0, "fromUrl", "refTmp", refTmp);
                if (refTmp == null) {
                    return d.returnError(result, "ERROR: preImageLoc ->  refTmp == null -> fromFile.ref :[" + ref + "]");
                }
                ref = refTmp.toString();
                d.ci(result.value0, "fromUrl", "ref.updated", ref);
            }
            fileCommonBall.macroLineTokens.set(6, ref);
            preImageLoc = TS_PathUtils.toPathOrError(ref).value0;
            d.ci(result.value0, "fromUrl", "preImageLoc.updated", preImageLoc);
        }

        //CHECK preImageLoc
        if (preImageLoc == null) {
            return d.returnError(result, "ERROR: preImageLoc == null");
        }
        if (!TS_FileUtils.isExistFile(preImageLoc)) {
            return d.returnError(result, "ERROR: preImageLoc -> !TS_FileUtils.isExistFile(preImageLoc):[" + preImageLoc + "]");
        }

        //GET preImage
        BufferedImage preImage;
        try {
            preImage = TS_FileImageUtils.toImage(preImageLoc);
            if (preImage == null) {
                return d.returnError(result, "ERROR: preImage == null @ preImageLoc:" + preImageLoc);
            }
        } catch (Exception e) {
            TGS_UnSafe.throwIfInterruptedException(e);
            mifHandler.addText("ERROR: preImage == e @ preImageLoc:" + preImageLoc + ", e:" + e.getMessage());
            d.ci(result.value0, "  processed.");
            d.ci(result.value0, "compile_INSERT_IMAGE_COMMON  processed. ");
            return d.returnTrue(result);
        }

        //INIT W and H
        {
            var preImageW = preImage.getWidth();
            var preImageH = preImage.getHeight();
            d.ci(result.value0, "INFO: preImageW/H is : " + preImageW + "/" + preImageH);
            d.ci(result.value0, "INFO: #1 w/h is : " + w + "/" + h);
            if (w == null && h == null) {
                d.ci(result.value0, "INFO: will set w/h");
                w = preImageW;
                h = preImageH;
            } else if (w == null) {
                d.ci(result.value0, "INFO: will set w");
                w = (int) (1d * preImageW * h / preImageH);
            } else if (h == null) {
                d.ci(result.value0, "INFO: will set h");
                h = (int) (1d * preImageH * w / preImageW);
            }
            d.ci(result.value0, "INFO: #2 w/h is : " + w + "/" + h);

            //CHANGE W and H by rotate
            if (r == 90 || r == 270) {//rotate
                d.ci(result.value0, "INFO: #4.1 DETECTED r == 90 || r == 270");
                Integer tmp = w;//var is problematic!!
                w = h;
                h = tmp;
            }

            //CHANGE W and H by rotationIsLandscape
            if (rotationIsLandscape && w < h) {
                d.ci(result.value0, "INFO: #4.2 DETECTED rotationIsLandscape && w < h");
                Integer tmp = w;//var is problematic!!
                w = h;
                h = tmp;
            }

            //CHANGE W and H by rotationIsPortrait
            if (rotationIsPortrait && w > h) {
                d.ci(result.value0, "INFO: #4.3 DETECTED rotationIsPortrait && w > h");
                Integer tmp = w;//var is problematic!!
                w = h;
                h = tmp;
            }

            //CHANGE W and H by cellHeight
            if (fileCommonBall.cellHeight == null) {
                d.ci(result.value0, "INFO: #5.1 SKIP fileCommonBall.cellHeight == null");
            } else if (fileCommonBall.cellHeight >= h) {
                d.ci(result.value0, "INFO: #5.2 SKIP fileCommonBall.cellHeight >= h");
            } else {
                d.ci(result.value0, "INFO: #5.3 DETECTED fileCommonBall.cellHeight < h");
                var newImageWidth = 1d * fileCommonBall.cellHeight * w / h;
                w = (int) Math.round(newImageWidth);
                h = fileCommonBall.cellHeight;
            }
            d.ci(result.value0, "INFO: #5.4 w/h is : " + w + "/" + h);

            d.ci(result.value0, "INFO: #6 w/h is : " + w + "/" + h);
        }

        var quality = DEFAULT_QUALITY();
        var pstImageLoc = Path.of(preImageLoc.toString() + "." + w + "." + h + "." + r + "." + respect + "." + quality + "." + TS_FileUtils.getNameType(preImageLoc));
        d.ci(result.value0, "INFO: pstImageLoc is : " + pstImageLoc);

        BufferedImage pstImage;
        if (TS_FileUtils.isExistFile(pstImageLoc)) {
            d.ci(result.value0, "INFO: pstImageLoc found -> notProcessed :)");
            pstImage = TS_FileImageUtils.toImage(pstImageLoc);
        } else {
            d.ci(result.value0, "INFO: pstImageLoc notFOund -> processing...");
            pstImage = TS_FileImageUtils.resize_and_rotate(preImage, new TGS_ShapeDimension(w, h), r, respect);
            w = pstImage.getWidth();
            h = pstImage.getHeight();
//            System.out.println("AAAAAAAAA- AYARLI QUALITY:" + quality);
            TS_FileImageUtils.toFile(pstImage, pstImageLoc, quality / 100f);
        }
        if (pstImage == null) {
            return d.returnError(result, "ERROR: pstImage == null @ pstImageLoc:" + pstImageLoc);
        }

        d.ci(result.value0, "INFO: pstImage w/h is : " + w + "/" + h);
        d.ci(result.value0, "INFO: pstImageLoc: " + pstImageLoc);
        d.ci(result.value0, "INFO: pstImageType: " + pstImage.getType());
        d.ci(result.value0, "INFO: END");

        if (!mifHandler.addImage(pstImage, pstImageLoc, textWrap, left0_center1_right2, fileCommonBall.imageCounter++)) {
            return d.returnError(result, "ERR@ " + pstImageLoc);
        }
        d.ci(result.value0, "compile_INSERT_IMAGE_COMMON  processed. ");
        return d.returnTrue(result);
    }
}
