package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.function.client.TGS_FuncEffectivelyFinal;
import com.tugalsan.api.function.client.TGS_Func_In2;
import com.tugalsan.api.cast.client.TGS_CastUtils;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.function.client.TGS_Func_In1;
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
import com.tugalsan.api.file.pdf.itext.server.TS_FilePdfItext;
import com.tugalsan.api.file.server.TS_FileUtils;
import com.tugalsan.api.file.zip.server.TS_FileZipUtils;
import com.tugalsan.api.font.server.TS_FontUtils;
import com.tugalsan.api.function.client.TGS_Func;
import com.tugalsan.api.sql.conn.server.TS_SQLConnAnchor;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.string.client.TGS_StringUtils;
import com.tugalsan.api.tuple.client.TGS_Tuple1;
import com.tugalsan.api.unsafe.client.TGS_UnSafe;
import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.lib.file.tmcr.client.TGS_LibFileTmcrTypes;
import com.tugalsan.lib.file.tmcr.server.code.parser.TS_LibFileTmcrParser;
import java.awt.Font;
import java.time.Duration;
import java.util.stream.IntStream;

public class TS_LibFileTmcrFileHandler {

    final public static TS_Log d = TS_Log.of(false, TS_LibFileTmcrFileHandler.class);
    final private static boolean PARALLEL = false; //may cause unexpected exception: java.lang.OutOfMemoryError: Java heap space

    public TS_FileCommonConfig fileCommonConfig;
    final public List<TS_FileCommonAbstract> files;

    public boolean isZipFileRequested() {
        return fileCommonConfig.requestedFileTypes.stream()
                .filter(type -> Objects.equals(type, TGS_LibFileTmcrTypes.FILE_TYPE_ZIP()))
                .findAny().isPresent();
    }

