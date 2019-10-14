package com.bsw.mydemo.utils.websocket.messages;

public class CannotConnect extends Message {
    public final String reason;

    public CannotConnect(String reason) {
        this.reason = reason;
    }
}
