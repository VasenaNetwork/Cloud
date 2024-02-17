package com.bedrockcloud.bedrockcloud.utils.files.json;

import java.io.FileReader;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.HashMap;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class json {

    public static final int MIN_RUNNING_SERVER = 0;
    public static final int MAX_RUNNING_SERVER = 1;
    public static final int MAX_PLAYER = 2;
    public static final int MAINTENANCE = 3;
    public static final int BETA = 4;
    public static final int PROXY = 5;
    public static final int TYPE = 6;
    public static final int IS_LOBBY = 7;
    public static final int IS_STATIC = 8;
    public static final int ALL = 9;

    public static Object get(final String name, final int type) throws IOException {
        final JSONParser jsonParser = new JSONParser();
        try (FileReader reader = new FileReader("./templates/config.json")) {
            final Object obj = jsonParser.parse(reader);
            final JSONArray template = (JSONArray) obj;
            final Object[] returnval = {null};
            final HashMap<String, Object> stats = new HashMap<>();
            for (final Object tmp : template) {
                final JSONObject tempobj = (JSONObject) tmp;
                final JSONObject directtemp = (JSONObject) tempobj.get(name);
                if (directtemp != null) {
                    if (type != ALL) {
                        switch (type) {
                            case MIN_RUNNING_SERVER -> returnval[0] = directtemp.get("minRunningServer");
                            case MAX_RUNNING_SERVER -> returnval[0] = directtemp.get("maxRunningServer");
                            case MAX_PLAYER -> returnval[0] = directtemp.get("maxPlayer");
                            case MAINTENANCE -> returnval[0] = directtemp.get("maintenance");
                            case BETA -> returnval[0] = directtemp.get("beta");
                            case PROXY -> returnval[0] = directtemp.get("proxy");
                            case TYPE -> returnval[0] = directtemp.get("type");
                            case IS_LOBBY -> returnval[0] = directtemp.get("isLobby");
                            case IS_STATIC -> returnval[0] = directtemp.get("isStatic");
                            default -> {}
                        }
                    } else {
                        stats.put("minRunningServer", directtemp.get("minRunningServer"));
                        stats.put("maxRunningServer", directtemp.get("maxRunningServer"));
                        stats.put("maxPlayer", directtemp.get("maxPlayer"));
                        stats.put("maintenance", directtemp.get("maintenance"));
                        stats.put("beta", directtemp.get("beta"));
                        stats.put("proxy", directtemp.get("proxy"));
                        stats.put("type", directtemp.get("type"));
                        stats.put("isLobby", directtemp.get("isLobby"));
                        stats.put("isStatic", directtemp.get("isStatic"));
                    }
                }
            }
            if (stats.isEmpty()) {
                return returnval[0];
            }
            return stats;
        } catch (FileNotFoundException | ParseException ex) {
            ex.printStackTrace();
            return false;
        }
    }
}
