package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_LibFileTmcrCodeImageBuilderAllign {

    protected TS_LibFileTmcrCodeImageBuilderAllign(Integer maxWidthNullable, Integer maxHeightNullable,
            boolean respectOrientation, int left0_right1_center2) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
        this.respectOrientation = respectOrientation;
        this.left0_right1_center2 = left0_right1_center2;
    }
    final Integer maxWidthNullable, maxHeightNullable;
    final boolean respectOrientation;
    final int left0_right1_center2;

    public TS_LibFileTmcrCodeImageBuilderTextWrap textWrap(boolean enable) {
        return new TS_LibFileTmcrCodeImageBuilderTextWrap(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, enable);
    }
}
