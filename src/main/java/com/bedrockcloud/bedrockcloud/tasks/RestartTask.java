package com.bedrockcloud.bedrockcloud.tasks;

import com.bedrockcloud.bedrockcloud.BedrockCloud;
import com.bedrockcloud.bedrockcloud.player.CloudPlayer;
import com.bedrockcloud.bedrockcloud.network.packets.PlayerTextPacket;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.TimerTask;

public class RestartTask extends TimerTask {

    private static boolean send1 = false;
    private static boolean send2 = false;
    private static boolean send3 = false;

    @Override
    public void run() {
        if (!BedrockCloud.isRunning()) return;

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());

        String currentTime = sdf.format(timestamp);

        if (currentTime.equals("01:55:00") && !send1) {
            sendRestartMessage("§45 minutes");
            send1 = true;
        } else if (currentTime.equals("01:59:00") && !send2) {
            sendRestartMessage("§41 minute");
            send2 = true;
        } else if ((currentTime.equals("02:00:00") || currentTime.equals("02:00:01") || currentTime.equals("02:00:02")) && !send3) {
            sendRestartMessage("§4now");
            send3 = true;
            initiateServerRestart();
        }
    }

    private void sendRestartMessage(String timeLeft) {

        for (CloudPlayer player : BedrockCloud.getCloudPlayerProvider().getCloudPlayerMap().values()) {
            PlayerTextPacket pk = new PlayerTextPacket();
            pk.playerName = player.getPlayerName();
            pk.type = pk.TYPE_MESSAGE;
            pk.value = BedrockCloud.prefix + "§cThe server is restarting in " + timeLeft + "§8.";
            player.getProxy().pushPacket(pk);
        }
    }

    private void initiateServerRestart() {
        BedrockCloud.setRunning(false);
        System.exit(0);
    }
}