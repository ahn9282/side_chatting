package side.chatting.dto;


import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;
import org.springframework.data.domain.Slice;
import side.chatting.entity.ChatRoom;
import side.chatting.entity.Member;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Data
public class ChatPageDto {

    private MemberDto member;
    private Slice<ChatRoomDto> chatRooms;
    private Set<MemberDto> friends;

    public ChatPageDto() {
    }

    public ChatPageDto(Long id, Set<Member> friends, Slice<ChatRoomDto> chatRooms) {
        this.friends = friends.stream()
                .map(MemberDto::new)
                .peek(dto -> {
                    if (dto.getId().equals(id)) {
                        this.member = dto;
                        System.out.println("EQUAL!! Find USer" + dto.toString());
                    }
                })
                .filter(dto -> !dto.getId().equals(id))
                .collect(Collectors.toSet());
        this.chatRooms = chatRooms;
    }

}
