package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.cast.client.TGS_CastUtils;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.coronator.client.TGS_Coronator;
import com.tugalsan.api.file.client.TGS_FileUtilsEng;
import com.tugalsan.api.file.client.TGS_FileUtilsTur;
import com.tugalsan.api.file.html.server.TS_FileHtml;
import com.tugalsan.api.file.common.server.TS_FileCommonAbstract;
import com.tugalsan.api.file.common.server.TS_FileCommonConfig;
import com.tugalsan.api.file.common.server.TS_FileCommonFontTags;
import com.tugalsan.api.file.docx.server.TS_FileDocx;
import com.tugalsan.api.file.xlsx.server.TS_FileXlsx;
import com.tugalsan.api.list.client.*;
import java.awt.image.*;
import java.nio.file.*;
import java.util.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.file.pdf.server.TS_FilePdf;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.zip.server.TS_FileZipUtils;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.runnable.client.TGS_RunnableType2;
import com.tugalsan.api.sql.conn.server.TS_SQLConnAnchor;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.string.server.TS_StringUtils;
import com.tugalsan.api.tuple.client.TGS_Tuple1;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.lib.file.tmcr.client.TGS_FileTmcrTypes;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_FileTmcrParser;
import java.util.stream.IntStream;

public class TS_FileTmcrFileHandler {

    final public static TS_Log d = TS_Log.of(TS_FileTmcrFileHandler.class);
    final private static boolean PARALLEL = false; //may cause unexpected exception: java.lang.OutOfMemoryError: Java heap space

    public TS_FileCommonConfig fileCommonConfig;
    final public List<TS_FileCommonAbstract> files;

    public boolean isZipFileRequested() {
        return fileCommonConfig.requestedFileTypes.stream()
                .filter(type -> Objects.equals(type, TGS_FileTmcrTypes.FILE_TYPE_ZIP()))
                .findAny().isPresent();
    }

    public List<Path> zipableFiles() {
        return TGS_StreamUtils.toLst(
                files.stream()
                        .filter(mif -> mif.isEnabled())
                        .filter(mif -> !mif.getLocalFileName().toString().endsWith(TGS_FileTmcrTypes.FILE_TYPE_TMCR())
                        && !mif.getLocalFileName().toString().endsWith(TGS_FileTmcrTypes.FILE_TYPE_HTML()))
                        .map(mif -> mif.getLocalFileName())
        );
    }

    public List<String> getRemoteFiles() {
        return TGS_StreamUtils.toLst(
                files.stream()
                        .filter(mif -> mif.isEnabled())
                        .map(f -> f.getRemoteFileName().url.toString())
        );
    }

    private TS_FileTmcrFileHandler(TS_FileCommonConfig fileCommonConfig, Path localfileZIP, TGS_Url remotefileZIP, TS_FileCommonAbstract... files) {
        this.fileCommonConfig = fileCommonConfig;
        this.localfileZIP = localfileZIP;
        this.remotefileZIP = remotefileZIP;
        this.files = TGS_StreamUtils.toLst(Arrays.stream(files));
    }
    public Path localfileZIP;
    public TGS_Url remotefileZIP;

    public static boolean use(TS_FileCommonConfig fileCommonConfig, TS_SQLConnAnchor anchor,
            TGS_RunnableType2<String, Integer> progressUpdate_with_userDotTable_and_percentage
    ) {
        return use(fileCommonConfig, anchor, progressUpdate_with_userDotTable_and_percentage, null);
    }

