package io.github.guocjsh.excel.exception;

import org.slf4j.helpers.MessageFormatter;

public class TaiaExcelException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public TaiaExcelException(String msg) {
        super(msg);
    }

    public TaiaExcelException(String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }

    public TaiaExcelException(Throwable cause, String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage(), cause);
    }

    public TaiaExcelException(Throwable cause) {
        super(cause);
    }
}