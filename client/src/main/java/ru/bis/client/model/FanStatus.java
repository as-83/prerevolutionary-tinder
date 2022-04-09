package ru.bis.client.model;

public enum FanStatus {
    FAN("Вы любимы"),
    FAVORITE("Любимъ вами"),
    BOTH("Взаимно любимы");

    private final String title;
    FanStatus(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
