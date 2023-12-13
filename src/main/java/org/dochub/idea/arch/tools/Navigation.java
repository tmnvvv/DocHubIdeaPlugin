package org.dochub.idea.arch.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.intellij.codeInsight.TargetElementUtil;
import com.intellij.find.findUsages.FindUsagesHandlerFactory;
import com.intellij.ide.DataManager;
import com.intellij.ide.actions.SearchEverywhereAction;
import com.intellij.ide.actions.searcheverywhere.SearchEverywhereManager;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionPlaces;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.IdeActions;
import com.intellij.openapi.actionSystem.Presentation;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.fileEditor.OpenFileDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.pom.Navigatable;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.psi.util.PsiUtilCore;
import com.intellij.util.messages.MessageBusConnection;
import org.dochub.idea.arch.indexing.CacheBuilder;
import org.dochub.idea.arch.references.providers.RefBaseID;
import org.dochub.idea.arch.utils.VirtualFileSystemUtils;

import java.awt.event.InputEvent;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.intellij.openapi.ui.playback.commands.ActionCommand.getInputEvent;
import static org.dochub.idea.arch.tools.Consts.ROOT_SOURCE_PATH;
import static org.dochub.idea.arch.tools.Consts.ROOT_SOURCE_URI;

public class Navigation {
    private Project project;
    private MessageBusConnection connBus;
    private FindUsagesHandlerFactory findUsagesHandlerFactory;


    public Navigation(Project project) {

        this.project = project;
    }

    private static String entityToSection(String entity) {
        String section = null;
        switch (entity) {
            case "component":
                section = "components";
                break;
            case "document":
                section = "docs";
                break;
            case "context":
                section = "contexts";
                break;
            case "aspect":
                section = "aspects";
                break;
            default:
                section = entity;
        }
        return section;
    }

    public VirtualFile getVFile(String uri) {
        String source;
        if (uri.equals(ROOT_SOURCE_URI)) {
            source = CacheBuilder.getRootManifestName(project);
        } else if (uri.startsWith(ROOT_SOURCE_PATH)) {
            source = uri.substring(ROOT_SOURCE_PATH.length());
        } else if (uri.startsWith(project.getBasePath())) {
            source = uri.substring(project.getBasePath().length());
        } else
            source = uri;
        return VirtualFileSystemUtils.findFile(source, project);
    }

    public void gotoPsiElement(PsiElement element) {
        AnAction action = ActionManager.getInstance().getAction(IdeActions.ACTION_COPY_REFERENCE);
        AnActionEvent event = new AnActionEvent(null, DataManager.getInstance().getDataContext(),
                ActionPlaces.UNKNOWN, new Presentation(),
                ActionManager.getInstance(), 0);
        action.actionPerformed(event);

        PsiElement navElement = element.getNavigationElement();
        navElement = TargetElementUtil.getInstance().getGotoDeclarationTarget(element, navElement);
        if (navElement instanceof Navigatable) {
            if (((Navigatable) navElement).canNavigate()) {
                ((Navigatable) navElement).navigate(true);
            }
        } else if (navElement != null) {
            int navOffset = navElement.getTextOffset();
            VirtualFile virtualFile = PsiUtilCore.getVirtualFile(navElement);
            if (virtualFile != null) {
                new OpenFileDescriptor(project, virtualFile, navOffset).navigate(true);
            }
        }
    }

    private void gotoByID(String uri, String entity, String id) {
        VirtualFile vFile = getVFile(uri);
        if (vFile != null) {
            String section = entityToSection(entity);
            PsiFile targetFile = PsiManager.getInstance(project).findFile(vFile);
            PsiTreeUtil.processElements(targetFile, element -> {
                if (RefBaseID.makeSourcePattern(section, id).accepts(element)) {
                    gotoPsiElement(element);
                    return false;
                }
                return true;
            });
        }
    }

    private void gotoBySource(String uri) {
        VirtualFile vFile = getVFile(uri);
        if (vFile != null)
            gotoPsiElement(PsiManager.getInstance(project).findFile(vFile));
    }

    private void gotoByPosition(String uri, int start) {
        VirtualFile vFile = getVFile(uri);
        if (vFile != null) {
            PsiFile targetFile = PsiManager.getInstance(project).findFile(vFile);
            gotoPsiElement(targetFile.findElementAt(start));
        }
    }


    public void go(String source, String entity, String id) {
        Application app = ApplicationManager.getApplication();
        app.invokeLater(() -> gotoByID(source, entity, id));
    }

    public void go(String source, int pos) {
        Application app = ApplicationManager.getApplication();
        app.invokeLater(new Runnable() {
            @Override
            public void run() {
                gotoByPosition(source, pos);
            }
        }, ModalityState.NON_MODAL);
    }

    public void go(String source) {
        Application app = ApplicationManager.getApplication();
        app.invokeLater(new Runnable() {
            @Override
            public void run() {
                gotoBySource(source);
            }
        }, ModalityState.NON_MODAL);
    }

    public void go(JsonNode location) {
        JsonNode jsonID = location.get("id");
        JsonNode jsonEntity = location.get("entity");
        JsonNode jsonSource = location.get("source");
        JsonNode jsonRange = location.get("range");
        if ((jsonID != null) && (jsonEntity != null) && (jsonSource != null)) {
            String id = jsonID.asText();
            String entity = jsonEntity.asText();
            String source = jsonSource.asText();
            if (source.equals("null")) {
                Map<String, CacheBuilder.SectionData> cache = CacheBuilder.getProjectCache(project);
                String section = entityToSection(entity);
                if (section == null) return;
                CacheBuilder.SectionData components = cache == null ? null : cache.get(section);
                if (components == null) return;
                ;
                List<VirtualFile> files = components.ids.get(id);

                Optional.ofNullable(files).orElseThrow(() -> new RuntimeException("Wrong: the files is null"));

                if (files.size() != 1) {
                    Application app = ApplicationManager.getApplication();
                    app.invokeLater(() -> {
                        ActionManager am = ActionManager.getInstance();
                        SearchEverywhereAction searchEverywhere = (SearchEverywhereAction) am.getAction("SearchEverywhere");
                        InputEvent inputEvent = getInputEvent("SearchEverywhere");
                        am.tryToExecute(searchEverywhere, inputEvent, null, null, true).doWhenDone(() -> {
                            SearchEverywhereManager searchEverywhereManager = SearchEverywhereManager.getInstance(project);
                            searchEverywhereManager.getCurrentlyShownUI().getSearchField().setText(id);
                        });
                    }, ModalityState.nonModal());

                } else {
                    source = files.get(0).getPath();
                }
            }
            go(source, entity, id);
        } else if ((jsonRange != null) && (jsonSource != null)) {
            go(jsonSource.asText(), jsonRange.get("start").asInt());
        } else if (jsonSource != null) go(jsonSource.asText());
    }
}
