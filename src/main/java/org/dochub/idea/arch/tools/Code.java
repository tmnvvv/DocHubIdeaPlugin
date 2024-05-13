package org.dochub.idea.arch.tools;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.*;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class Code {
    static public void pushFile(Project project, String path, String content) {
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
                            LocalFileSystem.getInstance().refreshAndFindFileByIoFile(new File(path));
                            VirtualFileUtil.refreshAndFindVirtualFile(Path.of(path));
                            VirtualFileManager.getInstance().asyncRefresh(new Runnable() {
                                @Override
                                public void run() {
                                    String newFilePath = "path/to/file";
                                    VirtualFile newFile = LocalFileSystem.getInstance().findFileByPath(newFilePath);
                                    if (newFile == null) {
                                        throw new RuntimeException("Could not find newly created file!");
                                    } else {
                                        // Open the file in the editor.
                                        new OpenFileDescriptor(project, newFile).navigate(false);
                                    }
                                }
                            });
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            }
        }, 100L);

    }
}
