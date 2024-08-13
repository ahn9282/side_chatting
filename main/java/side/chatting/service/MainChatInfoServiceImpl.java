package side.chatting.service;

import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import side.chatting.dto.ChatPageDto;
import side.chatting.dto.ChatRoomDto;
import side.chatting.entity.Member;
import side.chatting.repository.MemberRepository;

import java.util.Set;

@Service
public class MainChatInfoServiceImpl implements MainChatInfoService {


        private final MemberRepository memberRepository;

        public MainChatInfoServiceImpl(MemberRepository memberRepository) {
            this.memberRepository = memberRepository;
        }

        @Override
        public ChatPageDto mainPageInfoP(Long id, Integer pageNum) {
            if(pageNum == null) pageNum = 1;
            Set<Member> friends = memberRepository.withFriends(id);
            Slice<ChatRoomDto> chatRooms = memberRepository.chatRoomPagingSlice(id, pageNum);
            return new ChatPageDto(id, friends, chatRooms);

    }
}
