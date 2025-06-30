package org.xijinping.bot.savedata;

import java.io.Serializable;

public class UserData implements Serializable {
    public long userId;
    public long socialCreditAmount;
    public long lashes;
    public long lateRecord; // stored in milliseconds

    public UserData(long userId, long socialCreditAmount, long lashes, long lateRecord) {
        this.userId = userId;
        this.socialCreditAmount = socialCreditAmount;
        this.lashes = lashes;
        this.lateRecord = lateRecord;
    }

    public static UserData withSocialCredits(long userId, long amount) {
        return new UserData(userId, amount, 0, 0);
    }

    public static UserData withLashes(long userId, long amount) {
        return new UserData(userId, 0, amount, 0);
    }

    public static UserData withRecord(long userId, long recordInMilliseconds) {
        return new UserData(userId, 0, 0, recordInMilliseconds);
    }
}
