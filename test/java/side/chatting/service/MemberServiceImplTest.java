package side.chatting.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import side.chatting.dto.MemberDto;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberServiceImplTest {

    @Autowired
    MemberService memberService;
    @Test
    void validationServiceTest(){
            MemberDto data = new MemberDto();
            data.setUsername("test1");
            data.setName("userA");
        assertThat(memberService.checkDupleName(data)).isFalse();
        assertThat(memberService.checkDupleUsername(data)).isFalse();
        data.setUsername("test123");
        data.setName("userAqwe");
        assertThat(memberService.checkDupleName(data)).isTrue();
        assertThat(memberService.checkDupleUsername(data)).isTrue();
    }
}