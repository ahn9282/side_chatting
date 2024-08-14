package side.chatting.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import side.chatting.dto.ChatPageDto;
import side.chatting.dto.ChatRoomDto;
import side.chatting.entity.ChatRoom;
import side.chatting.entity.Member;

import java.util.List;
import java.util.Set;

public interface MemberRepositoryCustom {
    List<Member> withFriends(Long id);

    Slice<ChatRoomDto> chatRoomPagingSlice(Long id, Integer pageNum);

}
