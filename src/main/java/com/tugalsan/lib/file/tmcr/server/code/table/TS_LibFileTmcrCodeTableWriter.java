package com.tugalsan.lib.file.tmcr.server.code.table;

import module com.tugalsan.api.list;
import module com.tugalsan.api.log;
import module com.tugalsan.api.sql.restbl;
import module com.tugalsan.api.sql.resultset;
import module com.tugalsan.api.thread;
import module com.tugalsan.api.time;
import module com.tugalsan.lib.file.tmcr;
import java.util.stream.*;

public class TS_LibFileTmcrCodeTableWriter {

    final private static TS_Log d = TS_Log.of(TS_LibFileTmcrCodeTableWriter.class);

    public static String SET_TABLECELL_NOBORDER(boolean enabled) {
        return TS_LibFileTmcrCodeTableTags.CODE_TABLECELL_BORDER() + (enabled ? " NOBORDER\n" : " BORDER\n");
    }

    public static String END_TABLECELL() {
        return TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL() + "\n";
    }

    public static String END_TABLECELL(int rotation_0_90_180_270) {
        if (rotation_0_90_180_270 == 90) {
            return TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL_90() + "\n";
        }
        if (rotation_0_90_180_270 == 180) {
            return TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL_180() + "\n";
        }
        if (rotation_0_90_180_270 == 270) {
            return TS_LibFileTmcrCodeTableTags.CODE_END_TABLECELL_270() + "\n";
        }
        return END_TABLECELL();
    }

    public static String BEGIN_TABLECELL(Integer rowSpan_orNull, Integer colSpan_orNull, Integer height_orNull) {
        return TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLECELL() + " " + (rowSpan_orNull == null ? TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL() : rowSpan_orNull) + " " + (colSpan_orNull == null ? TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL() : colSpan_orNull) + " " + (height_orNull == null ? TS_LibFileTmcrCodeTableTags.CODE_TOKEN_NULL() : height_orNull) + "\n";
    }

    public static String BEGIN_TABLECELL() {
        return BEGIN_TABLECELL(null, null, null);
    }

    public static String END_TABLE() {
        return TS_LibFileTmcrCodeTableTags.CODE_END_TABLE() + "\n";
    }

    public static String BEGIN_TABLE(int columnSize[]) {
        var s = TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLE();
        for (var colInteger : columnSize) {
            s += " " + colInteger;
        }
        return s + "\n";
    }

    public static String BEGIN_TABLE(int columnCount) {
        return TS_LibFileTmcrCodeTableTags.CODE_BEGIN_TABLE() + " " + columnCount + "\n";
    }

    public static String getMacroCodeTable(TS_ThreadSyncTrigger servletKillTrigger, TGS_ListTable t, int colPercentage[], int fontsizeHeader, int fontsizeData, boolean sqlCellprefixIsBold) {
        return getMacroCodeTable(servletKillTrigger, t, colPercentage, fontsizeHeader, fontsizeData, false, sqlCellprefixIsBold);
    }

