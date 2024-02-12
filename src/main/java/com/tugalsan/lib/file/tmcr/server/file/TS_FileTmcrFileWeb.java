package com.tugalsan.lib.file.tmcr.server.file;

import com.tugalsan.api.file.html.server.TS_FileHtmlUtils;
import com.tugalsan.api.file.html.server.element.*;
import com.tugalsan.api.file.html.client.*;
import com.tugalsan.api.string.server.*;
import com.tugalsan.api.file.html.client.element.*;
import com.tugalsan.api.file.server.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.IntStream;
import com.tugalsan.api.log.server.TS_Log;
import com.tugalsan.api.runnable.client.TGS_RunnableType1;
import com.tugalsan.api.string.client.*;
import com.tugalsan.api.unsafe.client.*;
import com.tugalsan.api.url.client.*;
import com.tugalsan.lib.resource.client.*;
import java.util.Objects;
import com.tugalsan.lib.domain.server.*;

public class TS_FileTmcrFileWeb extends TS_FileTmcrFileInterface {

    final private static TS_Log d = TS_Log.of(TS_FileTmcrFileWeb.class);

    private static int FONT_HEIGHT_OFFSET() {
        return 12;
    }

    public TGS_FileHtml webWriter;

    private TS_FileTmcrParser_Globals macroGlobals;
    private final int fontHeightScalePercent;
    private final int widthScalePercent;
    private final String favIconPng;

    public boolean base64() {
        return isBase64;
    }
    private final boolean isBase64;

    private TS_FileTmcrFileWeb(boolean enabled, Path localFile, TGS_Url remoteFile, boolean isBase64, int widthScalePercent, int fontHeightScalePercent) {
        super(enabled, localFile, remoteFile);
        this.isBase64 = isBase64;
        this.fontHeightScalePercent = fontHeightScalePercent;
        this.widthScalePercent = widthScalePercent;

        var domain = TS_LibDomainCardUtils.get();

        if (Objects.equals(domain.firmaNameParam, "mesametal")) {
            this.favIconPng = TGS_LibResourceUtils.other.res.mesametal_com.favicon.mesametal_dark_16x16_png().toString();
        } else if (Objects.equals(domain.firmaNameParam, "mebosa")) {
            this.favIconPng = TGS_LibResourceUtils.other.res.mebosa_com.favicon.mebosa_com_dark_16x16_png().toString();
        } else {
            this.favIconPng = TGS_LibResourceUtils.common.res.favicon.default_dark_16x16_png().toString();
        }

        d.ci("init", "url/param/fav", remoteFile, domain.firmaNameParam, favIconPng);
    }
    private final String customCssForBlackText = TGS_FileHtmlText.getDefaultCustomCssForBlackText();

    public static void use(boolean enabled, TS_FileTmcrParser_Globals macroGlobals, Path localFile, TGS_Url remoteFile, boolean isBase64, int widthScalePercent, int fontHeightScalePercent, TGS_RunnableType1<TS_FileTmcrFileWeb> web) {
        var instance = new TS_FileTmcrFileWeb(enabled, localFile, remoteFile, isBase64, widthScalePercent, fontHeightScalePercent);
        try {
            instance.use_init(macroGlobals);
            web.run(instance);
        } catch (Exception e) {
            instance.saveFile(e.getMessage());
            throw e;
        } finally {
            instance.saveFile(null);
        }
    }

    private void use_init(TS_FileTmcrParser_Globals macroGlobals) {
        this.macroGlobals = macroGlobals;
        if (isClosed()) {
            return;
        }
        webWriter = new TGS_FileHtml(macroGlobals.funcName, favIconPng, macroGlobals.customDomain);
    }

    @Override
    public boolean saveFile(String errorSource) {
        if (isClosed()) {
            return true;
        }
        setClosed();
        d.ci("saveFile.Web->");
        if (webWriter == null) {
            d.ci("Web File is null");
        } else {
            TS_FileHtmlUtils.write2File(webWriter, getLocalFileName());
            if (TS_FileUtils.isExistFile(getLocalFileName())) {
                d.ci("saveFile.FIX: Web File save", getLocalFileName(), "successfull");
            } else {
                d.ce("saveFile.FIX: Web File save", getLocalFileName(), "failed");
            }
        }
        return errorSource == null;
    }