    public static boolean use(TS_FileCommonConfig fileCommonConfig, TS_SQLConnAnchor anchor,
            TGS_RunnableType2<String, Integer> progressUpdate_with_userDotTable_and_percentage,
            TGS_RunnableType1<TS_FileTmcrFileHandler> fileHandler
    ) {
        d.ci("use", "running macro code...");
        var _fileHandler = TGS_Coronator.of(TS_FileTmcrFileHandler.class).coronateAs(__ -> {
            TGS_Tuple1<TS_FileTmcrFileHandler> holdForAWhile = TGS_Tuple1.of();
            TS_FileTmcrFileHandler.use_do(fileCommonConfig, __fileHandler -> {
                holdForAWhile.value0 = __fileHandler;

                d.ci("use", "compileCode");
                TS_FileTmcrParser.compileCode(anchor, fileCommonConfig, __fileHandler, (userDotTable, percentage) -> {
                    if (progressUpdate_with_userDotTable_and_percentage != null) {
                        progressUpdate_with_userDotTable_and_percentage.run(userDotTable, percentage);
                    }
                });
            });
            return holdForAWhile.value0;
        });
        d.ci("use", "RENAME LOCAL FILES", "prefferedFileNameLabel", fileCommonConfig.prefferedFileNameLabel);
        if (!fileCommonConfig.prefferedFileNameLabel.isEmpty()) {
            if (TS_FileCommonAbstract.FILENAME_CHAR_SUPPORT_TURKISH) {
                fileCommonConfig.prefferedFileNameLabel = TGS_FileUtilsTur.toSafe(fileCommonConfig.prefferedFileNameLabel);
            } else {
                fileCommonConfig.prefferedFileNameLabel = TGS_FileUtilsEng.toSafe(fileCommonConfig.prefferedFileNameLabel);
            }
            if (!TS_FileCommonAbstract.FILENAME_CHAR_SUPPORT_SPACE) {
                fileCommonConfig.prefferedFileNameLabel = fileCommonConfig.prefferedFileNameLabel.replace(" ", "_");
            }
            _fileHandler.files.forEach(file -> TS_FileTmcrFilePrefferedFileName.renameFiles_ifEnabled(file, fileCommonConfig));
        }
        if (_fileHandler.isZipFileRequested()) {
            var zipableFiles = _fileHandler.zipableFiles();
            if (zipableFiles.isEmpty()) {
                d.ce("use", "zipableFiles.isEmpty()!");
                return false;
            }
            TS_FileZipUtils.zipList(zipableFiles, _fileHandler.localfileZIP);
            if (!TS_FileUtils.isExistFile(_fileHandler.localfileZIP)) {
                d.ce("use", "!TS_FileUtils.isExistFile", _fileHandler.localfileZIP);
                return false;
            }
            TS_FileTmcrFilePrefferedFileName.renameZip(fileCommonConfig, _fileHandler);
        }
        if (fileHandler != null) {
            fileHandler.run(_fileHandler);
        }
        return fileCommonConfig.runReport;
    }

