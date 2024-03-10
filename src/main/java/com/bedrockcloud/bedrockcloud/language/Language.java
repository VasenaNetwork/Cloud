package com.bedrockcloud.bedrockcloud.language;

import com.bedrockcloud.bedrockcloud.Cloud;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

public enum Language {
    GERMAN("German", "de_DE.yml", new String[]{"de_DE", "ger", "Deutsch"}),
    ENGLISH("English", "en_US.yml", new String[]{"en_US", "en", "Englisch"});

    private static final String FALLBACK = "en";

    private final String name;
    private final String filePath;
    private final String[] aliases;
    private final Map<String, String> messages;

    Language(String name, String filePath, String[] aliases) {
        this.name = name;
        this.filePath = filePath;
        this.aliases = aliases;
        this.messages = new HashMap<>();
        loadMessages();
    }

    private void loadMessages() {
        File file = new File(filePath);
        if (file.exists()) {
            try (InputStream inputStream = new FileInputStream(file)) {
                Yaml yaml = new Yaml();
                Map<String, Object> yamlData = yaml.load(inputStream);

                if (yamlData != null) {
                    yamlData.forEach((key, value) -> {
                        if (value != null) {
                            messages.put(key, value.toString());
                        }
                    });
                }

            } catch (IOException e) {
                Cloud.getLogger().exception(e);
            }
        } else {
            Cloud.getLogger().info("Language file not found: " + filePath);
        }
    }

    public String translate(String key, Object... params) {
        String message = messages.getOrDefault(key, key);
        for (int i = 0; i < params.length; i++) {
            String placeholder = "%" + i + "%";
            if (message.contains(placeholder)) {
                message = message.replace(placeholder, String.valueOf(params[i]));
            }
        }
        message = message.replace("{PREFIX}", messages.getOrDefault("inGame.prefix", ""));
        return message;
    }

    public static Language current() {
        String languageName = DefaultConfig.getInstance().getLanguage();
        return getLanguage(languageName != null ? languageName : FALLBACK);
    }

    public static Language fallback() {
        return getLanguage(FALLBACK);
    }

    public static Language getLanguage(String name) {
        for (Language language : Language.values()) {
            if (language.name.equals(name) || containsAlias(language.aliases, name)) {
                return language;
            }
        }
        return null;
    }

    private static boolean containsAlias(String[] aliases, String name) {
        for (String alias : aliases) {
            if (alias.equals(name)) {
                return true;
            }
        }
        return false;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public Map<String, String> getMessages() {
        return messages;
    }
}