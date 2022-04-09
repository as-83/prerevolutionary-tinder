package ru.bis.client.bot.model;

public enum Callback {
    FEMALE("Сударыня"),
    MAIL("Сударь"),
    MAIL_SEARCH("Сударя"),
    FEMALE_SEARCH("Сударыню"),
    ALL_SEARCH("Всех"),
    SUBSCRIBE("Да"),
    UNSUBSCRIBE("Нет"),
    FAVORITES("Любимци"),
    USERS_SEARCH("Поиск"),
    PREVIOUS("<-"),
    NEXT("->"),
    MENU("Меню");

    private final String title;

    Callback(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
