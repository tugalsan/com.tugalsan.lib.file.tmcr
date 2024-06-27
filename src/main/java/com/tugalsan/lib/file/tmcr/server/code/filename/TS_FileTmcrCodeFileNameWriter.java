package com.tugalsan.lib.file.tmcr.server.code.filename;

import com.tugalsan.api.function.client.TGS_Func_In1;
import com.tugalsan.api.list.client.*;

import java.util.*;

public class TS_FileTmcrCodeFileNameWriter {

    public static List<String> buildFileNameWithAddTextCommands(TGS_Func_In1<List> commands) {
        List<String> listOfAddTextCommands = TGS_ListUtils.of();
        commands.run(listOfAddTextCommands);
        List<String> allCommands = TGS_ListUtils.of(listOfAddTextCommands);
        if (allCommands.isEmpty()) {
            return allCommands;
        }
        allCommands.add(0, TS_FileTmcrCodeFileNameTags.CODE_FILENAME_START());
        allCommands.add(TS_FileTmcrCodeFileNameTags.CODE_FILENAME_END());
        return allCommands;
    }
}
