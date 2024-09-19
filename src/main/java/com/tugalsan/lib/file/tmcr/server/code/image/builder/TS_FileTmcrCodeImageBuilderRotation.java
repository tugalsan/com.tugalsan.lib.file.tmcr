package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_FileTmcrCodeImageBuilderRotation {

    protected TS_FileTmcrCodeImageBuilderRotation(Integer maxWidthNullable, Integer maxHeightNullable,
            boolean respectOrientation, int left0_right1_center2, boolean textWrap, int rotation_0_90_180_270) {
        this.maxWidthNullable = maxWidthNullable;
        this.maxHeightNullable = maxHeightNullable;
        this.respectOrientation = respectOrientation;
        this.left0_right1_center2 = left0_right1_center2;
        this.textWrap = textWrap;
        this.rotation_0_90_180_270 = rotation_0_90_180_270;
    }
    final Integer maxWidthNullable, maxHeightNullable;
    final boolean respectOrientation, textWrap;
    final int left0_right1_center2;
    final int rotation_0_90_180_270;

    public TS_FileTmcrCodeImageBuilderCreateFromTemplate ifNotExistsCreateFromTemplate(boolean enable) {
        return new TS_FileTmcrCodeImageBuilderCreateFromTemplate(maxWidthNullable, maxHeightNullable, respectOrientation, left0_right1_center2, textWrap, rotation_0_90_180_270, enable);
    }
}
