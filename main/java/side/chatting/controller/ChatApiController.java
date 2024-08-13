package side.chatting.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import side.chatting.dto.ChatPageDto;
import side.chatting.dto.CustomUser;
import side.chatting.entity.Member;
import side.chatting.jwt.JwtUtil;
import side.chatting.repository.MemberRepository;
import side.chatting.service.MainChatInfoService;

import java.util.List;

@RestController
@Slf4j
public class ChatApiController {
    private final MemberRepository memberRepository;
    private final JwtUtil jwtUtil;
    private final MainChatInfoService mainChatInfoService;

    public ChatApiController(MemberRepository memberRepository, JwtUtil jwtUtil, MainChatInfoService mainChatInfoService) {
        this.memberRepository = memberRepository;
        this.jwtUtil = jwtUtil;
        this.mainChatInfoService = mainChatInfoService;
    }

    @GetMapping("/user/chat")
    public ResponseEntity<ChatPageDto> authOkP(@RequestParam("pageNum")Integer pageNum) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUser token = (CustomUser) authentication.getPrincipal();
       Long id = token.getMemberId();
        log.info("token : {}, id : {}", token, id);
        ChatPageDto data = mainChatInfoService.mainPageInfoP(id, pageNum);


        return ResponseEntity.status(HttpStatus.OK).body(data);
    }
}
