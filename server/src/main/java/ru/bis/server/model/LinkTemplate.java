package ru.bis.server.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder
@Table(name = "LINK_TEMPLATE")
public class LinkTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkTemplateId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    private Long bannerTemplateId;

    public LinkTemplate(User user) {
        this.user = user;
    }
}
