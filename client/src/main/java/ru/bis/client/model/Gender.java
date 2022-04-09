package ru.bis.client.model;

public enum Gender {
    MAIL("Сударъ"),
    FEMALE("Сударыня"),
    ALL("Все");

    private final String title;
    Gender(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }
}
