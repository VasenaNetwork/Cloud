package com.bedrockcloud.bedrockcloud.api;

import java.util.Objects;
import java.io.IOException;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.SoftwareManager;
import com.bedrockcloud.bedrockcloud.utils.console.Loggable;
import org.jetbrains.annotations.ApiStatus;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.File;

public class GroupAPI implements Loggable {
    public static boolean isGroup(final String group) {
        final File theDir = new File("./templates/" + group);
        return theDir.exists();
    }

    @ApiStatus.Internal
    public static boolean deleteGroup(final String name) {
        if (isGroup(name)) {
            if (!BedrockCloud.getTemplateProvider().isTemplateRunning(BedrockCloud.getTemplateProvider().getTemplate(name))) {

                JSONParser parser = new JSONParser();
                JSONArray templateArray = new JSONArray();

                try {
                    FileReader reader = new FileReader("./templates/config.json");
                    templateArray = (JSONArray) parser.parse(reader);
                    reader.close();
                } catch (IOException | ParseException ignored) {}

                JSONArray updatedTemplateArray = new JSONArray();
                for (Object template : templateArray) {
                    if (template instanceof JSONObject tempObj) {
                        if (!tempObj.containsKey(name)) {
                            updatedTemplateArray.add(tempObj);
                        }
                    }
                }

                try {
                    FileWriter writer = new FileWriter("./templates/config.json");
                    writer.write(updatedTemplateArray.toJSONString());
                    writer.flush();
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BedrockCloud.getTemplateProvider().removeTemplate(name);

                final File theDir = new File("./templates/" + name);
                return theDir.delete();
            } else {
                BedrockCloud.getLogger().warning("§cThe template §e" + name + " §cmust be stopped before deleting§7.");
                return false;
            }
        } else {
            BedrockCloud.getLogger().warning("§cThe template §e" + name + " §cdon't exists§7.");
            return false;
        }
    }

    @ApiStatus.Internal
    public static void createGroup(final String name, final int type) {
        if (!isGroup(name)) {
            try {
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
                        if (type == SoftwareManager.SOFTWARE_SERVER) {
                            createConfigEntry(name, type);
                        }
                    }
                }

                if (type == SoftwareManager.SOFTWARE_PROXY) {
                    createConfigEntry(name, type);
                }

                BedrockCloud.getLogger().debug("The Group " + name + " has been successfully created!");
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }

    public static void createNewGroup(final String name, final int type) {
        if (!isGroup(name)) {
            try {
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
                        if (type == SoftwareManager.SOFTWARE_SERVER) {
                            createConfigEntry(name, type);
                        }
                    }
                }

                if (type == SoftwareManager.SOFTWARE_PROXY) {
                    createConfigEntry(name, type);
                }

                BedrockCloud.getLogger().debug("The Group " + name + " has been successfully created!");
                BedrockCloud.getTemplateProvider().loadTemplate(name);
            } catch (IOException e) {
                BedrockCloud.getLogger().exception(e);
            }
        }
    }

    @ApiStatus.Internal
    private static void createConfigEntry(String name, int type) throws IOException {
        final String configFilePath = "./templates/config.json";
        final JSONParser jsonParser = new JSONParser();
        JSONArray template;

        try (final FileReader reader = new FileReader(configFilePath)) {
            final Object obj = jsonParser.parse(reader);
            template = obj instanceof JSONArray ? (JSONArray) obj : new JSONArray();
        } catch (ParseException | IOException ignored) {
            template = new JSONArray();
        }

        JSONObject object = new JSONObject();
        object.put("minRunningServer", 1);
        object.put("maxRunningServer", 10);
        object.put("maxPlayer", 100);
        object.put("maintenance", false);
        object.put("isStatic", false);
        object.put("isLobby", false);
        object.put("type", type);
        object.put("beta", false);

        if (type == SoftwareManager.SOFTWARE_SERVER) {
            object.put("proxy", "Proxy-Master");
        }

        JSONObject temp = new JSONObject();
        temp.put(name, object);
        template.add(temp);

        try (final FileWriter fileWriter = new FileWriter(configFilePath)) {
            fileWriter.write(template.toJSONString());
            fileWriter.flush();
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
}
