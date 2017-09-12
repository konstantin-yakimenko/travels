package ru.travels.repositories;

import java.util.function.Supplier;

public class TryExecutor {

    public static <T> T execute(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (Exception e) {
                delay();
            }
        }
    }

    private static void delay() {
        try {
            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}