package com.tugalsan.api.file.tmcr.server.code.label;

import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_GOTO_LABEL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_SET_LABEL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_TOKEN_EQUALS;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_TOKEN_IF_VAL;
import static com.tugalsan.api.file.tmcr.server.code.label.TS_FileTmcrCodeLabelTags.CODE_TOKEN_NOT_EQUALS;

public class TS_FileTmcrCodeLabelWriter {

    public static String SET_LABEL(String label) {
        return CODE_SET_LABEL() + " " + label + "\n";
    }

    public static String GOTO_LABEL(String label) {
        return CODE_GOTO_LABEL() + " " + label + "\n";
    }

    public static String GOTO_LABEL_IF_VAL_EQUALS(String label, String VAR, String ID) {
        return CODE_GOTO_LABEL() + " " + label + " " + CODE_TOKEN_IF_VAL() + " " + CODE_TOKEN_EQUALS() + " " + VAR + " " + ID + "\n";
    }

    public static String GOTO_LABEL_IF_VAL_NOT_EQUALS(String label, String VAR, String ID) {
        return CODE_GOTO_LABEL() + " " + label + " " + CODE_TOKEN_IF_VAL() + " " + CODE_TOKEN_NOT_EQUALS() + " " + VAR + " " + ID + "\n";
    }
}
