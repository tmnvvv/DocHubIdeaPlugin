package org.dochub.idea.arch.tools;

import com.intellij.openapi.application.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;

import java.io.*;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Timer;
import java.util.TimerTask;

public class Download {
    static public void download(String content, String title, String description) {
        Timer timer = new Timer("Download diagram");
        timer.schedule(new TimerTask() {
            public void run() {
                ApplicationManager.getApplication().invokeLater(() -> {
                        byte[] payload = null;
                        String[] exts = new String[]{};
                        if (content.startsWith("data:")) {
                            String[] parts = content.split(",");
                            payload = Base64.getDecoder().decode(parts[1]);
                            String header = parts[0];
                            String mimeType = header.split(":")[1].split(";")[0];
                            String ext = MimeTypes.getDefaultExt(mimeType);
                            if (ext != null) exts = new String[]{ext};
                            /*
                            switch (mimeType) {
                                case "image/jpeg":
                                    exts = new String[]{"jpeg"};
                                    break;
                                case "image/svg+xml":
                                    exts = new String[]{"svg"};
                                    break;
                                case "image/png":
                                    exts = new String[]{"png"};
                                    break;
                            }
                            */
                        } else payload = content.getBytes();

                        FileSaverDescriptor descriptor = new FileSaverDescriptor(title, description, exts);
                        FileSaverDialog dialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, (Project) null);
                        VirtualFileWrapper vf = dialog.save((VirtualFile) null, "dh_" + System.currentTimeMillis());

                        if (vf == null) {
                            return;
                        }
                        File file = vf.getFile();
                        try {
                            Files.write(file.toPath(), payload);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            }
        }, 100L);

    }
}
