package com.tugalsan.api.file.tmcr.server.file;

import com.tugalsan.api.file.tmcr.server.code.font.TS_FileTmcrCodeFontTags;
import com.tugalsan.api.file.tmcr.server.code.parser.TS_FileTmcrParser_Globals;
import com.tugalsan.api.string.server.*;
import com.tugalsan.api.cast.client.*;
import com.tugalsan.api.charset.client.TGS_CharSetCast;
import com.tugalsan.api.file.client.TGS_FileUtilsEng;
import com.tugalsan.api.file.client.TGS_FileUtilsTur;
import com.tugalsan.api.list.client.*;
import java.awt.image.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;
import com.tugalsan.api.tuple.client.*;
import com.tugalsan.api.log.server.*;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.stream.client.TGS_StreamUtils;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.file.tmcr.client.TGS_FileTmcrTypes;

public class TS_FileTmcrFileHandler extends TS_FileTmcrFileInterface {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileHandler.class);
    final private static boolean PARALLEL = false; //may cause unexpected exception: java.lang.OutOfMemoryError: Java heap space

    public void rename(Path dirDat, TS_FileTmcrParser_Globals macroGlobals) {
        d.ci("rename", macroGlobals.prefferedFileNameLabel);
        if (!macroGlobals.prefferedFileNameLabel.isEmpty()) {
            if (TS_FileTmcrFileInterface.FILENAME_CHAR_SUPPORT_TURKISH) {
                macroGlobals.prefferedFileNameLabel = TGS_FileUtilsTur.toSafe(macroGlobals.prefferedFileNameLabel);
            } else {
                macroGlobals.prefferedFileNameLabel = TGS_FileUtilsEng.toSafe(macroGlobals.prefferedFileNameLabel);
            }
            if (!TS_FileTmcrFileInterface.FILENAME_CHAR_SUPPORT_SPACE){
                macroGlobals.prefferedFileNameLabel = macroGlobals.prefferedFileNameLabel.replace(" ", "_");
            }
            files.forEach(file -> file.renameLocalFileName_ifEnabled(dirDat, macroGlobals));
        }
    }

    public TS_FileTmcrParser_Globals macroGlobals;
    final public List<TS_FileTmcrFileInterface> files;

    public List<String> getRemoteFiles() {
        List<String> remoteFiles = TGS_ListUtils.of();
        files.stream().filter(mif -> mif.isEnabled()).forEachOrdered(f -> remoteFiles.add(f.getRemoteFileName().url.toString()));
        return remoteFiles;
    }

    private TS_FileTmcrFileHandler(TS_FileTmcrParser_Globals macroGlobals, TS_FileTmcrFileInterface... files) {
        super(true, null, null);
        this.macroGlobals = macroGlobals;
        this.files = TGS_StreamUtils.toLst(Arrays.stream(files));
    }

    public static void use(TS_FileTmcrParser_Globals macroGlobals, TGS_RunnableType1<TS_FileTmcrFileHandler> fileHandler) {
        var webWidthScalePercent = 68;
        var webFontHightPercent = 60;
        var webHTMLBase64 = false;
        var webHTMBase64 = true;
        var enableTMCR = macroGlobals.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_TMCR());
        var enableHTML = macroGlobals.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_HTML());
        var enableHTM = macroGlobals.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_HTM());
        var enablePDF = macroGlobals.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_PDF());
        var enableXLSX = macroGlobals.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_XLSX());
        var enableDOCX = macroGlobals.requestedFileTypes.contains(TGS_FileTmcrTypes.FILE_TYPE_DOCX());
        var fileNameFullTMCR = macroGlobals.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_TMCR();
        var fileNameFullHTML = macroGlobals.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_HTML();
        var fileNameFullHTM = macroGlobals.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_HTM();
        var fileNameFullPDF = macroGlobals.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_PDF();
        var fileNameFullXLSX = macroGlobals.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_XLSX();
        var fileNameFullDOCX = macroGlobals.fileNameLabel + TGS_FileTmcrTypes.FILE_TYPE_DOCX();
        var localfileTMCR = TS_FileTmcrFileInterface.constructPathForLocalFile(macroGlobals.dirDat, fileNameFullTMCR, macroGlobals.username);
        var localfileHTML = TS_FileTmcrFileInterface.constructPathForLocalFile(macroGlobals.dirDat, fileNameFullHTML, macroGlobals.username);
        var localfileHTM = TS_FileTmcrFileInterface.constructPathForLocalFile(macroGlobals.dirDat, fileNameFullHTM, macroGlobals.username);
        var localfilePDF = TS_FileTmcrFileInterface.constructPathForLocalFile(macroGlobals.dirDat, fileNameFullPDF, macroGlobals.username);
        var localfileXLSX = TS_FileTmcrFileInterface.constructPathForLocalFile(macroGlobals.dirDat, fileNameFullXLSX, macroGlobals.username);
        var localfileDOCX = TS_FileTmcrFileInterface.constructPathForLocalFile(macroGlobals.dirDat, fileNameFullDOCX, macroGlobals.username);
        var remotefileTMCR = TS_FileTmcrFileInterface.constructURLForRemoteFile(true, macroGlobals.dirDat, fileNameFullTMCR, macroGlobals.username, macroGlobals.url);
        var remotefileHTML = TS_FileTmcrFileInterface.constructURLForRemoteFile(false, macroGlobals.dirDat, fileNameFullHTML, macroGlobals.username, macroGlobals.url);
        var remotefileHTM = TS_FileTmcrFileInterface.constructURLForRemoteFile(true, macroGlobals.dirDat, fileNameFullHTM, macroGlobals.username, macroGlobals.url);
        var remotefilePDF = TS_FileTmcrFileInterface.constructURLForRemoteFile(true, macroGlobals.dirDat, fileNameFullPDF, macroGlobals.username, macroGlobals.url);
        var remotefileXLSX = TS_FileTmcrFileInterface.constructURLForRemoteFile(true, macroGlobals.dirDat, fileNameFullXLSX, macroGlobals.username, macroGlobals.url);
        var remotefileDOCX = TS_FileTmcrFileInterface.constructURLForRemoteFile(true, macroGlobals.dirDat, fileNameFullDOCX, macroGlobals.username, macroGlobals.url);
        TS_FileTmcrFileTMCR.use(enableTMCR, macroGlobals, localfileTMCR, remotefileTMCR, tmcr -> {
            TS_FileTmcrFileWeb.use(enableHTML, macroGlobals, localfileHTML, remotefileHTML, webHTMLBase64, webWidthScalePercent, webFontHightPercent, webHTML -> {
                TS_FileTmcrFileWeb.use(enableHTM, macroGlobals, localfileHTM, remotefileHTM, webHTMBase64, webWidthScalePercent, webFontHightPercent, webHTM -> {
                    TS_FileTmcrFilePDF.use(enablePDF, macroGlobals, localfilePDF, remotefilePDF, pdf -> {
                        TS_FileTmcrFileXLSX.use(enableXLSX, macroGlobals, localfileXLSX, remotefileXLSX, xlsx -> {
                            TS_FileTmcrFileDOCX.use(enableDOCX, macroGlobals, localfileDOCX, remotefileDOCX, docx -> {
                                var instance = new TS_FileTmcrFileHandler(macroGlobals,
                                        tmcr, webHTML, webHTM, pdf, xlsx, docx
                                );
                                fileHandler.run(instance);
                            });
                        });
                    });
                });
            });
        });
    }

    @Override
    public boolean saveFile(String errorSource) {
        TGS_UnSafe.run(() -> {
            if (errorSource != null) {
                macroGlobals.fontColor = TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_RED();
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
        return macroGlobals.runReport = true;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
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

    @Override
    public boolean addText(String mainText) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var tokens = TS_StringUtils.toList(mainText, "\n");
        IntStream.range(0, tokens.size()).forEachOrdered(i -> {
            var text = tokens.get(i);
            if (!text.isEmpty()) {
                var b = addText2(text);
                if (!b) {
                    result.value0 = false;
                }
            }
            if (i != tokens.size() - 1 || mainText.endsWith("\n")) {
                var b = addLineBreak();
                if (!b) {
                    result.value0 = false;
                }
            }
        });
        return result.value0;
    }

    private boolean addText2(String mainText) {
        d.ci("addText2", "mainText", mainText);
        if (colors.isEmpty()) {
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_BLACK());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_RED());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_YELLOW());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_BLUE());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_GREEN());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_PINK());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_ORANGE());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_CYAN());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_GRAY());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY());
            colors.add(TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA());
        }
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        mainText = mainText.replace("{fc_", "{FC_");
        mainText = mainText.replace("{fh_", "{FH_");
        mainText = mainText.replace("{b}", "{B}");
        mainText = mainText.replace("{p}", "{P}");
        mainText = mainText.replace("{i}", "{I}");
        d.ci("addText2.mainText:[" + mainText + "]");
        var plainArr = TS_StringUtils.toList(mainText, "{P}");
        for (var plainArr_i = 0; plainArr_i < plainArr.size(); plainArr_i++) {
            var plainText = plainArr.get(plainArr_i);
            d.ci("addText2.mainText.plainText[" + plainArr_i + "]:[" + plainText + "]");
            if (plainArr_i != 0) {
                macroGlobals.fontItalic = false;
                macroGlobals.fontBold = false;
                var b = setFontStyle();
                if (!b) {
                    result.value0 = false;
                }
            }
            var boldArr = TS_StringUtils.toList(plainText, "{B}");
            for (var boldArr_i = 0; boldArr_i < boldArr.size(); boldArr_i++) {
                var boldText = boldArr.get(boldArr_i);
                d.ci("addText2.mainText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "]:[" + boldText + "]");
                if (boldArr_i != 0) {
                    macroGlobals.fontBold = true;
                    var b = setFontStyle();
                    if (!b) {
                        result.value0 = false;
                    }
                }
                var italicArr = TS_StringUtils.toList(boldText, "{I}");
                for (var italicArr_i = 0; italicArr_i < italicArr.size(); italicArr_i++) {
                    var italicText = italicArr.get(italicArr_i);
                    d.ci("addText2.mainText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "]:[" + italicText + "]");
                    if (italicArr_i != 0) {
                        macroGlobals.fontItalic = true;
                        var b = setFontStyle();
                        if (!b) {
                            result.value0 = false;
                        }
                    }
                    var fontColorArr = TS_StringUtils.toList(italicText, "{FC_");
                    for (var fontColorArr_i = 0; fontColorArr_i < fontColorArr.size(); fontColorArr_i++) {
                        var fontColorText = fontColorArr.get(fontColorArr_i);
                        d.ci("addText2.mainText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "].colorText[" + fontColorArr_i + "]:[" + fontColorText + "]");
                        if (fontColorArr_i != 0) {
                            var i = fontColorText.indexOf("}");
                            if (i != -1) {
                                var fontColor = TGS_CharSetCast.toLocaleUpperCase(fontColorText.substring(0, i));
                                d.ci("addText2.fontColor to be parsed: [" + fontColor + "]");
                                fontColorText = fontColorText.substring(i + 1);
                                var found = false;
                                for (var cti = 0; cti < colors.size(); cti++) {
                                    if (colors.get(cti).equals(fontColor)) {
                                        macroGlobals.fontColor = colors.get(cti);
                                        var b = setFontColor();
                                        if (!b) {
                                            result.value0 = false;
                                        }
                                        found = true;
                                        break;
                                    }
                                }
                                if (!found) {
                                    d.ci("addText2.fontColorText[" + fontColor + "] cannot be processed. BLACK will be used isntead");
                                    macroGlobals.fontColor = colors.get(0);
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
                            d.ci("addText2.mainText.plainText[" + plainArr_i + "].boldText[" + boldArr_i + "].italicText[" + italicArr_i + "].colorText[" + fontColorArr_i + "].fontHeightText[" + fontHeightArr_i + "]:[" + fontHeightText + "]");
                            if (fontHeightArr_i != 0) {
                                var i = fontHeightText.indexOf("}");
                                if (i != -1) {
                                    var fontHeight = fontHeightText.substring(0, i);
                                    d.ci("addText2.fontHeight to be parsed: [" + fontHeight + "]");
                                    var fsInt = TGS_CastUtils.toInteger(fontHeight);
                                    if (fsInt == null) {
                                        d.ci("addText2.fontHeight[" + fontHeight + "] cannot be processed. 10 will be used instead.");
                                        fontHeightText = fontHeightText.substring(i + 1);
                                        macroGlobals.fontHeight = 10;
                                        var b = setFontHeight();
                                        if (!b) {
                                            result.value0 = false;
                                        }
                                    } else {
                                        fontHeightText = fontHeightText.substring(i + 1);
                                        macroGlobals.fontHeight = fsInt;
                                        var b = setFontHeight();
                                        if (!b) {
                                            result.value0 = false;
                                        }
                                    }
                                }
                            }
                            result.value0 = result.value0 && addText3(fontHeightText);
                        }
                    }
                }
            }
        }
        return result.value0;
    }

    private boolean addText3(String text) {
        TGS_Tuple1<Boolean> result = new TGS_Tuple1(true);
        var stream = PARALLEL ? files.parallelStream() : files.stream();
        stream.filter(mif -> mif.isEnabled()).forEach(mi -> {
            var b = mi.addText(text);
            if (!b) {
                result.value0 = false;
            }
        });
        if (!result.value0) {
            d.ce("addText3.CODE_ADD_TEXT HAD ERRORS.");
            return false;
        }
        d.ci("  processed.");
        return true;
    }

    @Override
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

    @Override
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

    @Override
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

    @Override
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
