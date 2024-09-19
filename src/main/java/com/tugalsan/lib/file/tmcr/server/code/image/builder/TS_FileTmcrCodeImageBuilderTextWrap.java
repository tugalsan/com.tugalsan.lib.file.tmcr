package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_FileTmcrCodeImageBuilderTextWrap {

    protected TS_FileTmcrCodeImageBuilderTextWrap(Integer maxWidthNullable, Integer maxHeightNullable, 
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

    public TS_FileTmcrCodeImageBuilderRotation rotateDisable() {
        return new TS_FileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, 0);
    }

    public TS_FileTmcrCodeImageBuilderRotation rotateDegrees_90() {
        return new TS_FileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, 90);
    }

    public TS_FileTmcrCodeImageBuilderRotation rotateDegrees_180() {
        return new TS_FileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, 180);
    }

    public TS_FileTmcrCodeImageBuilderRotation rotateDegrees_270() {
        return new TS_FileTmcrCodeImageBuilderRotation(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, 270);
    }

}
