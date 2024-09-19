package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_LibFileTmcrCodeImageBuilder {

    public static TS_LibFileTmcrCodeImageBuilderMaxWidth maxWidth(Integer maxWidthNullable) {
        return new TS_LibFileTmcrCodeImageBuilderMaxWidth(maxWidthNullable);
    }

    public static TS_LibFileTmcrCodeImageBuilderMaxWidth maxWidthNull() {
        return maxWidth(null);
    }
}