    @Override
    public boolean createNewPage(int pageSizeAX, boolean landscape, Integer marginLeft, Integer marginRight, Integer marginTop, Integer marginBottom) {
        if (isClosed()) {
            return true;
        }
        if (pageSizeAX < 0) {
            this.pageSizeAX = null;
        } else if (pageSizeAX > pageSizeMaxWidth.length) {
            this.pageSizeAX = pageSizeMaxWidth.length - 1;
        } else {
            this.pageSizeAX = pageSizeAX;
        }
        if (table != null) {
            d.ce("createNewPage.ERROR: MIFWeb.createNewPage -> why table exists!");
            return false;
        }
        if (isFirstPageTriggered) {
            webWriter.getChilderen().add(new TGS_FileHtmlHR());
        } else {
            isFirstPageTriggered = true;
        }
        return true;
    }
    private boolean isFirstPageTriggered = false;
    private Integer pageSizeAX = null;

    private boolean addImageWeb(String imageLoc, int width, int heights, int rotationInDegrees_0_90_180_270, long imageCounter) {
        if (isClosed()) {
            return true;
        }
        return TGS_UnSafe.call(() -> {
            if (isBase64) {
                d.ci("addImageWeb", "imageLoc", imageLoc);
            }
            var mImageLoc = imageLoc;
            var mWidth = width;
            var mHeight = heights;
            if (rotationInDegrees_0_90_180_270 == 90 || rotationInDegrees_0_90_180_270 == 270) {
//                var tmp = mWidth;
                mWidth = mHeight;
//                mHeight = tmp;
            }
//            d.ci("addImageWeb", "w", mWidth, "h", mHeight, "r", rotationInDegrees_0_90_180_270);
            mImageLoc = TS_FileTmcrFileConverter.convertLocalLocationToRemote(macroGlobals, mImageLoc);
            if (mImageLoc == null) {
                d.ce("addImageWeb", "Cannot convertLocalLocationToRemote", mImageLoc);
                return false;
            }

            var sw = mWidth + "px";
//            var sh = height + "px";
            if (pageSizeAX != null) {
                var maxWidth = pageSizeMaxWidth[pageSizeAX];
                var maxPermissableWidth = (int) (Math.round(maxWidth * widthScalePercent / 100f));
                var selectedWidth = mWidth > maxPermissableWidth ? maxPermissableWidth : mWidth;
                sw = selectedWidth + "px";
//                sh = ((int) (selectedWidth * height / width) + "px");
            }
            var image = isBase64
                    ? new TS_FileHtmlImage64(TS_FileHtmlImage64.class.getSimpleName() + "_" + imageCounter, imageLoc.startsWith("http") ? mImageLoc : imageLoc, sw, "auto", String.valueOf(rotationInDegrees_0_90_180_270))
                    : new TS_FileHtmlImage(TS_FileHtmlImage.class.getSimpleName() + "_" + imageCounter, mImageLoc, sw, "auto", String.valueOf(rotationInDegrees_0_90_180_270));
            if (tableRowCell == null) {
                webWriter.getChilderen().add(image);
            } else {
                tableRowCell.getChilderen().add(image);
            }
            return true;
        }, e -> {
            d.ce("addImageWeb", e);
            return true;
        });
    }

    @Override
    public boolean addImage(BufferedImage pstImage, Path pstImageLoc, boolean textWrap, int left0_center1_right2, long imageCounter) {
        if (isClosed()) {
            return true;
        }
        boolean result;
        d.ci("addImage", "init", "imageLoc", pstImageLoc);
        result = addImageWeb(pstImageLoc.toAbsolutePath().toString(), pstImage.getWidth(), pstImage.getHeight(), 0, imageCounter);
        d.ci("addImage", "fin");
        return result;
    }

