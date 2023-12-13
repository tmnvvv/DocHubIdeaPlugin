package org.dochub.idea.arch.formatter.settings;

import com.intellij.application.options.IndentOptionsEditor;
import com.intellij.application.options.SmartIndentOptionsEditor;
import com.intellij.lang.Language;
import com.intellij.psi.codeStyle.CommonCodeStyleSettings;
import com.intellij.psi.codeStyle.LanguageCodeStyleSettingsProvider;
import org.dochub.idea.arch.jsonata.JSONataLanguage;
import org.jetbrains.annotations.NotNull;

public class JSONataLanguageCodeStyleSettingsProvider extends LanguageCodeStyleSettingsProvider {
    private static final String DEFAULT_CODE_SAMPLE =
            "(\n" +
                    "  $config := entities.\"seaf.ta.reverse.general\".config;\n" +
                    "  [{\n" +
                    "      \"location\": $config.arch_menu,\n" +
                    "      \"link\": \"entities/seaf.ta.reverse.general/loc?id=reverse.cloud_ru.advanced&txt=Расширение IAAS\"\n" +
                    "   },\n" +
                    "   {\n" +
                    "      \"location\": $config.arch_menu & \"/Cloud.ru\",\n" +
                    "      \"link\": \"entities/seaf.ta.reverse.general/loc?id=reverse.cloud_ru&txt=Cloud.ru\"\n" +
                    "   }, \n" +
                    "   {\n" +
                    "      \"location\": $config.arch_menu & \"/Cloud.ru/Advanced\",\n" +
                    "      \"link\": \"entities/seaf.ta.reverse.general/loc?id=reverse.cloud_ru.advanced&txt=Advanced\"\n" +
                    "   }, \n" +
                    "   {\n" +
                    "      \"location\": $config.doc_menu,\n" +
                    "      \"link\": \"entities/seaf.ta.reverse.general/readme\"\n" +
                    "   },\n" +
                    "   {\n" +
                    "      \"location\": $config.doc_menu &  \"/Cloud.ru\",\n" +
                    "      \"link\": \"entities/seaf.ta.reverse.general/readme_cloud_ru\"\n" +
                    "   },\n" +
                    "   {\n" +
                    "      \"location\": $config.doc_menu &  \"/Cloud.ru/Advanced\",\n" +
                    "      \"link\": \"entities/seaf.ta.reverse.general/readme_cloud_advanced\"\n" +
                    "   }\n" +
                    "   ];\n" +
                    "  )";

    @NotNull
    @Override
    public Language getLanguage() {
        return JSONataLanguage.INSTANCE;
    }

    @NotNull
    @Override
    public String getCodeSample(@NotNull SettingsType settingsType) {
        return DEFAULT_CODE_SAMPLE;
    }

    @Override
    public IndentOptionsEditor getIndentOptionsEditor() {
        return new SmartIndentOptionsEditor();
    }

    @Override
    public CommonCodeStyleSettings getDefaultCommonSettings() {
        CommonCodeStyleSettings defaultSettings = new CommonCodeStyleSettings(getLanguage());
        CommonCodeStyleSettings.IndentOptions indentOptions = defaultSettings.initIndentOptions();
        indentOptions.INDENT_SIZE = 8;
        indentOptions.CONTINUATION_INDENT_SIZE = 8;
        indentOptions.TAB_SIZE = 8;
        indentOptions.USE_TAB_CHARACTER = true;

        defaultSettings.BLOCK_COMMENT_AT_FIRST_COLUMN = false;
        defaultSettings.LINE_COMMENT_AT_FIRST_COLUMN = false;
        return defaultSettings;
    }
}
