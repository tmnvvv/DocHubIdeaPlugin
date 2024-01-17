package org.dochub.idea.arch.indexing;

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
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Queue;

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
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static class SectionData {
        public ArrayList<String> locations = new ArrayList<>();
        public Map<String, ArrayList> ids = new HashMap();
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
                ArrayList sources = sectionData.ids.get(id);
                if (sources == null) {
                    sources = new ArrayList<String>();
                    sectionData.ids.put(id, sources);
                }
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

    private static Optional<PsiFile> getPsiFile(VirtualFile vFile, Project project) {

        return Optional.ofNullable(vFile)
                .map(e -> PsiManager
                        .getInstance(project)
                        .findFile(e));
    }

    private static Map<Integer, DocHubIndexData> getFileData(PsiFile psi, Project project, Map<String, SectionData> context) {

        return FileBasedIndex
                .getInstance()
                .getFileData(DocHubIndex.INDEX_ID, psi.getVirtualFile(), project);
    }


    private static void parseYamlManifest(Project project, String path, Map<String, SectionData> context) {

        VirtualFile vFile = VirtualFileSystemUtils.findFile(path, project);

        Queue<String> queue = new LinkedList<>();
        List<String> done = new ArrayList<>();

        while (vFile != null) {

            Optional<PsiFile> psiFile = getPsiFile(vFile, project);

            if (psiFile.isPresent()) {

                Map<Integer, DocHubIndexData> index = getFileData(psiFile.get(), project, context);

                for (Integer key : index.keySet()) {
                    DocHubIndexData data = index.get(key);

                    DocHubIndexData.Section imports = data.get("imports");

                    if (imports != null) {

                        VirtualFile finalVFile = vFile;
                        List<String> list = imports.imports
                                .stream()
                                .map(anImport -> (finalVFile.getParent().getPath() + "/" + anImport).substring(Objects.requireNonNull(project.getBasePath()).length()))
                                .toList();

                        list.forEach(queue::offer);
                    }

                    manifestMerge(context, data, vFile);
                }

                String importPath = queue.poll();

                while (done.contains(importPath)) {
                    importPath = queue.poll();
                }
                done.add(importPath);
                vFile = importPath != null ? VirtualFileSystemUtils.findFile(importPath, project) : null;
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

    private static Key cacheProjectKey = Key.create("dochub-global");

    private static class GlobalCacheProvider implements CachedValueProvider {
        private Project project;

        public GlobalCacheProvider(Project project) {
            this.project = project;
        }

        @Override
        public @Nullable Result compute() {
            Map<String, SectionData> manifest = new HashMap<>();
            String rootManifest = getRootManifestName(project);

            if (rootManifest != null)
                parseYamlManifest(project, rootManifest, manifest);

            return Result.create(
                    manifest,
                    PsiModificationTracker.MODIFICATION_COUNT,
                    ProjectRootManager.getInstance(project));
        }
    }

    private static Map<Project, GlobalCacheProvider> globalCacheProviders = new HashMap<>();

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