    @Override
    public boolean beginTable(int[] table_relColSizes) {
        if (isClosed()) {
            return true;
        }
        this.table_relColSizes = table_relColSizes;
        if (table != null) {
            d.ce("beginTable.ERROR: MIFWeb.beginTable -> table already exists");
            return false;
        }
        if (tableRow != null) {
            d.ce("beginTable.ERROR: MIFWeb.beginTable -> tableRow already exists");
            return false;
        }

        //ADD TABLE
        if (pageSizeAX == null || pageSizeAX >= pageSizeMaxWidth.length) {
            d.ce("beginTable", "pageSizeAX.fixing...", pageSizeAX);
            pageSizeAX = pageSizeMaxWidth.length - 1;
            d.ce("beginTable", "pageSizeAX.fixed", pageSizeAX);
        }
        var pageSizeFix = "max-width:" + (pageSizeAX == null ? "null" : ((int) (Math.round(pageSizeMaxWidth[pageSizeAX] * widthScalePercent / 100f)) + "px")) + ";";
        table = new TGS_FileHtmlTable("TK_POJOHTMLTable_" + TGS_FileHtmlTable.counter, pageSizeFix + "border-spacing:0;border-collapse:collapse;border:1px solid black; width:100%;");
        webWriter.getChilderen().add(table);

        //ADD ROW
        currentRowIndex = 0;
        tableRow = new TGS_FileHtmlTableRow("TK_POJOHTMLTableRow_" + TGS_FileHtmlTableRow.counter);
        var escape = new TS_FileHtmlEscape();
        Arrays.stream(table_relColSizes).forEachOrdered(rcs -> {
            tableRow.getChilderen().add(new TGS_FileHtmlTableRowCellVacant(escape));
        });
        table.getChilderen().add(tableRow);
        return true;
    }
    private int currentRowIndex = 0;
    private TGS_FileHtmlTable table = null;
    private int[] table_relColSizes = null;
    private TGS_FileHtmlTableRow tableRow = null;
    final private int[] pageSizeMaxWidth = new int[]{1330, 1330, 1330, 1330, 1330};//TODO PAGE SIZE CALCULATIONS is a mess

    @Override
    public boolean endTable() {
        if (isClosed()) {
            return true;
        }
        if (table == null) {
            d.ce("endTable.ERROR: MIFWeb.endTable -> table not exists");
            return false;
        }
        if (tableRow == null) {
            d.ce("endTable.ERROR: MIFWeb.endTable -> tableRow not exists");
            return false;
        }
        table = null;
        tableRow = null;
        currentRowIndex = 0;
        return true;
    }

