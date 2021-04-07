package ua.metal.nn.exception;

import lombok.Getter;

@Getter
public class BaseException extends Exception {

    private String msg;

    public BaseException(String msg) {
        super(msg);
        this.msg = msg;
    }

}
