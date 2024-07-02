package side.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import side.chatting.dto.LoginForm;
import side.chatting.dto.MemberDto;
import side.chatting.service.MemberService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String homeP() {
        return "login.html";
    }

    @GetMapping("/login")
    public String loginP() {
        return "login.html";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginProcess(LoginForm form) {
        return null;
    }

    @GetMapping("/join")
    public String joinP() {
        return null;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinProcess(@RequestBody  MemberDto form, BindingResult result) {
        log.info("input data : {}", form);
        if (result.hasErrors()) {
            return new ResponseEntity<>("모두 입력하셔야 합니다.", HttpStatus.BAD_REQUEST);
        }
        if (!memberService.checkDupleUsername(form))
            return new ResponseEntity<>("ID 중복", HttpStatus.BAD_REQUEST);
        if (!memberService.checkDupleName(form))
            return new ResponseEntity<>("닉네임 중복", HttpStatus.BAD_REQUEST);
        MemberDto newMember = memberService.joinMember(form);
        if (newMember == null) return new ResponseEntity<>("회원 가입에 문제가 생겼습니다.", HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(newMember, HttpStatus.OK);
    }
}