    @Override
    public boolean beginTableCell(int rowSpan, int colSpan, Integer cellHeight) {
        if (isClosed()) {
            return true;
        }
        if (tableRowCell != null) {
            d.ce("beginTableCell.ERROR: MIFWeb.beginTableCell -> why tableRowCell exists!");
            return false;
        }
        if (tableRow == null) {
            d.ce("beginTableCell.ERROR: MIFWeb.beginTableCell -> why tableRow not exists!");
            return false;
        }
        if (table == null) {
            d.ce("beginTableCell.ERROR: MIFWeb.beginTableCell -> why table not exists!");
            return false;
        }

        //CALCULATE rowCellColSpanOffset
        var rowCellColSpanOffset = calcultaeRowCellColSpanOffset();
        if (rowCellColSpanOffset == -1) {
            return false;
        }
        while (checkMaxColumnSize(rowCellColSpanOffset)) {
            rowCellColSpanOffset = calcultaeRowCellColSpanOffset();
            if (rowCellColSpanOffset == -1) {
                return false;
            }
        }

        //SET CELL
        {
            if (table_relColSizes.length <= rowCellColSpanOffset) {
                d.ce("beginTableCell.ERROR: MIFWeb.beginTableCell -> FAILED(table_relColSizes.length <= cellColCounter)");
                return false;
            }
            var escape = new TS_FileHtmlEscape();
            tableRowCell = new TGS_FileHtmlTableRowCell(escape, "TK_POJOHTMLTableRowCell_" + TGS_FileHtmlTableRowCell.counter, String.valueOf(rowSpan), String.valueOf(colSpan), "");
            tableRow.getChilderen().set(rowCellColSpanOffset, tableRowCell);
        }

        var sumWidth = 0;//SET STYLE
        for (var c = 0; c < colSpan; c++) {
            if (rowCellColSpanOffset + c <= table_relColSizes.length - 1) {
                sumWidth += table_relColSizes[rowCellColSpanOffset + c];
            } else {
                d.ci("beginTableCell.ERROR: MIFWeb.beginTableCell -> sumWidth WHY CANOT ADD COLSPANWIDTH: rowCellColSpanOffset + c <= table_relColSizes.length - 1", "rowCellColSpanOffset", rowCellColSpanOffset, "table_relColSizes.length", table_relColSizes.length);
            }
        }
        var pageSizeFix = "";//max-width:" + (pageSizeAX == null ? "null" : ((int) (Math.round(pageSizeMaxWidth[pageSizeAX] * widthScalePercent / 100f)) + "px")) + ";";
        var styleWidth = TGS_StringUtils.concat("width:", String.valueOf(sumWidth), "%;");
        var styleHeight = cellHeight == null ? "" : TGS_StringUtils.concat("height:", String.valueOf(cellHeight), "px;");
        tableRowCell.setStyle_Properties2(TGS_StringUtils.concat(pageSizeFix, "vertical-align:top;border:1px solid black;", styleWidth, styleHeight));

        var fRowCellColSpanOffset = rowCellColSpanOffset;
        var escape = new TS_FileHtmlEscape();
        IntStream.range(1, colSpan).forEach(ci -> {//ADD COLSPAN FILL TODO
            if (fRowCellColSpanOffset + ci <= table_relColSizes.length - 1) {
                tableRow.getChilderen().set(fRowCellColSpanOffset + ci,
                        new TGS_FileHtmlTableRowCellOccupied(escape)
                );
            } else {
                d.ci("beginTableCell.ERROR: MIFWeb.beginTableCell -> eColSpan WHY CANOT ADD COLSPANFULL: rowCellColSpanOffset + c <= table_relColSizes.length - 1", "rowCellColSpanOffset", fRowCellColSpanOffset, "table_relColSizes.length", table_relColSizes.length);
            }
        });
        //ADD ROWSPAN FILL
        IntStream.range(1, rowSpan).forEachOrdered(ri -> {
            final TGS_FileHtmlTableRow nextRow;
            if (table.getChilderen().size() <= currentRowIndex + ri) {
                nextRow = new TGS_FileHtmlTableRow("TK_POJOHTMLTableRow_" + TGS_FileHtmlTableRow.counter);
                Arrays.stream(table_relColSizes).forEachOrdered(rcs -> {
                    nextRow.getChilderen().add(new TGS_FileHtmlTableRowCellVacant(escape));
                });
                table.getChilderen().add(nextRow);
            } else {
                nextRow = (TGS_FileHtmlTableRow) table.getChilderen().get(currentRowIndex + ri);
            }
            IntStream.range(0, colSpan).forEach(ci -> {
                nextRow.getChilderen().set(fRowCellColSpanOffset + ci,
                        new TGS_FileHtmlTableRowCellOccupied(escape)
                );
            });
        });
        return true;
    }
    private TGS_FileHtmlTableRowCell tableRowCell = null;

    private int calcultaeRowCellColSpanOffset() {
        var rowCellColSpanOffset = 0;
        if (isClosed()) {
            return rowCellColSpanOffset;
        }
        for (var eRowCell : tableRow.getChilderen()) {//INC
            if (eRowCell instanceof TGS_FileHtmlTableRowCell) {
                if (eRowCell instanceof TGS_FileHtmlTableRowCellVacant) {
                    break;
                } else if (eRowCell instanceof TGS_FileHtmlTableRowCellOccupied) {
                    rowCellColSpanOffset += 1; //from rowspan
                } else {
                    rowCellColSpanOffset += 1; //full already adds 1
                }
            } else {
                d.ce("calcultaeRowCellColSpanOffset.ERROR: MIFWeb.beginTableCell -> e NOT instanceof TK_POJOHTMLTableRowCell: " + eRowCell);
                return -1;
            }
        }
        d.ci("calcultaeRowCellColSpanOffset.MIFWeb.calcultaeRowCellColSpanOffset", "rowCellColSpanMax: " + table_relColSizes.length, "rowCellColSpanOffset", rowCellColSpanOffset);
        return rowCellColSpanOffset;
    }

