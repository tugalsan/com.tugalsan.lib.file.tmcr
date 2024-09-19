package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_FileTmcrCodeImageBuilderMaxHeight {

    protected TS_FileTmcrCodeImageBuilderMaxHeight(Integer maxWidthNullable, Integer maxHeightNullable) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
    }
    final Integer maxWidthNullable, maxHeightNullable;

    public TS_FileTmcrCodeImageBuilderRespect respectOrientation(boolean enable) {
        return new TS_FileTmcrCodeImageBuilderRespect(maxWidthNullable, maxHeightNullable, enable);
    }
}
