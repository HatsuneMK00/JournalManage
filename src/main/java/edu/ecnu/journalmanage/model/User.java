package edu.ecnu.journalmanage.model;

import lombok.Data;

@Data
public class User {
    private Integer id;
    private String name;
    private String password;
    private Role role;
    private boolean valid;
    private String researchArea;
    private String title;
    private String email;
}
