package side.chatting.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import side.chatting.dto.JoinForm;
import side.chatting.dto.MailDto;
import side.chatting.entity.Member;
import side.chatting.service.MemberService;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberApiController {

    private static final String senderEmail = "ssja@gmail.com";
    private final MemberService memberService;
    private final JavaMailSender javaMailSender;
    private final String JOIN_EMAIL_TITLE = "채팅 가입 메일";
    private final String JOIN_EMAIL_CONTENT = " 현재 이메일로 가입을 위한 인증번호를 보내드립니다. 인증을 마쳐주세요.\n";
    private final String AUTH_EMAIL_TITLE = "채팅 메일 계정 찾기 인증";
    private final String AUTH_EMAIL_CONTENT = " 현재 이메일로 아이디 또는 임시 비밀번호를 보내드립니다. 인증을 마쳐주세요.\n";

    @GetMapping("/email/check")
    public ResponseEntity<String> checkEmailDuple(@RequestParam("email") String email) {
        try {
            if (memberService.checkDupleEmail(email).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일");
            }
            String authNum = UUID.randomUUID().toString().substring(0, 6);
            emailAuth(email, JOIN_EMAIL_TITLE, JOIN_EMAIL_CONTENT, authNum);

            return ResponseEntity.status(HttpStatus.OK).body(authNum);

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러");
        }
    }

    @GetMapping("/username/check")
    public ResponseEntity<String> checkIdDuple(@RequestParam("user") String id) {
        try {
            if (memberService.checkDupleUsername(id)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복 아이디");
            }
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/name/check")
    public ResponseEntity<String> checkNameDuple(@RequestParam("name") String name) {
        try {
            if (memberService.checkDupleName(name)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복 닉네임");
            }
            return ResponseEntity.status(HttpStatus.OK).body("");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/join-us")
    public ResponseEntity<String> joinProcess(@RequestBody JoinForm joinData) {
        log.info("join data : {}", joinData);

        return null;
    }
    @PostMapping("/find/id")
    public ResponseEntity<String> findUserName(@RequestBody String data, MemberApiController memberApiController) {
        try {
            Optional<Member> findMemberInfo = memberService.checkDupleEmail(data);
            if (findMemberInfo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 이메일입니다.");
            }
            String findUsername = findMemberInfo.get().getUsername();
            memberApiController.emailAuth(data, memberApiController.AUTH_EMAIL_TITLE, memberApiController.AUTH_EMAIL_CONTENT, findUsername);

            return ResponseEntity.status(HttpStatus.OK).body("success");


        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러");
        }
    }

    @PostMapping("/find/pw")
    public ResponseEntity<String> findPassWord(@RequestBody String data) {
        try {
            String tempPassWord = memberService.findPassWordProcess(data);
            if (tempPassWord == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("존재하지 않는 계정입니다.");
            } else {
                String[] emailAndTempPw = tempPassWord.split("&");
                emailAuth(emailAndTempPw[0], AUTH_EMAIL_TITLE, AUTH_EMAIL_CONTENT, emailAndTempPw[1]);

                return ResponseEntity.status(HttpStatus.OK).body("success");
            }
        } catch (MailSendException msgEx) {
            msgEx.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 에러");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러");
        }
    }

    private void emailAuth(String email, String emailTitle, String emailContent, String value) {
        MailDto mail = new MailDto(email);
        mail.setTitle(emailTitle);
        mail.setAuthNum(value);
        mail.setContent(emailContent + value);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getReceiver());
        message.setFrom(senderEmail);
        message.setSubject(mail.getTitle());
        message.setText(mail.getContent());
        javaMailSender.send(message);

    }

}
