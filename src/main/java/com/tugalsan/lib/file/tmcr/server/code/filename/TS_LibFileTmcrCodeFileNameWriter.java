package com.tugalsan.lib.file.tmcr.server.code.filename;

import module com.tugalsan.api.function;
import module com.tugalsan.api.list;
import java.util.*;

public class TS_LibFileTmcrCodeFileNameWriter {

    public static List<String> buildFileNameWithAddTextCommands(TGS_FuncMTU_In1<List> commands) {
        List<String> listOfAddTextCommands = TGS_ListUtils.of();
        commands.run(listOfAddTextCommands);
        List<String> allCommands = TGS_ListUtils.of(listOfAddTextCommands);
        if (allCommands.isEmpty()) {
            return allCommands;
        }
        allCommands.add(0, TS_LibFileTmcrCodeFileNameTags.CODE_FILENAME_START());
        allCommands.add(TS_LibFileTmcrCodeFileNameTags.CODE_FILENAME_END());
        return allCommands;
    }
}
