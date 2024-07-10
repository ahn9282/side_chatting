package side.chatting.dto;

public interface Oauth2Response {
    String getProvider();

    String getProviderId();

    String getEmail();

    String getName();
}
