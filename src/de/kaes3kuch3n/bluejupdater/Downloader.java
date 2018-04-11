package de.kaes3kuch3n.bluejupdater;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Downloader {

    public static void download(String url, String outputPath) {
        try (ReadableByteChannel rbc = Channels.newChannel(new URL(url).openStream());
             FileOutputStream out = new FileOutputStream(outputPath)
        ) {
            out.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
