package side.chatting.service;


import side.chatting.dto.ChatPageDto;

import java.awt.print.Pageable;

public interface MainChatInfoService {

    ChatPageDto mainPageInfoP(Long id, Integer pageNum);
}