    private boolean checkMaxColumnSize(int rowCellColSpanOffset) {
        if (isClosed()) {
            return true;
        }
        var escape = new TS_FileHtmlEscape();
        var rowAdded = false;
        if (table_relColSizes.length <= rowCellColSpanOffset) {
            rowAdded = true;
            if (table.getChilderen().size() - 1 == currentRowIndex) {
                currentRowIndex++;
                tableRow = new TGS_FileHtmlTableRow("TK_POJOHTMLTableRow_" + TGS_FileHtmlTableRow.counter);
                Arrays.stream(table_relColSizes).forEachOrdered(rcs -> {
                    tableRow.getChilderen().add(new TGS_FileHtmlTableRowCellVacant(escape));
                });
                table.getChilderen().add(tableRow);
                d.ci("checkMaxColumnSize.MIFWeb.beginTableCell.checkMaxColumnSize.DECISION: NEWROW_ADDED");
            } else {
                currentRowIndex++;
                tableRow = (TGS_FileHtmlTableRow) table.getChilderen().get(currentRowIndex);
                d.ci("checkMaxColumnSize.MIFWeb.beginTableCell.checkMaxColumnSize.DECISION: ROW_ALREADY_EXISTS");
            }
        } else {
            d.ci("checkMaxColumnSize.MIFWeb.beginTableCell.checkMaxColumnSize.DECISION: PASS");
        }
        return rowAdded;
    }

    @Override
    public boolean endTableCell(int rotationInDegrees_0_90_180_270) {
        if (isClosed()) {
            return true;
        }
        if (tableRow == null) {
            d.ce("endTableCell.ERROR: MIFWeb.endTableCell -> why tableRow not exists!");
            return false;
        }
        if (tableRowCell == null) {
            d.ce("endTableCell.ERROR: MIFWeb.endTableCell -> why tableRowCell not exists!");
            return false;
        }
        tableRowCell = null;
        return true;
    }

    @Override
    public boolean beginText(int allign_Left0_center1_right2_just3) {
        if (isClosed()) {
            return true;
        }
        if (parag != null) {
            d.ce("beginText.ERROR: MIFWeb.beginText -> why parag exists!");
            return false;
        }
        String allignText;
        allignText = switch (allign_Left0_center1_right2_just3) {
            case 1 ->
                "center";
            case 2 ->
                "right";
            default ->
                "left";
        };
        var escape = new TS_FileHtmlEscape();
        parag = new TGS_FileHtmlParagraph(escape, "TK_POJOHTMLParagraph_" + TGS_FileHtmlParagraph.counter, "padding:0px; margin:0px;text-align:" + allignText + ";");
        return true;
    }
    private TGS_FileHtmlParagraph parag = null;

    @Override
    public boolean endText() {
        if (isClosed()) {
            return true;
        }
        if (parag == null) {
            d.ce("endText.ERROR: MIFWeb.endText -> why not exists!");
            return false;
        }
        if (tableRowCell == null) {
            webWriter.getChilderen().add(parag);
        } else {
            tableRowCell.getChilderen().add(parag);
        }
        parag = null;
        return true;
    }

