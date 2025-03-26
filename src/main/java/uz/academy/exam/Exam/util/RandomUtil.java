package uz.academy.exam.Exam.util;

import java.util.Random;

public final class RandomUtil {
    private final static Random RANDOM;

    static {
        RANDOM = new Random();
    }

    public static int getRandomVal(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }

    public static int getRandomVal(int max) {
        return RANDOM.nextInt(max);
    }
}