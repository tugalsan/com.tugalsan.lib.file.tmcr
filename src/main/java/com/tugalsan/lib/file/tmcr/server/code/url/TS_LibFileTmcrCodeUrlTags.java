package com.tugalsan.lib.file.tmcr.server.code.url;

import module com.tugalsan.api.servlet.url;

public class TS_LibFileTmcrCodeUrlTags {

    public static String CODE_URL_SH_OLD() {
        return "sh?servletName(){ return ";
    }

    public static String CODE_URL_SH_NEW() {
        return TGS_SURLUtils.LOC_NAME + "?" + TGS_SURLUtils.PARAM_SERVLET_NAME() + "(){ return ";
    }
}
