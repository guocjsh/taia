package io.github.guocjsh.excel.exception;

import org.slf4j.helpers.MessageFormatter;

public class AllEmptyRowException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public AllEmptyRowException(String format, Object... arguments) {
        super(MessageFormatter.arrayFormat(format, arguments).getMessage());
    }
}