    public List<Path> zipableFiles() {
        return TGS_StreamUtils.toLst(files.stream()
                        .filter(mif -> mif.isEnabled())
                        .filter(mif -> !mif.getLocalFileName().toString().endsWith(TGS_LibFileTmcrTypes.FILE_TYPE_TMCR())
                        && !mif.getLocalFileName().toString().endsWith(TGS_LibFileTmcrTypes.FILE_TYPE_HTML()))
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

    private TS_LibFileTmcrFileHandler(TS_FileCommonConfig fileCommonConfig, Path localfileZIP, TGS_Url remotefileZIP, TS_FileCommonAbstract... files) {
        this.fileCommonConfig = fileCommonConfig;
        this.localfileZIP = localfileZIP;
        this.remotefileZIP = remotefileZIP;
        this.files = TGS_StreamUtils.toLst(Arrays.stream(files));
    }
    public Path localfileZIP;
    public TGS_Url remotefileZIP;

    public static boolean use(TS_FileCommonConfig fileCommonConfig, TS_SQLConnAnchor anchor,
            TGS_Func_In2<String, Integer> progressUpdate_with_userDotTable_and_percentage,
            Duration timeout
    ) {
        return use(fileCommonConfig, anchor, progressUpdate_with_userDotTable_and_percentage, null,timeout);
    }

    public static boolean use(TS_FileCommonConfig fileCommonConfig, TS_SQLConnAnchor anchor,
            TGS_Func_In2<String, Integer> progressUpdate_with_userDotTable_and_percentage,
            TGS_Func_In1<TS_LibFileTmcrFileHandler> fileHandler, Duration timeout
    ) {
        d.ci("use", "running macro code...");
        var _fileHandler = TGS_FuncEffectivelyFinal.of(TS_LibFileTmcrFileHandler.class).coronateAs(__ -> {
            TGS_Tuple1<TS_LibFileTmcrFileHandler> holdForAWhile = TGS_Tuple1.of();
            TS_LibFileTmcrFileHandler.use_do(fileCommonConfig, __fileHandler -> {
                holdForAWhile.value0 = __fileHandler;

                d.ci("use", "compileCode");
                TS_LibFileTmcrParser.compileCode(anchor, fileCommonConfig, __fileHandler, (userDotTable, percentage) -> {
                    if (progressUpdate_with_userDotTable_and_percentage != null) {
                        progressUpdate_with_userDotTable_and_percentage.run(userDotTable, percentage);
                    }
                }, timeout);
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
            _fileHandler.files.forEach(file -> TS_LibFileTmcrFilePrefferedFileName.renameFiles_ifEnabled(file, fileCommonConfig));
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
            TS_LibFileTmcrFilePrefferedFileName.renameZip(fileCommonConfig, _fileHandler);
        }
        if (fileHandler != null) {
            fileHandler.run(_fileHandler);
        }
        return fileCommonConfig.runReport;
    }

    private static void use_do(TS_FileCommonConfig fileCommonConfig, TGS_Func_In1<TS_LibFileTmcrFileHandler> fileHandler) {
        var webWidthScalePercent = 68;
        var webFontHightPercent = 60;
        var webHTMLBase64 = false;
        var webHTMBase64 = true;
        var enableTMCR = fileCommonConfig.requestedFileTypes.contains(TGS_LibFileTmcrTypes.FILE_TYPE_TMCR());
        var enableHTML = fileCommonConfig.requestedFileTypes.contains(TGS_LibFileTmcrTypes.FILE_TYPE_HTML());
        var enableHTM = fileCommonConfig.requestedFileTypes.contains(TGS_LibFileTmcrTypes.FILE_TYPE_HTM());
        var enablePDF = fileCommonConfig.requestedFileTypes.contains(TGS_LibFileTmcrTypes.FILE_TYPE_PDF());
        var enableXLSX = fileCommonConfig.requestedFileTypes.contains(TGS_LibFileTmcrTypes.FILE_TYPE_XLSX());
        var enableDOCX = fileCommonConfig.requestedFileTypes.contains(TGS_LibFileTmcrTypes.FILE_TYPE_DOCX());
        var fileNameFullZIP = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_ZIP();
        var fileNameFullTMCR = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_TMCR();
        var fileNameFullHTML = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_HTML();
        var fileNameFullHTM = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_HTM();
        var fileNameFullPDF = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_PDF();
        var fileNameFullXLSX = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_XLSX();
        var fileNameFullDOCX = fileCommonConfig.fileNameLabel + TGS_LibFileTmcrTypes.FILE_TYPE_DOCX();
        var localfileZIP = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullZIP);
        var localfileTMCR = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullTMCR);
        var localfileHTML = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullHTML);
        var localfileHTM = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullHTM);
        var localfilePDF = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullPDF);
        var localfileXLSX = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullXLSX);
        var localfileDOCX = TS_LibFileTmcrFileSetName.path(fileCommonConfig, fileNameFullDOCX);
        var remotefileZIP = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullZIP, true);
        var remotefileTMCR = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullTMCR, true);
        var remotefileHTML = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullHTML, false);
        var remotefileHTM = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullHTM, true);
        var remotefilePDF = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullPDF, true);
        var remotefileXLSX = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullXLSX, true);
        var remotefileDOCX = TS_LibFileTmcrFileSetName.urlUser(fileCommonConfig, fileNameFullDOCX, true);
        TS_LibFileTmcrFileTMCR.use(enableTMCR, fileCommonConfig, localfileTMCR, remotefileTMCR, tmcr -> {
            TS_FileHtml.use(enableHTML, fileCommonConfig, localfileHTML, remotefileHTML, webHTMLBase64, webWidthScalePercent, webFontHightPercent, (webHTM, imageLoc) -> TS_LibFileTmcrFileSetName.urlFromPath(fileCommonConfig, imageLoc), webHTML -> {
                TS_FileHtml.use(enableHTM, fileCommonConfig, localfileHTM, remotefileHTM, webHTMBase64, webWidthScalePercent, webFontHightPercent, (webHTM, imageLoc) -> TS_LibFileTmcrFileSetName.urlFromPath(fileCommonConfig, imageLoc), webHTM -> {
                    TS_FilePdfItext.use(enablePDF, fileCommonConfig, localfilePDF, remotefilePDF, pdf -> {
                        TS_FileXlsx.use(enableXLSX, fileCommonConfig, localfileXLSX, remotefileXLSX, xlsx -> {
                            TS_FileDocx.use(enableDOCX, fileCommonConfig, localfileDOCX, remotefileDOCX, docx -> {
                                var instance = new TS_LibFileTmcrFileHandler(fileCommonConfig, localfileZIP, remotefileZIP,
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
        }, e -> TGS_Func.empty.run());
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

    private Font getFont(int fontFamilyIdx) {
        if (fileCommonConfig.fontBold && fileCommonConfig.fontItalic) {
            return fileCommonConfig.fontFamilyFonts.get(fontFamilyIdx).boldItalic();
        }
        if (fileCommonConfig.fontBold) {
            return fileCommonConfig.fontFamilyFonts.get(fontFamilyIdx).bold();
        }
        if (fileCommonConfig.fontItalic) {
            return fileCommonConfig.fontFamilyFonts.get(fontFamilyIdx).italic();
        }
        return fileCommonConfig.fontFamilyFonts.get(fontFamilyIdx).regular();
    }

    public boolean addText(String fullText) {
        if (fullText.isEmpty()) {
            d.ci("fullText", "fullText.isEmpty");
            return true;
        }
        if (TS_FontUtils.canDisplay(getFont(0), fullText)) {
            return addText_canDisplay(fullText);
        }
        var fontFamilySize = fileCommonConfig.fontFamilyFonts.size();
        var fullTextThatCanBeDisplayed = TGS_FuncEffectivelyFinal.ofStr().coronateAs(__ -> {//change unsupported codePoints to '?'
            var codePointUnsupported = "?".codePointAt(0);
            TGS_Tuple1<Boolean> codePointUnsupportedFound = TGS_Tuple1.of(false);
            var sb = new StringBuilder();
            fullText.codePoints().map(cp -> {
                for (var fontFamilyIdx = 0; fontFamilyIdx < fontFamilySize; fontFamilyIdx++) {
                    var font = getFont(fontFamilyIdx);
                    if (TS_FontUtils.canDisplay(font, cp)) {
                        return cp;
                    }
                }
                codePointUnsupportedFound.value0 = true;
                return codePointUnsupported;
            }).forEachOrdered(cp -> sb.appendCodePoint(cp));
            return codePointUnsupportedFound.value0 ? sb.toString() : fullText;
        });
        {//prepare init font
            fileCommonConfig.fontFamilyIdx = 0;
            setFontStyle();
        }
        //decide fontidx and send text to addText_canDisplay
        var sb = new StringBuilder();
        fullTextThatCanBeDisplayed.codePoints().forEachOrdered(cp -> {
            var decidedFontFamilyIdx = TGS_FuncEffectivelyFinal.ofInt().coronateAs(__ -> {
                for (var fontFamilyIdx = 0; fontFamilyIdx < fontFamilySize; fontFamilyIdx++) {
                    if (TS_FontUtils.canDisplay(getFont(fontFamilyIdx), cp)) {
                        return fontFamilyIdx;
                    }
                }
                return 0;
            });
            if (decidedFontFamilyIdx != fileCommonConfig.fontFamilyIdx) {//If new font detected
                if (!sb.isEmpty()) {//send previous data to addText_canDisplay
                    addText_canDisplay(sb.toString());
                    sb.setLength(0);
                }
                {//prepare new font
                    fileCommonConfig.fontFamilyIdx = decidedFontFamilyIdx;
                    setFontStyle();
                }
            }
            sb.appendCodePoint(cp);//buffer to be sent later time
        });
        addText_canDisplay(sb.toString());//send the last batch
        {//prepare default font before exiting func
            fileCommonConfig.fontFamilyIdx = 0;
            setFontStyle();
        }
        return true;
    }

    private boolean addText_canDisplay(String fullText) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var tokens = TGS_StringUtils.jre().toList(fullText, "\n");
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
        var plainArr = TGS_StringUtils.jre().toList(lineText, "{P}");
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
            var boldArr = TGS_StringUtils.jre().toList(plainText, "{B}");
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
                var italicArr = TGS_StringUtils.jre().toList(boldText, "{I}");
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
                    var fontColorArr = TGS_StringUtils.jre().toList(italicText, "{FC_");
                    for (var fontColorArr_i = 0; fontColorArr_i < fontColorArr.size(); fontColorArr_i++) {
                        var fontColorText = fontColorArr.get(fontColorArr_i);
                        d.ci("addText_line.lineText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "].colorText[" + fontColorArr_i + "]:[" + fontColorText + "]");
                        if (fontColorArr_i != 0) {
                            var i = fontColorText.indexOf("}");
                            if (i != -1) {
                                var fontColor = TGS_CharSetCast.current().toUpperCase(fontColorText.substring(0, i));
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
                        var fontHeightArr = TGS_StringUtils.jre().toList(fontColorText, "{FH_");
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
