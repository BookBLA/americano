package com.bookbla.americano.base.exceptions;

import com.bookbla.americano.base.enums.exception.ErrorCodeEnum;
import lombok.Getter;

import java.util.Map;

@Getter
public class BizException extends RuntimeException {

    private ErrorCodeEnum code;
    private Map<String, String> args;

    public BizException(String message) {
        super(message);
    }

    public BizException(ErrorCodeEnum code) {
        super(code.getName() + "/" + code.getLongMessage());
        this.code = code;
    }

    public BizException(ErrorCodeEnum code, Map<String, String> args) {
        super(code.getName() + "/" + code.getLongMessage() + "/" + args);
        this.code = code;
        this.args = args;
    }

    public BizException(ErrorCodeEnum code, String message) {
        super(message);
        this.code = code;
    }

    public BizException(ErrorCodeEnum code, Map<String, String> args, String message) {
        super(message);
        this.code = code;
        this.args = args;
    }

    public BizException(Map<String, Object> args) {
        super(args.toString());
    }
}
