package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_LibFileTmcrCodeImageBuilderRespect {

    protected TS_LibFileTmcrCodeImageBuilderRespect(Integer maxWidthNullable, Integer maxHeightNullable, 
            boolean respectOrientation) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
        this.respectOrientation = respectOrientation;
    }
    final Integer maxWidthNullable, maxHeightNullable;
    final boolean respectOrientation;

    public TS_LibFileTmcrCodeImageBuilderAllign allignLeft() {
        return new TS_LibFileTmcrCodeImageBuilderAllign(maxWidthNullable, maxHeightNullable, respectOrientation, 0);
    }

    public TS_LibFileTmcrCodeImageBuilderAllign allignRight() {
        return new TS_LibFileTmcrCodeImageBuilderAllign(maxWidthNullable, maxHeightNullable, respectOrientation, 1);
    }

    public TS_LibFileTmcrCodeImageBuilderAllign allignCenter() {
        return new TS_LibFileTmcrCodeImageBuilderAllign(maxWidthNullable, maxHeightNullable, respectOrientation, 2);
    }
}
