package side.chatting.dto;

import lombok.Data;

@Data
public class MailDto {

    private String receiver;
    private String title;
    private String content;
    private String authNum;

    public MailDto(String receiver) {
        this.receiver = receiver;
    }
}