package com.avatarduel.exception;

public class GameStatusInitializationFailed extends Exception {
    public GameStatusInitializationFailed(String msg) {
        super("Game status initialization failed: " + msg);
    }
}
