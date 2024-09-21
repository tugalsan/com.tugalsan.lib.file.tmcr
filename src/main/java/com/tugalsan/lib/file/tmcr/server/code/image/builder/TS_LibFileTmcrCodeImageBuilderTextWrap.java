package com.tugalsan.lib.file.tmcr.server.code.image.builder;

import com.tugalsan.lib.file.tmcr.server.code.image.TS_LibFileTmcrCodeImageTags;

public class TS_LibFileTmcrCodeImageBuilderTextWrap {

    protected TS_LibFileTmcrCodeImageBuilderTextWrap(Integer maxWidthNullable, Integer maxHeightNullable,
            boolean respectOrientation, int left0_right1_center2, boolean textWrap) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
        this.respectOrientation = respectOrientation;
        this.left0_right1_center2 = left0_right1_center2;
        this.textWrap = textWrap;
    }
    final Integer maxWidthNullable, maxHeightNullable;
    final boolean respectOrientation, textWrap;
    final int left0_right1_center2;

    public TS_LibFileTmcrCodeImageBuilderRotation rotatePortrait() {
        return new TS_LibFileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_PORTRAIT());
    }

    public TS_LibFileTmcrCodeImageBuilderRotation rotateLandscape() {
        return new TS_LibFileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LANDSCAPE());
    }

    public TS_LibFileTmcrCodeImageBuilderRotation rotateDisable() {
        return new TS_LibFileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, "0");
    }

    public TS_LibFileTmcrCodeImageBuilderRotation rotateDegrees_90() {
        return new TS_LibFileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, "90");
    }

    public TS_LibFileTmcrCodeImageBuilderRotation rotateDegrees_180() {
        return new TS_LibFileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, "180");
    }

    public TS_LibFileTmcrCodeImageBuilderRotation rotateDegrees_270() {
        return new TS_LibFileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, "270");
    }

}
