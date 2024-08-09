package side.chatting.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
public class HomeController {

    private final MemberService memberService;

    @GetMapping("/")
    public String homeP() {
        return "index";
    }
    @GetMapping("/auth-ok")
    public String authOkP() {
        return "index";
    }

    @GetMapping("/login")
    public String loginP() {
        return "login";
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<?> loginProcess(LoginForm form) {


        return null;

    }

    @GetMapping("/sign_on")
    public String joinP() {
        return "join";
    }


    @GetMapping("/user")
    @ResponseBody
    public String testAuthentication() {
        return "user";
    }



    @GetMapping("/test")
    public String as() {

        return "testTrans";
    }

}
