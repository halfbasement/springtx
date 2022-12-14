package hello.springtx.propagation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
class MemberServiceTest {


    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    LogRepository logRepository;


    /**
     *  memberService @Transactional:OFF
     *  memberRepository @Transactional:ON
     *  logRepository @Transactional:ON
     */
    @Test
    void outerTxOff_success() {

        //givens
        String username = "outerTxOff_success";

        //when
        memberService.joinV1(username);


        //when: 모든 데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isPresent());


    }

    /**
     *  memberService @Transactional:OFF
     *  memberRepository @Transactional:ON
     *  logRepository @Transactional:ON Exception
     */
    @Test
    void outerTxOff_fail() {

        //givens
        String username = "로그예외_outerTxOff_success";

        //when
        assertThatThrownBy(()->memberService.joinV1(username)).isInstanceOf(RuntimeException.class);

        //when: 모든 데이터가 정상 저장된다.
        Assertions.assertTrue(memberRepository.find(username).isPresent());
        Assertions.assertTrue(logRepository.find(username).isEmpty()); //롤백되어서 empty


    }
}