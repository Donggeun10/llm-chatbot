package com.example.chatbot.component;

import com.example.chatbot.domain.MembersResponse;
import com.example.chatbot.entity.Member;
import com.example.chatbot.exception.NotFoundMemberException;
import com.example.chatbot.repository.MemberRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AssistantTool {

    private final MemberRepository memberRepository;

    @Tool("List the member that the pet clinic has: register_id, member_name, phone number, pets")
    public MembersResponse getAllOwners() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Member> memberPage = memberRepository.findAll(pageable);
        return new MembersResponse(memberPage.getContent());
    }

    @Tool("Searches member for list, given the name of the member, and returns the member's information: register_id, member_name, phone number, pets")
    public MembersResponse searchMember(@P("search name") String name) {
        Optional<Member> memberOpt = memberRepository.findByMemberName(name);
        if(memberOpt.isPresent()) {
            return new MembersResponse(List.of(memberOpt.get()));
        }else{
            throw new NotFoundMemberException(String.format("Member(%s) not found", name));
        }
    }

}

