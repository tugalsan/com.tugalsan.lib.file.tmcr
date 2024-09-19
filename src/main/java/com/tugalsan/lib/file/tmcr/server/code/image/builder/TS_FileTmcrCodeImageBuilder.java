package com.tugalsan.lib.file.tmcr.server.code.image.builder;

public class TS_FileTmcrCodeImageBuilder {

    public static TS_FileTmcrCodeImageBuilderMaxWidth maxWidth(Integer maxWidthNullable) {
        return new TS_FileTmcrCodeImageBuilderMaxWidth(maxWidthNullable);
    }

    public static TS_FileTmcrCodeImageBuilderMaxWidth maxWidthNull() {
        return maxWidth(null);
    }
}
