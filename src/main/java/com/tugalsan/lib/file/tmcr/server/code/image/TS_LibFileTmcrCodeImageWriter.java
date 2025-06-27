package com.tugalsan.lib.file.tmcr.server.code.image;

@Deprecated //USE TS_LibFileTmcrCodeImageBuilder
public class TS_LibFileTmcrCodeImageWriter {
    
    private TS_LibFileTmcrCodeImageWriter(){
        
    }

    public static String INSERT_IMAGE(String filePath) {
        return "INSERT_IMAGE NULL NULL RESPECT CENTER NULL " + filePath + " 0";
    }

    public static String INSERT_IMAGE_FROMQR(String QR64) {
        return "INSERT_IMAGE NULL NULL RESPECT CENTER NULL " + QR64 + " 0";
    }

    public static String INSERT_IMAGE_FROMSQL(String tablename, String id_nullForSelectedID) {
        return "INSERT_IMAGE_FROMSQL NULL NULL RESPECT LEFT NULL " + tablename + ".STRFILEJPG_ID " + (id_nullForSelectedID == null ? "SELECTED_ID" : id_nullForSelectedID) + " 0 CREATE";
    }

}
