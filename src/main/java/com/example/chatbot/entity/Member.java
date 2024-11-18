package com.example.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name="TB_MEMBER")
@ToString(exclude = "password")
public class Member {

    private int registerId;

    @Id
    private String memberName;
    private String password;
    private String phoneNumber;
    private String pets;
}