    @Override
    public boolean addText(String text) {
        if (isClosed()) {
            return true;
        }
        if (parag == null) {
            d.ce("addText.ERROR: MIFWeb.addText -> why parag not exists!");
            return false;
        }
        var lines = TS_StringUtils.toList(text, "\n");
        var escape = new TS_FileHtmlEscape();
        IntStream.range(0, lines.size()).forEachOrdered(i -> {
            var line = lines.get(i);
            if (!line.isEmpty()) {
                if (!TGS_StringDouble.may(text)) {
                    var span = new TGS_FileHtmlSpan(escape, "TK_POJOHTMLSpan_" + TGS_FileHtmlSpan.counter, line, getFont());
                    parag.getChilderen().add(span);
                } else {
                    var tags = TS_StringUtils.toList_spc(line);
                    IntStream.range(0, tags.size()).forEachOrdered(j -> {
                        var tag = tags.get(j);
                        var dbl = TGS_StringDouble.of(text);
                        if (dbl.isEmpty()) {
                            var span = new TGS_FileHtmlSpan(escape, "TK_POJOHTMLSpan_" + TGS_FileHtmlSpan.counter, tag, getFont());
                            parag.getChilderen().add(span);
                        } else {
                            var htmlText = TGS_StringUtils.concat(String.valueOf(dbl.get().left), "<sub>", String.valueOf(dbl.get().dim()), String.valueOf(dbl.get().right), "</sub>");
                            var span = new TGS_FileHtmlSpan(escape, "TK_POJOHTMLSpan_" + TGS_FileHtmlSpan.counter, htmlText, getFont());
                            span.pureCode = true;
                            parag.getChilderen().add(span);
                        }
                        if (tags.size() - 1 != j) {
                            var span = new TGS_FileHtmlSpan(escape, "TK_POJOHTMLSpan_spc" + TGS_FileHtmlSpan.counter, " ", getFont());
                            parag.getChilderen().add(span);
                        }
                    });
                }
            }
            if (i != lines.size() - 1 || text.endsWith("\n")) {
                addLineBreak();
            }
        });
        return true;
    }

    @Override
    public boolean addLineBreak() {
        if (isClosed()) {
            return true;
        }
        if (parag == null) {
            d.ce("addLineBreak.ERROR: MIFWeb.addLineBreak -> why not exists!");
            return false;
        }
        parag.getChilderen().add(new TGS_FileHtmlBR());
        return true;
    }

    @Override
    public boolean setFontStyle() {
        if (isClosed()) {
            return true;
        }
        styleIterator = null;
        getFont();
        return true;
    }

    @Override
    public boolean setFontHeight() {
        if (isClosed()) {
            return true;
        }
        styleIterator = null;
        getFont();
        return true;
    }

    @Override
    public boolean setFontColor() {
        if (isClosed()) {
            return true;
        }
        styleIterator = null;
        getFont();
        return true;
    }

    private String getFont() {
        if (styleIterator == null) {
            var calculatedfontHeight = (int) (Math.round((macroGlobals.fontHeight + FONT_HEIGHT_OFFSET()) * fontHeightScalePercent / 100f));
            while (calculatedfontHeight < 1) {
                calculatedfontHeight++;
            }
            styleIterator = "font-family:fontText, Arial Unicode MS, Arial,Helvetica,sans-serif;color:" + getFontColor() + ";font-size:" + calculatedfontHeight + "px;font-style:" + (macroGlobals.fontItalic ? "italic" : "normal") + ";font-weight:" + (macroGlobals.fontBold ? "bold" : "normal") + ";";
        }
        return styleIterator;
    }
    private String styleIterator = null;

    private String getFontColor() {
        if (isClosed()) {
            return "#000000";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_BLUE())) {
            return "#0000FF";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_CYAN())) {
            return "#00FFFF";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_DARK_GRAY())) {
            return "#505050";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_GRAY())) {
            return "#808080";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_GREEN())) {
            return "#008000";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_LIGHT_GRAY())) {
            return "#D3D3D3";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_MAGENTA())) {
            return "#FF00FF";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_ORANGE())) {
            return "#FFA500";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_PINK())) {
            return "#FFC0CB";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_RED())) {
            return "#FF0000";
        }
        if (Objects.equals(macroGlobals.fontColor, TS_FileTmcrCodeFontTags.CODE_TOKEN_FONT_COLOR_YELLOW())) {
            return "#FFFF00";
        }
        return customCssForBlackText == null ? "#000000" : customCssForBlackText;
    }
}
