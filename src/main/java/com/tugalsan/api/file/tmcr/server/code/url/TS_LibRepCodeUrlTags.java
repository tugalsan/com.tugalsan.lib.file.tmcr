package com.tugalsan.api.file.tmcr.server.code.url;

import com.tugalsan.api.servlet.url.client.TGS_SURLUtils;

public class TS_LibRepCodeUrlTags {

    public static String CODE_URL_SH_OLD() {
        return "sh?servletName(){ return ";
    }

    public static String CODE_URL_SH_NEW() {
        return TGS_SURLUtils.LOC_NAME + "?" + TGS_SURLUtils.PARAM_SERVLET_NAME() + "(){ return ";
    }
}
