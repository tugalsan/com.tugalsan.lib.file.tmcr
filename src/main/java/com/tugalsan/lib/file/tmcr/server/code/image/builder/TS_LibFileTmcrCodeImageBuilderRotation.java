package com.tugalsan.lib.file.tmcr.server.code.image.builder;

import com.tugalsan.api.url.client.TGS_Url;
import com.tugalsan.lib.file.tmcr.server.code.image.TS_LibFileTmcrCodeImageTags;
import java.nio.file.Path;

public class TS_LibFileTmcrCodeImageBuilderRotation {

    protected TS_LibFileTmcrCodeImageBuilderRotation(Integer maxWidthNullable, Integer maxHeightNullable,
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

    public StringBuilder buildFromUrl(TGS_Url url) {
        return buildFromPathOrUrl_do(url.url);
    }

    public StringBuilder buildFromPath(Path path) {
        return buildFromPathOrUrl_do(path.toAbsolutePath().toString());
    }

    private StringBuilder buildFromPathOrUrl_do(CharSequence pathOrUrl) {
        var sb = new StringBuilder();
        sb.append(TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE());
        sb.append(" ").append(maxWidthNullable == null ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL() : maxWidthNullable.toString());
        sb.append(" ").append(maxHeightNullable == null ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL() : maxHeightNullable.toString());
        sb.append(" ").append(respectOrientation ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RESPECT() : TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        switch (left0_right1_center2) {
            case 2 ->
                sb.append(" ").append(TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CENTER());
            case 1 ->
                sb.append(" ").append(TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RIGHT());
            default -> //0
                sb.append(" ").append(TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LEFT());
        }
        sb.append(" ").append(textWrap ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP() : TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        sb.append(" ").append(pathOrUrl);
        var fixedRotation = rotation_0_90_180_270;
        while (fixedRotation < 0) {
            fixedRotation += 360;
        }
        while (fixedRotation > 360) {
            fixedRotation -= 360;
        }
        if (fixedRotation > 270) {
            sb.append(" ").append(270);
        } else if (fixedRotation > 180) {
            sb.append(" ").append(180);
        } else if (fixedRotation > 90) {
            sb.append(" ").append(90);
        } else {
            sb.append(" ").append(0);
        }
        return sb;
    }

    public StringBuilder buildFromSql(String tablename_dot_columnName, long id) {
        return buildFromSql(tablename_dot_columnName, String.valueOf(id));
    }

    public StringBuilder buildFromSql(String tablename_dot_columnName, String id) {
        return buildFromSql_do(true, tablename_dot_columnName, id);
    }

    @Deprecated //NO NEED
    public StringBuilder buildFromSql_ifNotExistsDoNotCreateFromTemplate(String tablename_dot_columnName, String id) {
        return buildFromSql_do(false, tablename_dot_columnName, id);
    }

    private StringBuilder buildFromSql_do(boolean ifNotExistsCreateFromTemplate, String tablename_dot_columnName, String id) {
        var sb = new StringBuilder();
        sb.append(TS_LibFileTmcrCodeImageTags.CODE_INSERT_IMAGE_FROMSQL());
        sb.append(" ").append(maxWidthNullable == null ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL() : maxWidthNullable.toString());
        sb.append(" ").append(maxHeightNullable == null ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL() : maxHeightNullable.toString());
        sb.append(" ").append(respectOrientation ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RESPECT() : TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        switch (left0_right1_center2) {
            case 2 ->
                sb.append(" ").append(TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CENTER());
            case 1 ->
                sb.append(" ").append(TS_LibFileTmcrCodeImageTags.CODE_TOKEN_RIGHT());
            default -> //0
                sb.append(" ").append(TS_LibFileTmcrCodeImageTags.CODE_TOKEN_LEFT());
        }
        sb.append(" ").append(textWrap ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_TEXTWRAP() : TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        sb.append(" ").append(tablename_dot_columnName);
        sb.append(" ").append(id);
        var fixedRotation = rotation_0_90_180_270;
        while (fixedRotation < 0) {
            fixedRotation += 360;
        }
        while (fixedRotation > 360) {
            fixedRotation -= 360;
        }
        if (fixedRotation > 270) {
            sb.append(" ").append(270);
        } else if (fixedRotation > 180) {
            sb.append(" ").append(180);
        } else if (fixedRotation > 90) {
            sb.append(" ").append(90);
        } else {
            sb.append(" ").append(0);
        }
        sb.append(" ").append(ifNotExistsCreateFromTemplate ? TS_LibFileTmcrCodeImageTags.CODE_TOKEN_CREATE() : TS_LibFileTmcrCodeImageTags.CODE_TOKEN_NULL());
        return sb;
    }
}
