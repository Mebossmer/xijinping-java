package org.xijinping.bot.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;

public class BotConfig {
    public static class FilterPhrase {
        public String phrase;
        public int amount; // of social credits
    }

    public static class FilterChannelConfig {
        public boolean active;
        public String channel;
    }

    public String token;
    public String clientId;
    public String guildId;

    public String userDataStorage;

    public long lateMultiplier;

    public FilterChannelConfig filterChannel;

    public List<FilterPhrase> filter = new ArrayList<>();

    private static void copyExampleConfig(String outputPath) {
        try(InputStream is = BotConfig.class.getClassLoader().getResourceAsStream("exampleconfig.json")) {
            try(OutputStream os = new FileOutputStream(outputPath)) {
                is.transferTo(os);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static BotConfig get(String path) {
        StringBuilder builder = new StringBuilder();

        if(!Files.exists(Paths.get(path))) {
            System.out.println("No configuration file found, creating new one");

            copyExampleConfig(path);
        }

        Scanner sc;
        try {
            sc = new Scanner(new File(path));
            while(sc.hasNextLine()) {
                builder.append(sc.nextLine());
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        BotConfig conf = gson.fromJson(builder.toString(), BotConfig.class);

        System.out.println("Loaded bot configuration file");

        return conf;
    }
}
