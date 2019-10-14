package com.bsw.mydemo.utils.websocket.messages;

/// An exception occured in the WS reader or WS writer.
public class WebError extends Message {

    public Exception mException;

    public WebError(Exception e) {
        mException = e;
    }
}
