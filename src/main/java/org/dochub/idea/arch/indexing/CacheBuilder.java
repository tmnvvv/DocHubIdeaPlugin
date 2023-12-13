package org.dochub.idea.arch.indexing;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ProjectRootManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.CachedValueProvider;
import com.intellij.psi.util.CachedValuesManager;
import com.intellij.psi.util.PsiModificationTracker;
import com.intellij.util.indexing.FileBasedIndex;
import org.dochub.idea.arch.utils.VirtualFileSystemUtils;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheBuilder {
    private static boolean isFileExists(Project project, String filename) {
        return (new File(project.getBasePath() + "/" + filename)).exists();
    }

    private static Map<String, String> parseEnvFile(String filename) {
        Map<String, String> result = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] lineStruct = line.split("\\=");
                result.put(lineStruct[0], lineStruct.length > 1 ? lineStruct[1] : null);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class SectionData {
        public final List<String> locations = Collections.synchronizedList(new ArrayList<>());
        public final Map<String, ArrayList> ids = new ConcurrentHashMap<>();

    }

    private static void manifestMerge(Map<String, SectionData> context, DocHubIndexData data, VirtualFile source) {
        for (String sectionKey : data.keySet()) {

            if (sectionKey.equals("imports")) continue;

            // Получаем секцию
            DocHubIndexData.Section section = data.get(sectionKey);
            SectionData sectionData = context.get(sectionKey);
            if (sectionData == null) {
                sectionData = new SectionData();
                context.put(sectionKey, sectionData);
            }

            // Разбираем секцию
            // Идентификаторы
            for (int i = 0; i < section.ids.size(); i++) {
                String id = section.ids.get(i);
                ArrayList sources = sectionData.ids.computeIfAbsent(id, k -> new ArrayList<String>());
                sources.add(source);
            }
            // Локации
            for (int i = 0; i < section.locations.size(); i++) {
                String location = section.locations.get(i);
                if (!sectionData.locations.contains(location))
                    section.locations.add(location);
            }
        }
    }

    private static void parseYamlManifest(Project project, String path, Map<String, SectionData> context) {
        VirtualFile vFile = VirtualFileSystemUtils.findFile(path, project);

        if (vFile != null) {
            PsiFile targetFile = PsiManager.getInstance(project).findFile(vFile);
            if (targetFile != null) {
                Map index = FileBasedIndex.getInstance().getFileData(DocHubIndex.INDEX_ID, vFile, project);

                for (Object key : index.keySet()) {
                    DocHubIndexData data = (DocHubIndexData) index.get(key);

                    DocHubIndexData.Section imports = data.get("imports");
                    if (imports != null) {
                        for (int i = 0; i < imports.imports.size(); i++) {
                            String importPath =
                                    (vFile.getParent().getPath() + "/" + imports.imports.get(i))
                                            .substring(project.getBasePath().length());
                            parseYamlManifest(project, importPath, context);
                        }
                    }

                    manifestMerge(context, data, vFile);
                }
            }
        }
    }

    private static String getFromEnv(Project project) {
        String[] names = new String[]{".env.local", ".env"};
        for (String name : names) {
            if (isFileExists(project, name)) {
                Map<String, String> env = parseEnvFile(project.getBasePath() + "/" + name);
                return "public/" + env.get("VUE_APP_DOCHUB_ROOT_MANIFEST");
            }
            ;
        }
        return null;
    }

    public static String getRootManifestName(Project project) {
        String rootManifest = null;
        if (isFileExists(project, "dochub.yaml"))  // Если это проект DocHub
            rootManifest = "dochub.yaml";
        else
            rootManifest = getFromEnv(project);
        return rootManifest != null ? rootManifest : "dochub.yaml";
    }

    private static final Key cacheProjectKey = Key.create("dochub-global");

    private static class GlobalCacheProvider implements CachedValueProvider {
        private final Project project;

        public GlobalCacheProvider(Project project) {
            this.project = project;
        }

        @Override
        public @Nullable Result compute() {
            Map<String, SectionData> manifest = new HashMap<>();
            String rootManifest = getRootManifestName(project);

            if (!rootManifest.isEmpty()) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    parseYamlManifest(project, rootManifest, manifest);
                });
            }

            return Result.create(
                    manifest,
                    PsiModificationTracker.MODIFICATION_COUNT,
                    ProjectRootManager.getInstance(project));
        }
    }

    private final static Map<Project, GlobalCacheProvider> globalCacheProviders = new ConcurrentHashMap<>();

    public static Map<String, SectionData> getProjectCache(Project project) {
        GlobalCacheProvider globalCacheProvider = globalCacheProviders.get(project);
        if (globalCacheProvider == null) {
            globalCacheProvider = new GlobalCacheProvider(project);
            globalCacheProviders.put(project, globalCacheProvider);
        }
        CachedValuesManager cacheManager = CachedValuesManager.getManager(project);
        return (Map<String, SectionData>) cacheManager.getCachedValue(
                project,
                cacheProjectKey,
                globalCacheProvider,
                false
        );
    }
}
