package dev.gigafyde.stitchd.Listeners;

import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;

public class PrivateMessageListener extends ListenerAdapter {
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        event.getMessage().getAttachments().get(0).downloadToFile("download.zip");



    }
}
