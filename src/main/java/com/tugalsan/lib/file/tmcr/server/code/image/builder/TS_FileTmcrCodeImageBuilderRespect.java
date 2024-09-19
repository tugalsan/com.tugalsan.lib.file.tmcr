package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_FileTmcrCodeImageBuilderRespect {

    protected TS_FileTmcrCodeImageBuilderRespect(Integer maxWidthNullable, Integer maxHeightNullable, 
            boolean respectOrientation) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
        this.respectOrientation = respectOrientation;
    }
    final Integer maxWidthNullable, maxHeightNullable;
    final boolean respectOrientation;

    public TS_FileTmcrCodeImageBuilderAllign allignLeft() {
        return new TS_FileTmcrCodeImageBuilderAllign(maxWidthNullable, maxHeightNullable, respectOrientation, 0);
    }

    public TS_FileTmcrCodeImageBuilderAllign allignRight() {
        return new TS_FileTmcrCodeImageBuilderAllign(maxWidthNullable, maxHeightNullable, respectOrientation, 1);
    }

    public TS_FileTmcrCodeImageBuilderAllign allignCenter() {
        return new TS_FileTmcrCodeImageBuilderAllign(maxWidthNullable, maxHeightNullable, respectOrientation, 2);
    }
}
