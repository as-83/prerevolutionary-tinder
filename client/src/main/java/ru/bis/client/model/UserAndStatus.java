package ru.bis.client.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class UserAndStatus {
    private User user;
    private FanStatus status;
}
