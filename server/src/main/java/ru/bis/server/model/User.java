package ru.bis.server.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "usr")
public class User {

    @Id
    @Column(name = "user_id")
    @SequenceGenerator(name = "usrIdSeq", sequenceName = "usr_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usrIdSeq")
    private long userId;

    @Getter(onMethod_ = @JsonIgnore)
    @Setter(onMethod_ = @JsonIgnore)
    private long telegramId;

    private String name;

    private String description;

    private Sex sex;

    private Sex searchPreferences;

    @OneToOne(mappedBy = "user")
    private LinkTemplate linkTemplate;



    public User(long telegramId, String name, String description, Sex sex, Sex searchPreferences) {
        this.telegramId = telegramId;
        this.name = name;
        this.description = description;
        this.sex = sex;
        this.searchPreferences = searchPreferences;
    }

    @Getter(onMethod_ = @JsonIgnore)
    @Setter(onMethod_ = @JsonIgnore)
    @ManyToMany
    @JoinTable(
            name = "user_user",
            joinColumns = {@JoinColumn(name = "usr_id")},
            inverseJoinColumns = {@JoinColumn(name = "favorit_id")}
    )
    @ToString.Exclude
    private List<User> favorites = new ArrayList<>();

    @Getter(onMethod_ = @JsonIgnore)
    @Setter(onMethod_ = @JsonIgnore)
    @ManyToMany
    @JoinTable(
            name = "user_user",
            joinColumns = {@JoinColumn(name = "favorit_id")},
            inverseJoinColumns = {@JoinColumn(name = "usr_id")}
    )
    @ToString.Exclude
    private List<User> fans = new ArrayList<>();

   /* @Getter(onMethod_=@JsonIgnore)
    @Setter(onMethod_=@JsonIgnore)
    @OneToMany(mappedBy = "user")
    private List<Identifier> identifiers = new ArrayList<>();*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return userId == user.userId;
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}



