package com.bsw.mydemo.utils.websocket.messages;


import com.bsw.mydemo.utils.websocket.exceptions.WebSocketException;

/// WebSockets reader detected WS protocol violation.
public class ProtocolViolation extends Message {

    public WebSocketException mException;

    public ProtocolViolation(WebSocketException e) {
        mException = e;
    }
}
