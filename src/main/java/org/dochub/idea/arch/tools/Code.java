package org.dochub.idea.arch.tools;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileChooser.FileChooserFactory;
import com.intellij.openapi.fileChooser.FileSaverDescriptor;
import com.intellij.openapi.fileChooser.FileSaverDialog;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileWrapper;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class Code {
    static public void pushFile(String path, String content) {
        Timer timer = new Timer("Push file");
        timer.schedule(new TimerTask() {
            public void run() {
                ApplicationManager.getApplication().invokeLater(() -> {
                        byte[] payload = null;
                        if (content.startsWith("data:")) {
                            String[] parts = content.split(",");
                            payload = Base64.getDecoder().decode(parts[1]);
                        } else {
                            payload = content.getBytes(StandardCharsets.UTF_8);
                        }

                        try{
                            Files.write(Path.of(path), payload);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            }
        }, 100L);

    }
}
