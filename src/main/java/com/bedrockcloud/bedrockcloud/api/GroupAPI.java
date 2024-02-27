package com.bedrockcloud.bedrockcloud.api;

import com.bedrockcloud.bedrockcloud.Cloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.templates.Template;
import com.bedrockcloud.bedrockcloud.templates.TemplateProvider;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.jetbrains.annotations.ApiStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class GroupAPI implements Loggable {

    public static boolean isGroup(final String group) {
        final File theDir = new File("./templates/" + group);
        return theDir.exists();
    }

    @ApiStatus.Internal
    public static boolean deleteGroup(final String name) {
        if (!isGroup(name)) {
            Cloud.getLogger().warning("§cThe template §e" + name + " §cdon't exists§7.");
            return false;
        }

        TemplateProvider templateProvider = Cloud.getTemplateProvider();
        Template template = templateProvider.getTemplate(name);
        if (template == null) {
            Cloud.getLogger().warning("§cThe template §e" + name + " §cdon't exists§7.");
            return false;
        }

        if (templateProvider.isTemplateRunning(template)) {
            Cloud.getLogger().warning("§cThe template §e" + name + " §cmust be stopped before deleting§7.");
            return false;
        }

        JSONArray templateArray = readTemplateConfig();

        JSONArray updatedTemplateArray = new JSONArray();
        for (Object templateObj : templateArray) {
            if (templateObj instanceof JSONObject tempObj) {
                if (!tempObj.containsKey(name)) {
                    updatedTemplateArray.add(tempObj);
                }
            }
        }

        try (FileWriter fileWriter = new FileWriter("./templates/config.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(updatedTemplateArray);
            fileWriter.write(jsonString);
        } catch (IOException e) {
            Cloud.getLogger().exception(e);
            return false;
        }

        templateProvider.removeTemplate(name);

        File theDir = new File("./templates/" + name);
        return theDir.delete();
    }

    @ApiStatus.Internal
    public static void createGroup(final String name, final int type, final boolean lobby) {
        if (isGroup(name)) {
            Cloud.getLogger().warning("§cThe template §e" + name + " §calready exists§7.");
            return;
        }

        try {
            createTemplateDirectories(name, type);
            createConfigEntry(name, type, lobby);

            Cloud.getLogger().debug("The Group " + name + " has been successfully created!");
        } catch (IOException e) {
            Cloud.getLogger().exception(e);
        }
    }

    public static void createNewGroup(final String name, final int type, final boolean lobby) {
        if (isGroup(name)) {
            Cloud.getLogger().warning("§cThe template §e" + name + " §calready exists§7.");
            return;
        }

        try {
            createTemplateDirectories(name, type);
            createConfigEntry(name, type, lobby);

            Cloud.getLogger().debug("The Group " + name + " has been successfully created!");
            Cloud.getTemplateProvider().loadTemplate(name);
        } catch (IOException e) {
            Cloud.getLogger().exception(e);
        }
    }

    @ApiStatus.Internal
    private static void createTemplateDirectories(String name, int type) throws IOException {
        final ArrayList<String> directories = new ArrayList<>();
        directories.add("./templates/" + name);

        if (type == SoftwareManager.SOFTWARE_SERVER) {
            directories.add(directories.get(0) + "/crashdumps");
            directories.add(directories.get(0) + "/plugins");
            directories.add(directories.get(0) + "/plugin_data");
            directories.add(directories.get(0) + "/worlds");
        }

        for (final String directory : directories) {
            final File theDir = new File(directory);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
        }
    }

    @ApiStatus.Internal
    private static void createConfigEntry(String name, int type, boolean lobby) throws IOException {
        JSONArray templateArray = readTemplateConfig();

        JSONObject object = new JSONObject();
        object.put("minRunningServer", 1);
        object.put("maxRunningServer", 1);
        object.put("maxPlayer", 20);
        object.put("maintenance", true);
        object.put("isStatic", false);
        object.put("isLobby", lobby);
        object.put("type", type);
        object.put("beta", false);

        if (type == SoftwareManager.SOFTWARE_SERVER) {
            object.put("proxy", "Proxy-Master");
        }

        JSONObject temp = new JSONObject();
        temp.put(name, object);
        templateArray.add(temp);

        try (FileWriter fileWriter = new FileWriter("./templates/config.json")) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(templateArray);
            fileWriter.write(jsonString);
        }
    }

    public static ArrayList<String> getGroups() {
        final File file = new File("./templates/");
        final ArrayList<String> names = new ArrayList<>();
        for (final File own : Objects.requireNonNull(file.listFiles())) {
            if (!own.getName().equals("config.json")) {
                names.add(own.getName());
            }
        }
        return names;
    }

    private static JSONArray readTemplateConfig() {
        final String configFilePath = "./templates/config.json";
        final JSONParser jsonParser = new JSONParser();
        JSONArray template;

        try (final FileReader reader = new FileReader(configFilePath)) {
            final Object obj = jsonParser.parse(reader);
            template = obj instanceof JSONArray ? (JSONArray) obj : new JSONArray();
        } catch (ParseException | IOException ignored) {
            template = new JSONArray();
        }

        return template;
    }
}