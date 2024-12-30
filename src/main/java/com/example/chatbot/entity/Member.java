package com.example.chatbot.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Entity
@Getter
@Table(name="TB_MEMBER")
@ToString(exclude = "password")
@Builder
@AllArgsConstructor
public class Member {

    private int registerId;

    @Id
    private String memberName;
    private String password;
    private String phoneNumber;
    private String pets;


    public Member() {
        // comment explaining why the method is empty
    }
}
