package side.chatting.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import side.chatting.dto.MailDto;
import side.chatting.service.MemberService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberApiController {

    private final MemberService memberService;
    private final JavaMailSender javaMailSender;
    private static final String senderEmail="ssja@gmail.com";

    @GetMapping("/email/check")
    public ResponseEntity<String> checkEmailDuple(@RequestParam("email")String email) {
        try{
            
       
        if (memberService.checkDupleEmail(email)) {

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 가입된 이메일");
        }
        MailDto mail = new MailDto(email);
        mail.setTitle("채팅 가입 메일");
        String authNum = UUID.randomUUID().toString().substring(0, 6);
        mail.setAuthNum(authNum);
        mail.setContent(" 현재 이메일로 가입을 위한 인증번호를 보내드립니다. 인증을 마쳐주세요.\n" + authNum);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mail.getReceiver());
        message.setFrom(senderEmail);
        message.setSubject(mail.getTitle());
        message.setText(mail.getContent());

            javaMailSender.send(message);

        return ResponseEntity.status(HttpStatus.OK).body(authNum);

        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러");
        }
    }
    
}
