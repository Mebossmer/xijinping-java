package org.xijinping.bot.casino;

public record Card(Color color, Symbol symbol, Type type) {
    public enum Color {
        BLACK,
        RED
    }

    public enum Symbol {
        HEART,
        DIAMOND,
        CLUB,
        SPADE
    }

    public enum Type {
        ACE(-1), // can be 1 or 11
        TWO(2),
        THREE(3),
        FOUR(4),
        FIVE(5),
        SIX(6),
        SEVEN(7),
        EIGHT(8),
        NINE(9),
        TEN(10),
        JACK(10),
        QUEEN(10),
        KING(10);

        public final int value;

        Type(int value) {
            this.value = value;
        }
    }
}
