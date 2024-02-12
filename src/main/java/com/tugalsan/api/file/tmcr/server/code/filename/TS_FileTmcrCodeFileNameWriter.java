package com.tugalsan.api.file.tmcr.server.code.filename;

import com.tugalsan.api.list.client.*;
import com.tugalsan.api.runnable.client.*;
import java.util.*;

public class TS_FileTmcrCodeFileNameWriter {

    public static List<String> buildFileNameWithAddTextCommands(TGS_RunnableType1<List> commands) {
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
