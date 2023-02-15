package org.dochub.idea.arch.tools;

import com.intellij.openapi.application.*;
import com.intellij.openapi.fileChooser.*;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFileWrapper;
import org.springframework.beans.factory.annotation.*;

import java.io.*;
import java.util.Timer;
import java.util.TimerTask;

public class Download {
    static public void download(String content, String title, String description, String extension) {
        Timer timer = new Timer("Download diagram");
        timer.schedule(new TimerTask() {
            public void run() {
                ApplicationManager.getApplication().invokeLater(() -> {

                        FileSaverDescriptor descriptor = new FileSaverDescriptor(title, description, new String[]{extension});
                        FileSaverDialog dialog = FileChooserFactory.getInstance().createSaveFileDialog(descriptor, (Project) null);
                        VirtualFileWrapper vf = dialog.save((VirtualFile) null, "diagram");

                        if (vf == null) {
                            return;
                        }
                        File file = vf.getFile();
                        try {
                            if (content.substring(0, 5).equals("data:")) {
                                byte[] imageByteArray = java.util.Base64.getMimeDecoder().decode(content.split(",")[1]);
                                FileOutputStream stream = new FileOutputStream(file);
                                stream.write(imageByteArray);
                                stream.close();
                            } else {
                                BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
                                writer.append(content);
                                writer.close();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    });
            }
        }, 100L);

    }
}
