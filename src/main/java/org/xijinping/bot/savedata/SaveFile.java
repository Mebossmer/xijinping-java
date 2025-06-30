package org.xijinping.bot.savedata;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Optional;

import org.xijinping.bot.Bot;

public class SaveFile extends ArrayList<UserData> {
    private int version;

    public SaveFile(int version) {
        this.version = version;
    }

    public int getVersion() {
        return version;
    }

    public Optional<UserData> getUser(long userId) {
        return stream().filter(ud -> ud.userId == userId).findFirst();
    }

    // returns new amount of social credits
    public long addSocialCredits(long userId, long amount) {
        Optional<UserData> data = getUser(userId);

        long currentSocialCredits = 0;
        if(!data.isPresent()) {
            add(UserData.withSocialCredits(userId, amount));
            currentSocialCredits = amount;
        } else {
            data.get().socialCreditAmount += amount;
            currentSocialCredits = data.get().socialCreditAmount;
        }

        write();

        return currentSocialCredits;
    }

    // returns new amount of lashes
    public long addLash(long userId) {
        Optional<UserData> data = getUser(userId);

        long currentLashes = 0;
        if(!data.isPresent()) {
            add(UserData.withLashes(userId, 1));
            currentLashes = 1;
        } else {
            data.get().socialCreditAmount += 1;
            currentLashes = data.get().socialCreditAmount;
        }

        write();

        return currentLashes;
    }

    // returns true if record
    public boolean setRecordIfRecord(long userId, long timeInMilliseconds) {
        Optional<UserData> data = getUser(userId);

        if(!data.isPresent()) {
            add(UserData.withRecord(userId, timeInMilliseconds));
        } else if(timeInMilliseconds > data.get().lateRecord) {
            data.get().lateRecord = timeInMilliseconds;
        } else {
            return false;
        }

        write();

        return true;
    }

    public static SaveFile retrieve() {
        SaveFile file = new SaveFile(1);
        try(ObjectInputStream ois = new ObjectInputStream(
            new FileInputStream(Bot.getConfig().userDataStorage)
        )) {
            file = (SaveFile) ois.readObject();
        } catch (FileNotFoundException e) {
            System.out.println("User data file not found, creating new one");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void write() {
        try(ObjectOutputStream oos = new ObjectOutputStream(
            new FileOutputStream(Bot.getConfig().userDataStorage)
        )) {
            oos.writeObject(this);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