    private static void use_do(TS_FileCommonConfig fileCommonConfig, TGS_RunnableType1<TS_FileTmcrFileHandler> fileHandler) {
        var webWidthScalePercent = 68;
        var webFontHightPercent = 60;
        var webHTMLBase64 = false;
        var webHTMBase64 = true;
        var enableTMCR = fileCommonConfig.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_TMCR());
        var enableHTML = fileCommonConfig.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_HTML());
        var enableHTM = fileCommonConfig.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_HTM());
        var enablePDF = fileCommonConfig.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_PDF());
        var enableXLSX = fileCommonConfig.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_XLSX());
        var enableDOCX = fileCommonConfig.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_DOCX());
        var fileNameFullZIP = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_ZIP();
        var fileNameFullTMCR = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_TMCR();
        var fileNameFullHTML = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_HTML();
        var fileNameFullHTM = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_HTM();
        var fileNameFullPDF = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_PDF();
        var fileNameFullXLSX = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_XLSX();
        var fileNameFullDOCX = fileCommonConfig.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_DOCX();
        var localfileZIP = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullZIP);
        var localfileTMCR = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullTMCR);
        var localfileHTML = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullHTML);
        var localfileHTM = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullHTM);
        var localfilePDF = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullPDF);
        var localfileXLSX = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullXLSX);
        var localfileDOCX = TS_FileTmcrFileSetName.path(fileCommonConfig, fileNameFullDOCX);
        var remotefileZIP = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullZIP, true);
        var remotefileTMCR = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullTMCR, true);
        var remotefileHTML = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullHTML, false);
        var remotefileHTM = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullHTM, true);
        var remotefilePDF = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullPDF, true);
        var remotefileXLSX = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullXLSX, true);
        var remotefileDOCX = TS_FileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullDOCX, true);
        TS_FileTmcrFileTMCR.use(enableTMCR, fileCommonConfig, localfileTMCR, remotefileTMCR, tmcr -> {
            TS_FileHtml.use(enableHTML, fileCommonConfig, localfileHTML, remotefileHTML, webHTMLBase64, webWidthScalePercent, webFontHightPercent, (webHTM, imageLoc) -> TS_FileTmcrFileSetName.urlFromPath(fileCommonConfig, imageLoc), webHTML -> {
                TS_FileHtml.use(enableHTM, fileCommonConfig, localfileHTM, remotefileHTM, webHTMBase64, webWidthScalePercent, webFontHightPercent, (webHTM, imageLoc) -> TS_FileTmcrFileSetName.urlFromPath(fileCommonConfig, imageLoc), webHTM -> {
                    TS_FilePdf.use(enablePDF, fileCommonConfig, localfilePDF, remotefilePDF, pdf -> {
                        TS_FileXlsx.use(enableXLSX, fileCommonConfig, localfileXLSX, remotefileXLSX, xlsx -> {
                            TS_FileDocx.use(enableDOCX, fileCommonConfig, localfileDOCX, remotefileDOCX, docx -> {
                                var instance = new TS_FileTmcrFileHandler(fileCommonConfig, localfileZIP, remotefileZIP,
                                        tmcr, webHTML, webHTM, pdf, xlsx, docx
                                );
                                fileHandler.run(instance);
                            });
                        });
                    });
                }
                );
            });
        }
        );
    }

    public boolean saveFile(String errorSource) {
        TGS_UnSafe.run(() -> {
            if (errorSource != null) {
                fileCommonConfig.fontColor = TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED();
                beginText(0);
                addText(errorSource);
                endText();
                d.ce("saveFile", "WARNING: error message added to files", errorSource);
            }
        }, e -> TGS_StreamUtils.runNothing());
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            TGS_UnSafe.run(() -> {
                if (!mi.saveFile(errorSource)) {
                    d.ce("saveFile", "ERROR: cannot save mi.getLocalFileName:" + mi.getLocalFileName());
                }
            }, e -> d.ct("saveFile", e));
        });
        return fileCommonConfig.runReport = true;
    }

    public boolean createNewPage(int pageSizeAX, boolean landscape, Integer marginLeft, Integer marginRight, Integer marginTop, Integer marginBottom) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.createNewPage(pageSizeAX, landscape, marginLeft, marginRight, marginTop, marginBottom);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("createNewPage.CODE_INSERT_PAGE HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;//I know
    }

    public boolean addImage(BufferedImage pstImage, Path pstImageLoc, boolean textWrap, int left0_center1_right2, long imageCounter) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.addImage(pstImage, pstImageLoc, textWrap, left0_center1_right2, imageCounter);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("addImage.CODE_INSERTIMAGE HAD ERRORS.");
            return false;
        }
        return result.value0;
    }

    public boolean beginTableCell(int rowSpan, int colSpan, Integer cellHeight) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.beginTableCell(rowSpan, colSpan, cellHeight);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("beginTableCell.CODE_BEGIN_TABLECELL HAD ERRORS.");
            return false;
        }
        return result.value0;
    }

    public boolean endTableCell(int rotationInDegrees_0_90_180_270) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.endTableCell(rotationInDegrees_0_90_180_270);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("endTableCell.END_TABLECELL HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return result.value0;
    }

    public boolean beginTable(int[] relColSizes) {
        d.ci("beginTable", "#1");
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        d.ci("beginTable", "#2");
        files.stream().filter(mif -> mif.isEnabled()).forEachOrdered(mi -> {
            d.ci("beginTable", "#3.nm", mi.getClass().getSimpleName());
            var b = mi.beginTable(relColSizes);
            d.ci("beginTable", "#3.b", mi.getClass().getSimpleName());
            if (!b) {
                d.ci("beginTable", "#3.s", mi.getClass().getSimpleName());
                result.value0 = false;
            }
        });
        d.ci("beginTable", "#4");
        d.ci("beginTable", "#5");
        if (!result.value0) {
            d.ce("beginTable", "END_TABLECELL HAD ERRORS.");
            return false;
        }
        d.ci("beginTable", "  processed.");
        return true;
    }

    public boolean endTable() {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.endTable();
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("endTable.END_TABLE HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    public boolean beginText(int allign_Left0_center1_right2_just3) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.beginText(allign_Left0_center1_right2_just3);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("beginText.CODE_BEGIN_TEXT HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    public boolean endText() {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.endText();
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("endText.CODE_END_TEXT HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    private final static List<String> colors = TGS_ListUtils.of();

    @Deprecated //TODO PAN-UNICODE HANDLING NOT WORKING
    public boolean addText(String fullText) {
        if (fullText.isEmpty()) {
            return true;
        }
        if (fileCommonConfig.fontFamiliesFontOther.isEmpty()) {
            return addText_fonted(fullText);
        }
        var codePoints = fullText.codePoints().toArray();
        if (fileCommonConfig.fontFamiliesOtherIdx != -1) {
            fileCommonConfig.fontFamiliesOtherIdx = -1;
            setFontStyle();
        }
        var sb = new StringBuilder();
        var offset = 0;
        for (var idx = 0; idx < codePoints.length; idx++) {
            var codePoint = codePoints[idx];
            var canDisplay = fileCommonConfig.canDisplay(codePoint);
            if (d.infoEnable) {
                sb.setLength(0);
                sb.appendCodePoint(codePoint);
                d.ci("addText", idx, codePoint, sb.toString(), canDisplay);
            }
            if (idx == 0) {
                fileCommonConfig.fontFamiliesOtherIdx = canDisplay ? -1 : 0;
                continue;
            }
            if (fileCommonConfig.fontFamiliesOtherIdx == -1 && !canDisplay) {
                continue;
            }
            if (fileCommonConfig.fontFamiliesOtherIdx != -1 && canDisplay) {
                continue;
            }
            if (fileCommonConfig.fontFamiliesOtherIdx == -1 && canDisplay) {
                sb.setLength(0);
                IntStream.range(offset, idx)
                        .map(_idx -> codePoints[_idx])
                        .forEachOrdered(cp -> sb.appendCodePoint(cp));
                var r = addText_fonted(sb.toString());
                if (!r) {
                    return false;
                }
                offset = idx;
                fileCommonConfig.fontFamiliesOtherIdx = 0;
                setFontStyle();
                continue;
            }
            if (fileCommonConfig.fontFamiliesOtherIdx == 0 && !canDisplay) {
                sb.setLength(0);
                IntStream.range(offset, idx)
                        .map(_idx -> codePoints[_idx])
                        .forEachOrdered(cp -> sb.appendCodePoint(cp));
                var r = addText_fonted(sb.toString());
                if (!r) {
                    return false;
                }
                offset = idx;
                fileCommonConfig.fontFamiliesOtherIdx = -1;
                setFontStyle();
                continue;
            }
        }
        setFontStyle();
        sb.setLength(0);
        IntStream.range(offset, codePoints.length)
                .map(idx -> codePoints[idx])
                .forEachOrdered(cp -> sb.appendCodePoint(cp));
        return addText_fonted(sb.toString());
    }

    public boolean addText_fonted(String fullText) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var tokens = TS_StringUtils.toList(fullText, "\n");
        IntStream.range(0, tokens.size()).forEachOrdered(i -> {
            var lineText = tokens.get(i);
            if (!lineText.isEmpty()) {
                var b = addText_line(lineText);
                if (!b) {
                    result.value0 = false;
                }
            }
            if (i != tokens.size() - 1 || fullText.endsWith("\n")) {
                var b = addLineBreak();
                if (!b) {
                    result.value0 = false;
                }
            }
        });
        return result.value0;
    }

    private boolean addText_line(String lineText) {
        d.ci("addText_line", "lineText", lineText);
        if (colors.isEmpty()) {
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLACK());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_RED());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_YELLOW());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_BLUE());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GREEN());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_PINK());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_ORANGE());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_CYAN());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_GRAY());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY());
            colors.add(TS_FileCommonFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA());
        }
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        lineText = lineText.replace("{fc_", "{FC_");
        lineText = lineText.replace("{fh_", "{FH_");
        lineText = lineText.replace("{b}", "{B}");
        lineText = lineText.replace("{p}", "{P}");
        lineText = lineText.replace("{i}", "{I}");
        d.ci("addText_line.lineText:[" + lineText + "]");
        var plainArr = TS_StringUtils.toList(lineText, "{P}");
        for (var plainArr_i = 0; plainArr_i < plainArr.size(); plainArr_i++) {
            var plainText = plainArr.get(plainArr_i);
            d.ci("addText_line.lineText.plainText[" + plainArr_i + "]:[" + plainText + "]");
            if (plainArr_i != 0) {
                fileCommonConfig.fontItalic = false;
                fileCommonConfig.fontBold = false;
                var b = setFontStyle();
                if (!b) {
                    result.value0 = false;
                }
            }
            var boldArr = TS_StringUtils.toList(plainText, "{B}");
            for (var boldArr_i = 0; boldArr_i < boldArr.size(); boldArr_i++) {
                var boldText = boldArr.get(boldArr_i);
                d.ci("addText_line.lineText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "]:[" + boldText + "]");
                if (boldArr_i != 0) {
                    fileCommonConfig.fontBold = true;
                    var b = setFontStyle();
                    if (!b) {
                        result.value0 = false;
                    }
                }
                var italicArr = TS_StringUtils.toList(boldText, "{I}");
                for (var italicArr_i = 0; italicArr_i < italicArr.size(); italicArr_i++) {
                    var italicText = italicArr.get(italicArr_i);
                    d.ci("addText_line.lineText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "]:[" + italicText + "]");
                    if (italicArr_i != 0) {
                        fileCommonConfig.fontItalic = true;
                        var b = setFontStyle();
                        if (!b) {
                            result.value0 = false;
                        }
                    }
                    var fontColorArr = TS_StringUtils.toList(italicText, "{FC_");
                    for (var fontColorArr_i = 0; fontColorArr_i < fontColorArr.size(); fontColorArr_i++) {
                        var fontColorText = fontColorArr.get(fontColorArr_i);
                        d.ci("addText_line.lineText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "].colorText[" + fontColorArr_i + "]:[" + fontColorText + "]");
                        if (fontColorArr_i != 0) {
                            var i = fontColorText.indexOf("}");
                            if (i != -1) {
                                var fontColor = TGS_CharSetCast.toLocaleUpperCase(fontColorText.substring(0, i));
                                d.ci("addText_line.fontColor to be parsed: [" + fontColor + "]");
                                fontColorText = fontColorText.substring(i + 1);
                                var found = false;
                                for (var cti = 0; cti < colors.size(); cti++) {
                                    if (colors.get(cti).equals(fontColor)) {
                                        fileCommonConfig.fontColor = colors.get(cti);
                                        var b = setFontColor();
                                        if (!b) {
                                            result.value0 = false;
                                        }
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    d.ci("addText_line.fontColorText[" + fontColor + "] cannot be processed. BLACK will be used instead");
                                    fileCommonConfig.fontColor = colors.get(0);
                                    var b = setFontColor();
                                    if (!b) {
                                        result.value0 = false;
                                    }
                                }
                            }
                        }
                        var fontHeightArr = TS_StringUtils.toList(fontColorText, "{FH_");
                        for (var fontHeightArr_i = 0; fontHeightArr_i < fontHeightArr.size(); fontHeightArr_i++) {
                            var fontHeightText = fontHeightArr.get(fontHeightArr_i);
                            d.ci("addText_line.lineText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "].colorText[" + fontColorArr_i + "].fontHeightText[" + fontHeightArr_i + "]:[" + fontHeightText + "]");
                            if (fontHeightArr_i != 0) {
                                var i = fontHeightText.indexOf("}");
                                if (i != -1) {
                                    var fontHeight = fontHeightText.substring(0, i);
                                    d.ci("addText_line.fontHeight to be parsed: [" + fontHeight + "]");
                                    var fsInt = TGS_CastUtils.toInteger(fontHeight);
                                    if (fsInt == null) {
                                        d.ci("addText_line.fontHeight[" + fontHeight + "] cannot be processed. 10 will be used instead.");
                                        fontHeightText = fontHeightText.substring(i + 1);
                                        fileCommonConfig.fontHeight = 10;
                                        var b = setFontHeight();
                                        if (!b) {
                                            result.value0 = false;
                                        }
                                    } else {
                                        fontHeightText = fontHeightText.substring(i + 1);
                                        fileCommonConfig.fontHeight = fsInt;
                                        var b = setFontHeight();
                                        if (!b) {
                                            result.value0 = false;
                                        }
                                    }
                                }
                            }
                            result.value0 = result.value0 && addText_do(fontHeightText);
                        }
                    }
                }
            }
        }
        return result.value0;
    }

    private boolean addText_do(String text) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.addText(text);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("addText_do.CODE_ADD_TEXT HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    public boolean addLineBreak() {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.addLineBreak();
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("addLineBreak.CODE_ADD_TEXT_HR HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    public boolean setFontStyle() {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.setFontStyle();
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("setFontStyle.CODE_SET_FONT_STYLE HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    public boolean setFontHeight() {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.setFontHeight();
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("setFontHeight.CODE_SET_FONT_SIZE HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    public boolean setFontColor() {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.setFontColor();
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("setFontColor.CODE_SET_FONT_COLOR HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

}
