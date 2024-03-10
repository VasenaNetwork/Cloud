package com.bedrockcloud.bedrockcloud.language;

import java.util.HashMap;
import java.util.Map;

public final class DefaultMessages {

    public static final Map<String, String> MESSAGES = new HashMap<>();
    public static final Map<String, String> MESSAGES_DE = new HashMap<>();

    static {
        //English messages
        MESSAGES.put("raw.yes", "Yes");
        MESSAGES.put("raw.no", "No");
        MESSAGES.put("raw.author", "Author(s)");
        MESSAGES.put("raw.description", "Description");
        MESSAGES.put("raw.enabled", "Enabled");
        MESSAGES.put("template.loading", "Loading all templates...");
        MESSAGES.put("template.loaded.none", "No templates were loaded!");
        MESSAGES.put("template.loaded", "Successfully loaded §e%0% templates(s)§r!");
        MESSAGES.put("template.create", "§aCreating §rtemplate §e%0%§r...");
        MESSAGES.put("server.start", "§bTry to §astart §bthe service §3%service§b...");
        MESSAGES.put("server.startedSuccessfully", "§bThe service §3%service §bwas §astarted §bsuccessfully.");
        MESSAGES.put("server.startFailed", "§bFailed to start the service §3%service§b...");
        MESSAGES.put("server.stop", "§bTry to §cstop §bthe service §3%service§b...");
        MESSAGES.put("server.stoppedSuccessfully", "§bThe service §3%service §bwas §cstopped §bsuccessfully.");

        //German messages
        MESSAGES_DE.put("raw.yes", "Ja");
        MESSAGES_DE.put("raw.no", "Nein");
        MESSAGES_DE.put("raw.author", "Autor(en)");
        MESSAGES_DE.put("raw.description", "Beschreibung");
        MESSAGES_DE.put("raw.enabled", "Aktiviert");
        MESSAGES_DE.put("template.loading", "Lade alle Templates...");
        MESSAGES_DE.put("template.loaded.none", "Es wurden keine Templates geladen!");
        MESSAGES_DE.put("template.loaded", "Es wurden erfolgreich §e%0% Templates(s) §rgeladen!");
        MESSAGES_DE.put("template.create", "§aErstelle §rdas Template §e%0%§r...");
        MESSAGES_DE.put("server.start", "§bVersuche den service §3%service §azu starten§b...");
        MESSAGES_DE.put("server.startedSuccessfully", "§bDer service §3%service §bwurde erfolgreich §agestartet§b.");
        MESSAGES_DE.put("server.startFailed", "§bFehler beim starten des service §3%service§b...");
        MESSAGES_DE.put("server.stop", "§bVersuche §bden service §3%service §czu stoppen§b...");
        MESSAGES_DE.put("server.stoppedSuccessfully", "§bDer service §3%service §bwurde erfolgreich §cgestoppt§b.");
    }
}