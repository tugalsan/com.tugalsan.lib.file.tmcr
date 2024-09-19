package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_LibFileTmcrCodeImageBuilderMaxHeight {

    protected TS_LibFileTmcrCodeImageBuilderMaxHeight(Integer maxWidthNullable, Integer maxHeightNullable) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
    }
    final Integer maxWidthNullable, maxHeightNullable;

    public TS_LibFileTmcrCodeImageBuilderRespect respectOrientation(boolean enable) {
        return new TS_LibFileTmcrCodeImageBuilderRespect(maxWidthNullable, maxHeightNullable, enable);
    }
}
