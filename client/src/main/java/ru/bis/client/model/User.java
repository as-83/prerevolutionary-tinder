package ru.bis.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.bis.client.bot.BotState;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User{
    private long dbId;

    private long tgId;

    private String name;

    private String description;

    private Gender gender;

    private Gender searchGender;

    BotState botState;

    //private List<Long> favoritesId;//TODO

   // private List<Long> fansId;//TODO


    @Override
    public String toString() {
        String preferences;
        switch (searchGender) {
            case MAIL: preferences = "Сударя"; break;
            case FEMALE: preferences = "Сударыню"; break;
            default: preferences = "Всех";
        }
        return
                gender.getTitle() + " " + name +
                ".\nО себе: " + description +
                ".\nИщу  " + preferences;
    }
}
