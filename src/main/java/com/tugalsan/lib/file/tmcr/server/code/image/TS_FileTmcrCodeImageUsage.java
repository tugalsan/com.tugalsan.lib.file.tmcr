package com.tugalsan.lib.file.tmcr.server.code.image;

public class TS_FileTmcrCodeImageUsage {

    public static void addUsageCode(StringBuilder sb) {
        sb.append("//  ").append(TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE())
                .append(" MAXWIDTH/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append(" MAXHEIGHT/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append(" RESPECT/NULL ")
                .append(TS_FileTmcrCodeImageTags.CODE_TOKEN_LEFT()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_RIGHT()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_CENTER())
                .append(" ").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append(" LOC")
                .append(" ROTATION/0/90/180/270 ")
                .append(TS_FileTmcrCodeImageTags.CODE_TOKEN_CREATE()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append("\n");
        sb.append("//  ").append(TS_FileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL())
                .append(" MAXWIDTH/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append(" MAXHEIGHT/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append(" RESPECT/NULL ").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_LEFT()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_RIGHT()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_CENTER())
                .append(" ").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append(" VAR")
                .append(" ID")
                .append(" ROTATION/0/90/180/270 ")
                .append(TS_FileTmcrCodeImageTags.CODE_TOKEN_CREATE()).append("/").append(TS_FileTmcrCodeImageTags.CODE_TOKEN_NULL())
                .append("\n");
    }
}
