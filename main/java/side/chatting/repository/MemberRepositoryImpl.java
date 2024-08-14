package side.chatting.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.*;
import side.chatting.dto.ChatPageDto;
import side.chatting.dto.ChatRoomDto;
import side.chatting.entity.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static side.chatting.entity.QChatMember.chatMember;
import static side.chatting.entity.QChatRoom.chatRoom;
import static side.chatting.entity.QFriendShip.*;
import static side.chatting.entity.QMember.*;


public class MemberRepositoryImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    public MemberRepositoryImpl(JPAQueryFactory jpaQueryFactory) {
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public List<Member> withFriends(Long id) {
       return jpaQueryFactory
                .selectDistinct(member)
                .from(friendShip)
               .innerJoin(member).on(friendShip.member.id.eq(id).or(friendShip.friend.id.eq(id)))
                .groupBy(member.id)
               .fetch();
    }

    @Override
    public Slice<ChatRoomDto> chatRoomPagingSlice(Long id, Integer pageNum) {

        PageRequest pageable = PageRequest.of(pageNum, 20, Sort.by(Sort.Direction.DESC, "createdDate"));

        JPAQuery<ChatRoom> query = jpaQueryFactory
                .select(chatRoom)
                .from(chatRoom)
                .leftJoin(chatMember).on(chatMember.chatRoom.id.eq(chatRoom.id))
                .where(chatMember.member.id.eq(id))
                .orderBy(chatRoom.createdDate.desc())
                .offset(pageNum * 20)
                .limit(21);

        List<ChatRoom> result = query.fetch();

        boolean hasNext = result.size() > 20;
        if (hasNext) {
            result.remove(result.size() - 1);
        }
        List<ChatRoomDto> list = result.stream().map(ChatRoomDto::new).toList();

        return new SliceImpl<>(list, pageable, hasNext);
    }


    }
