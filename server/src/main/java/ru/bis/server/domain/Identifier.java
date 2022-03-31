package ru.bis.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;


import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "identifier")
public class Identifier {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE )
    private Long id;

    private IdentifierType idType;
    private Long identifierId;

    @Getter(onMethod_=@JsonIgnore)
    @Setter(onMethod_=@JsonIgnore)
    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

}
