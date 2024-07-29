package side.chatting.Config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.CloseStatus;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class MultipleWebSocketHandler extends TextWebSocketHandler {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();



    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // 클라이언트와의 연결이 설정되었을 때 호출
        String clientId = session.getId();
        sessions.put(clientId, session);
        log.info("Connect Client  id : {}", clientId);
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // 클라이언트와의 연결이 종료되었을 때 호출
        String clientId = session.getId();
        sessions.remove(clientId);
    }
}
