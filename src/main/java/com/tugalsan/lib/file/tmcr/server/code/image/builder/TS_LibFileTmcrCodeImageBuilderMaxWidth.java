package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_LibFileTmcrCodeImageBuilderMaxWidth {

    protected TS_LibFileTmcrCodeImageBuilderMaxWidth(Integer maxWidthNullable) {
        this.maxWidthNullable = maxWidthNullable;
    }
    final Integer maxWidthNullable;

    public TS_LibFileTmcrCodeImageBuilderMaxHeight maxHeight(Integer maxHeightNullable) {
        return new TS_LibFileTmcrCodeImageBuilderMaxHeight(maxWidthNullable, maxHeightNullable);
    }

    public TS_LibFileTmcrCodeImageBuilderMaxHeight maxHeightNull() {
        return maxHeight(null);
    }
}