    public static String getMacroCodeTable(TS_ThreadSyncTrigger servletKillTrigger, TGS_ListTable t, int colPercentage[], int fontsizeHeader, int fontsizeData, boolean rotateHeader_90, boolean sqlCellprefixIsBold) {
        if (t == null) {
            return "";
        }
        var columnSize = t.getMaxColumnSize();
        if (columnSize == 0) {
            return "";
        }
        var rowSize = t.getRowSize();
//        if (rowSize == 0) {gerek yok
//            return "";
//        }
        var sb = new StringBuilder();
        if (colPercentage == null) {
            sb.append(BEGIN_TABLE(columnSize));
        } else {
            sb.append(BEGIN_TABLE(colPercentage));
        }

        for (var c = 0; c < columnSize; c++) {
            sb.append(t.isHeaderBold() ? TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_BOLD() : TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_PLAIN());
            sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_COLOR_BLACK());
            sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_SIZE(fontsizeHeader));
            sb.append(getMacroCodeTableCellObject(t.getValueAsObject(0, c), sqlCellprefixIsBold));
        }

        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_PLAIN());
        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_COLOR_BLACK());
        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_SIZE(fontsizeData));
        for (var r = 1; r < rowSize; r++) {
            if (servletKillTrigger.hasTriggered()) {
                return "";
            }
            for (var c = 0; c < columnSize; c++) {
                sb.append(getMacroCodeTableCellObject(t.getValueAsObject(r, c), sqlCellprefixIsBold));
            }
        }

        sb.append(END_TABLE());
        return sb.toString();
    }

    public static String getMacroCodeTable(TS_ThreadSyncTrigger servletKillTrigger, TS_SQLResultSet rs, int colPercentage[], int fontsizeHeader, int fontsizeData) {
        var size = rs.row.size();
        var sb = new StringBuilder();
        if (colPercentage == null) {
            sb.append(BEGIN_TABLE(rs.col.size()));
        } else {
            sb.append(BEGIN_TABLE(colPercentage));
        }

        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_BOLD());
        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_COLOR_BLACK());
        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_SIZE(fontsizeHeader));
        sb.append(getMacroCodeTableHeader(rs));

        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_PLAIN());
        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_COLOR_BLACK());
        sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_SIZE(fontsizeData));
        IntStream.range(0, size).forEachOrdered(ri -> {
            if (servletKillTrigger.hasTriggered()) {
                return;
            }
            rs.row.scrll(ri);
            sb.append(getMacroCodeTableRow(rs));
        });
        sb.append(END_TABLE());
        return sb.toString();
    }

    public static String getMacroCodeTableHeader(TS_SQLResultSet rs) {
        var sb = new StringBuilder();
        IntStream.range(0, rs.col.size()).forEachOrdered(i -> {
            sb.append(getMacroCodeTableHeaderColumn(rs, i));
        });
        return sb.toString();
    }

    public static String getMacroCodeTableRow(TS_SQLResultSet rs) {
        var sb = new StringBuilder();
        IntStream.range(0, rs.col.size()).forEachOrdered(ci -> {
            sb.append(getMacroCodeTableRowColumn(rs, ci));
        });
        return sb.toString();
    }

    public static String getMacroCodeTableRowColumn(TS_SQLResultSet rs, int ci) {
        return getMacroCodeTableCellString(rs.str.get(ci));
    }

    public static String getMacroCodeTableHeaderColumn(TS_SQLResultSet rs, int ci) {
        return getMacroCodeTableCellString(rs.col.name(ci));
    }

    public static String getMacroCodeTableCellString(String value) {
        var sb = new StringBuilder();
        sb.append(BEGIN_TABLECELL());
        sb.append(TS_LibFileTmcrCodeTextWriter.BEGIN_TEXT_LEFT());
        sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(value));
        sb.append(TS_LibFileTmcrCodeTextWriter.END_TEXT());
        sb.append(END_TABLECELL());
        return sb.toString();
    }

    public static String getMacroCodeTableCellObject(Object cellValue, boolean sqlCellprefixIsBold) {
        return getMacroCodeTableCellObject(cellValue, false, null, null, null, sqlCellprefixIsBold);
    }

    public static String getMacroCodeTableCellObject(Object cellValue, boolean rotateHeader_90, Integer rowSpan_orNull, Integer colSpan_orNull, Integer height_orNull, boolean sqlCellprefixIsBold) {
        var sb = new StringBuilder();
        sb.append(BEGIN_TABLECELL(rowSpan_orNull, colSpan_orNull, height_orNull));

        if (cellValue != null && cellValue instanceof TS_LibFileTmcrCodeTableObj_CellCode) {
            sb.append(((TS_LibFileTmcrCodeTableObj_CellCode) cellValue).code);
            sb.append(END_TABLECELL(rotateHeader_90 ? 90 : 0));
            return sb.toString();
        }

        sb.append(TS_LibFileTmcrCodeTextWriter.BEGIN_TEXT_LEFT());
        d.ci("getPDFMacroCodeTableCellObject.cellValue:[" + cellValue + "]");
        if (cellValue == null) {
            sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(""));
        } else if (cellValue instanceof TS_SQLResTblValue cellValueSQL) {
            d.ci("getPDFMacroCodeTableCellObject.cellValue IS instanceof TS_SQLTableValue");
            if (sqlCellprefixIsBold) {
                sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_BOLD());
            }
            var tmp0 = cellValueSQL.prefix;
            var tmp1 = cellValueSQL.prefix.trim();
            sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(tmp0));
            IntStream.range(0, tmp0.length() - tmp1.length()).forEach(i -> {
                sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT_SPC());
            });
            if (sqlCellprefixIsBold) {
                sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_PLAIN());
            }
            sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT_VAR_FROMSQL(cellValueSQL.tableAndColumnName, cellValueSQL.id));
            sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(cellValueSQL.suffix));
        } else {
            d.ci("getPDFMacroCodeTableCellObject.cellValue NOT instanceof TS_SQLTableValue");
            var cellValueText = String.valueOf(cellValue);
            var time = TGS_Time.ofTime_HH_MM(cellValueText);
            if (time != null) {
                sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(cellValueText));
            } else {
                if (sqlCellprefixIsBold) {
                    var idx = cellValueText.indexOf(":");
                    if (idx == -1) {
                        sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(cellValueText));
                    } else {
                        var prefix = cellValueText.substring(0, idx);
                        var text = cellValueText.substring(idx);
                        if (sqlCellprefixIsBold) {
                            sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_BOLD());
                        }
                        sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(prefix));
                        if (sqlCellprefixIsBold) {
                            sb.append(TS_LibFileTmcrCodeFontWriter.SET_FONT_STYLE_PLAIN());
                        }
                        sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(text));
                    }
                } else {
                    sb.append(TS_LibFileTmcrCodeTextWriter.ADD_TEXT(cellValueText));
                }
            }
        }
        sb.append(TS_LibFileTmcrCodeTextWriter.END_TEXT());
        sb.append(END_TABLECELL(rotateHeader_90 ? 90 : 0));
        return sb.toString();
    }

}
