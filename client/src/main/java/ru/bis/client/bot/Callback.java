package ru.bis.client.bot;

public enum Callback {
    FEMALE("Сударыня"),
    MAIL("Сударь"),
    MAIL_SEARCH("Сударя"),
    FEMALE_SEARCH("Сударыню"),
    ALL_SEARCH("Всех"),
    SUBSCRIBE("Да"),
    UNSUBSCRIBE("Нет"),
    FAVORITES("Любимци"),
    CANDIDATES("Поиск"),
    PREVIOUS("<-"),
    NEXT("->"),
    PREV_CANDIDATE("<-"),
    NEXT_CANDIDATE("->"),
    MENU("Меню");

    private final String title;

    Callback(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
