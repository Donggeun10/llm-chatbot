package com.example.chatbot.domain;

import com.example.chatbot.entity.Member;
import java.util.List;

public record MembersResponse(List<Member> member) {
}
