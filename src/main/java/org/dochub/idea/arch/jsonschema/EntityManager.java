package org.dochub.idea.arch.jsonschema;

import com.intellij.openapi.application.*;
import com.intellij.openapi.project.*;
import com.intellij.openapi.util.io.*;
import com.intellij.openapi.vfs.*;
import com.intellij.psi.*;
import com.intellij.util.*;

import java.io.*;
import java.util.*;

public class EntityManager {
    private static Map<String, VirtualFile> schemas = new HashMap<>();

    // Применяет JSONSchema для проекта
    public static VirtualFile applySchema(Project project, String schema) {

        String projectHash = project.getLocationHash();

        VirtualFile currentSchema = schemas.get(projectHash);

        if (currentSchema != null) {
            (new File(currentSchema.getPath())).delete();
        }

        try {

            File file = File.createTempFile("EntityDocHubJSONSchema", ".json");

            FileUtil.ensureExists(file);

            FileUtil.writeToFile(file, String.valueOf(schema));

            LocalFileSystem fs = LocalFileSystem.getInstance();
            currentSchema = fs.findFileByPath(file.getAbsolutePath());

            schemas.put(projectHash, currentSchema);

            Runnable dropCaches = () -> {
                for (Project pjt : ProjectManager.getInstance().getOpenProjects()) {
                    PsiManager.getInstance(pjt).dropPsiCaches();
                    PsiManager.getInstance(pjt).dropResolveCaches();
                }
            };

            ModalityUiUtil.invokeLaterIfNeeded(ModalityState.defaultModalityState(), dropCaches);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currentSchema;
    }

    // Возвращает JSONSchema для проекта
    public static VirtualFile getSchema(Project project) {
        return schemas.get(project.getLocationHash());
    }
}
