package com.example.chatbot.repository;


import com.example.chatbot.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByMemberName(String memberName);

}
