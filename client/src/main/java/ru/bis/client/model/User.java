package ru.bis.client.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty(value = "userId")
    private long dbId;

    @JsonProperty(value = "telegramId")
    private long tgId;

    private String name;

    private String description;

    @JsonProperty(value = "sex")
    private Gender gender;

    @JsonProperty(value = "searchPreferences")
    private Gender searchGender;

    @Getter(onMethod_ = @JsonIgnore)
    @Setter(onMethod_ = @JsonIgnore)
    private BotState botState;

    public User(long tgId, String name, String description, Gender gender, Gender searchGender, BotState botState) {
        this.tgId = tgId;
        this.name = name;
        this.description = description;
        this.gender = gender;
        this.searchGender = searchGender;
        this.botState = botState;
    }

    public User(long tgId, BotState botState) {
        this.tgId = tgId;
        this.botState = botState;
    }

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
