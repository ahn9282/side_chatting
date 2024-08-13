package side.chatting.excpetion;

public class NotExistChatRoomException extends RuntimeException {
    public NotExistChatRoomException() {
        super();
    }

    public NotExistChatRoomException(String message) {
        super(message);
    }
}
