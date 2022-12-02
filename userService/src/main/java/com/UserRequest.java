package com;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class UserRequest {
    private String userName;
    private String mail;
    private String name;
    private int age;
}
