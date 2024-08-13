package side.chatting.repository;


import com.querydsl.core.QueryFactory;

public class ChatRoomRepositoryImpl implements ChatRoomRepositoryCustom {

    private final QueryFactory queryFactory;

    public ChatRoomRepositoryImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

}
