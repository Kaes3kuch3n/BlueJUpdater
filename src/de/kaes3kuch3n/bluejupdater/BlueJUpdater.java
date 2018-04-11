package de.kaes3kuch3n.bluejupdater;

import java.io.IOException;
import java.net.URL;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Scanner;

public class BlueJUpdater {

    private static final String PATH_TO_BLUEJ_APP = "/Applications/BlueJ.app";
    private static int latestVersion;
    private static String latestVersionString;

    public static void main(String[] args) {
        System.out.println("Checking for update...");
        if (checkForUpdate()) {
            System.out.println("Update available!\nDownloading version " + latestVersionString + " ...");
            Downloader.download("https://bluej.org/download/files/BlueJ-mac-" + latestVersion + ".zip", "bluej.zip");
            //Unzipper.unzip("bluej.zip", "bluej");
            //update();
            //cleanUp();
        } else
            System.out.println("Up to date!");
    }

    /**
     * Checks if an update for BlueJ is available.
     * @return true if an update is avaiable
     */
    private static boolean checkForUpdate() {
        int currentVersion = getCurrentVersion();
        latestVersion = getLatestVersion();
        return (currentVersion < latestVersion);
    }

    /**
     * Replaces the currently installed version of BlueJ with the latest version downloaded.
     */
    private static void update() {
        Path currentBlueJApp = Paths.get(PATH_TO_BLUEJ_APP);
        Path latestBlueJApp = Paths.get("bluej/BlueJ " + latestVersionString + "/BlueJ.app");
        try {
            deleteDirectory(currentBlueJApp);
            Files.move(latestBlueJApp, currentBlueJApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes the downloaded files that are not needed anymore.
     */
    private static void cleanUp() {
        Path zipFile = Paths.get("bluej.zip");
        Path zipFolder = Paths.get("bluej");
        try {
            Files.delete(zipFile);
            deleteDirectory(zipFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets the version number of the currently installed BlueJ.app.
     * @return The version number
     */
    private static int getCurrentVersion() {
        try (Scanner scanner = new Scanner(Paths.get(PATH_TO_BLUEJ_APP + "/Contents/Info.plist"))) {
            String line = scanner.nextLine();
            while (!line.contains("CFBundleVersion"))
                line = scanner.nextLine();
            line = scanner.nextLine();
            line = line.trim().replaceAll("<|/|s|t|r|i|n|g|>|\\.|", "");
            return Integer.parseInt(line);
        } catch (IOException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * Gets the version number of the latest BlueJ.app available.
     * @return The latest version number.
     */
    private static int getLatestVersion() {
        latestVersionString = getLatestVersionString();
        return Integer.parseInt(latestVersionString.replace(".", ""));
    }

    /**
     * Gets the version String of the latest BlueJ.app available.
     * @return The latest version String.
     */
    private static String getLatestVersionString() {
        try {
            URL url = new URL("https://bluej.org/index.html");
            Scanner scanner = new Scanner(url.openStream());
            String line = scanner.nextLine();
            while (!line.contains("Version"))
                line = scanner.nextLine();
            return line.trim().substring(12, 17);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Deletes a directory recursively.
     * @param dir The directory to delete
     * @throws IOException
     */
    private static void deleteDirectory(Path dir) throws IOException {
        Files.walk(dir, FileVisitOption.values())
                .sorted(Comparator.reverseOrder())
                .forEach(file -> {
                    try {
                        Files.delete(file);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }
}
