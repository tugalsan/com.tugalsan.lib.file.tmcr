package com.tugalsan.lib.file.tmcr.server.code.label;

public class TS_LibFileTmcrCodeLabelWriter {

    public static String SET_LABEL(String label) {
        return TS_LibFileTmcrCodeLabelTags.CODE_SET_LABEL() + " " + label + "\n";
    }

    public static String GOTO_LABEL(String label) {
        return TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL() + " " + label + "\n";
    }

    public static String GOTO_LABEL_IF_VAL_EQUALS(String label, String VAR, String ID) {
        return TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL() + " " + label + " " + TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_IF_VAL() + " " + TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_EQUALS() + " " + VAR + " " + ID + "\n";
    }

    public static String GOTO_LABEL_IF_VAL_NOT_EQUALS(String label, String VAR, String ID) {
        return TS_LibFileTmcrCodeLabelTags.CODE_GOTO_LABEL() + " " + label + " " + TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_IF_VAL() + " " + TS_LibFileTmcrCodeLabelTags.CODE_TOKEN_NOT_EQUALS() + " " + VAR + " " + ID + "\n";
    }
}
