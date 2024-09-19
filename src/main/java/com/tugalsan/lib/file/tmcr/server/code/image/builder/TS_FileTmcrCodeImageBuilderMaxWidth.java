package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_FileTmcrCodeImageBuilderMaxWidth {

    protected TS_FileTmcrCodeImageBuilderMaxWidth(Integer maxWidthNullable) {
        this.maxWidthNullable = maxWidthNullable;
    }
    final Integer maxWidthNullable;

    public TS_FileTmcrCodeImageBuilderMaxHeight maxHeight(Integer maxHeightNullable) {
        return new TS_FileTmcrCodeImageBuilderMaxHeight(maxWidthNullable, maxHeightNullable);
    }

    public TS_FileTmcrCodeImageBuilderMaxHeight maxHeightNull() {
        return maxHeight(null);
    }
}
