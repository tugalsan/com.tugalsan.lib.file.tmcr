package com.tugalsan.lib.file.tmcr.server.code.image;

public class TS_FileTmcrCodeImageWriter {

    public static String INSERT_IMAGE(String filePath) {
        return "INSERT_IMAGE NULL NULL RESPECT CENTER NULL " + filePath + " 0";
    }

    public static String INSERT_IMAGE_FROMSQL(String tablename, String id_nullForSelectedID) {
        return "INSERT_IMAGE_FROMSQL NULL NULL RESPECT LEFT NULL " + tablename + ".STRFILEJPG_ID " + (id_nullForSelectedID == null ? "SELECTED_ID" : id_nullForSelectedID) + " 0 CREATE";
    }
    
    //INSERT_IMAGE_FROMSQL NULL NULL RESPECT LEFT NULL plsurun.STRFILEJPG_ID MAPGET.0 0 CREATE
}
