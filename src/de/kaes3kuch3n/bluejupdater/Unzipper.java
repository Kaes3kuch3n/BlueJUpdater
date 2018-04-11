package de.kaes3kuch3n.bluejupdater;

import java.io.*;
import java.nio.file.*;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class Unzipper {

    public static void unzip(String zipFile, String outputPath) {
        ZipFile zip;
        Enumeration<ZipEntry> entries;

        try {
            zip = new ZipFile(zipFile);
            entries = (Enumeration<ZipEntry>)zip.entries();

            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();

                if (entry.isDirectory()) {
                    (new File(outputPath + File.separator + entry.getName())).mkdirs();
                    continue;
                }

                copyInputStream(zip.getInputStream(entry), new BufferedOutputStream(
                                new FileOutputStream(outputPath + File.separator + entry.getName())));
            }
            zip.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void copyInputStream(InputStream in, OutputStream out)
            throws IOException
    {
        byte[] buffer = new byte[1024];
        int len;

        while((len = in.read(buffer)) >= 0)
            out.write(buffer, 0, len);

        in.close();
        out.close();
    }

}
