package com.bookbla.americano.base.handler;

import com.bookbla.americano.base.dto.BaseErrorResponseDTO;
import com.bookbla.americano.base.enums.exception.ErrorCodeEnum;
import com.bookbla.americano.base.exceptions.BizException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class BaseExceptionHandler extends ResponseEntityExceptionHandler {


    //fixme 리팩토링 대상
    @ResponseStatus(OK)
    @ExceptionHandler(Exception.class)
    public BaseErrorResponseDTO defaultErrorHandler(Exception e) {

        log.error("## Biz exception response ##");
        log.error("{}", e.toString());

        boolean isBizException = e instanceof BizException;
//        boolean isLoginException = e instanceof InternalAuthenticationServiceException;
        String errorCode = null;
        String errorLongMessage;
        String errorShortMessage;

        if (isBizException) {
            BizException bizException = (BizException) e;
            ErrorCodeEnum errorCodeEnum = bizException.getCode();
            if (errorCodeEnum == null) {
                errorShortMessage = e.getMessage();
                errorLongMessage = e.getMessage();
            } else {
                errorCode = errorCodeEnum.getName();
                errorShortMessage = errorCodeEnum.getShortMessage();
                errorLongMessage = errorCodeEnum.getLongMessage();

                if (hasErrorArgument(bizException)) {
                    Map<String, String> argsMap = bizException.getArgs();
                    for (Map.Entry<String, String> entry : argsMap.entrySet()) {
                        String argsKey = "{" + entry.getKey() + "}";
                        errorLongMessage = errorLongMessage.replace(argsKey, entry.getValue());
                        errorShortMessage = errorShortMessage.replace(argsKey, entry.getValue());
                    }
                }
            }
            return BaseErrorResponseDTO.error(errorCode, errorLongMessage, errorShortMessage);

            //todo 로그인 시 필요
//        } else if (isLoginException) {
//            errorShortMessage = e.getMessage();
//            errorLongMessage = e.getMessage();
//            return BaseErrorResponseDTO.error(errorLongMessage, errorShortMessage);

        } else {
            errorLongMessage = e.getMessage();
            errorShortMessage = "system error";
            return BaseErrorResponseDTO.systemError(errorLongMessage, errorShortMessage);
        }
    }

    private boolean hasErrorArgument(BizException bizException) {
        return bizException.getArgs() != null && !bizException.getArgs().isEmpty();
    }
}