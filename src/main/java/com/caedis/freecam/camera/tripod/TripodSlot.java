package com.caedis.freecam.camera.tripod;

import static org.lwjgl.input.Keyboard.KEY_1;

// credit: https://github.com/hashalite/Freecam/blob/main/common/src/main/java/net/xolt/freecam/tripod/TripodSlot.java
public enum TripodSlot {

    NONE,
    ONE,
    TWO,
    THREE,
    FOUR,
    FIVE,
    SIX,
    SEVEN,
    EIGHT,
    NINE;

    public static final int MIN = 1;
    public static final int MAX = values().length - 1;

    @Override
    public String toString() {
        return this == NONE ? "None" : String.format("#%d", ordinal());
    }

    public static boolean inRange(int number) {
        return number >= MIN && number <= MAX;
    }

    public static TripodSlot valueOf(int number) throws IndexOutOfBoundsException {
        if (!inRange(number)) {
            throw new IndexOutOfBoundsException(
                String.format("Cannot get TripodSlot for number %d: must be %d-%d.", number, MIN, MAX));
        }
        return valueOfUnsafe(number);
    }

    public static TripodSlot ofKeyCode(int keyCode) {
        int number = keyCode - KEY_1 + 1;
        return inRange(number) ? valueOfUnsafe(number) : NONE;
    }

    private static TripodSlot valueOfUnsafe(int number) {
        return values()[number];
    }
}
