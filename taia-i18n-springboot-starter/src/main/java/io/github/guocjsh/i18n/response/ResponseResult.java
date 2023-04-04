package io.github.guocjsh.i18n.response;


import io.github.guocjsh.i18n.context.I18nContextHandler;
import io.github.guocjsh.i18n.util.I18nUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * @ClassName ResponseObject
 * @Description TODO
 * @Author iron
 * @Date 2020/8/10 10:54
 * @Version 1.0
 */
public class ResponseResult<T> {


    /**
     * 返回码
     */
    private Integer code;
    /**
     * 消息
     */
    private String message;

    /**
     * 返回
     */
    private T data;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public ResponseResult() {
    }

    public ResponseResult(Integer code, String message, T data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ResponseResult<T> Error(Integer code, String message) {
        return setResult(code, message, null);
    }

    // 返回错误，可以传msg
    public static <T> ResponseResult<T> Error(String message) {
        return setResult(ResponseStatus.RESPONSE_ERROR.getCode(), message, null);
    }

    // 返回错误
    public static <T> ResponseResult<T> Error() {
        return setResult(ResponseStatus.RESPONSE_ERROR.getCode(), ResponseStatus.RESPONSE_ERROR.getValue(), null);
    }

    // 返回成功，可以传data值
    public static <T> ResponseResult<T> Success(T data) {
        return setResult(ResponseStatus.RESPONSE_SUCCESS.getCode(), ResponseStatus.RESPONSE_SUCCESS.getValue(), data);
    }


    // 返回成功，沒有data值
    public static <T> ResponseResult<T> Success() {
        return setResult(ResponseStatus.RESPONSE_SUCCESS.getCode(), ResponseStatus.RESPONSE_SUCCESS.getValue(), null);
    }

    // 返回成功，沒有data值
    public static <T> ResponseResult<T> Success(String message) {
        return setResult(ResponseStatus.RESPONSE_SUCCESS.getCode(), message, null);
    }

    // 通用封装
    public static <T> ResponseResult<T> setResult(Integer code, String message, T data) {
        if (StringUtils.isNoneBlank(I18nContextHandler.getLang())) {
            if (code == 20000) {
                message = I18nContextHandler.getLang().equals("zh-CN") ? message : "request successful";
            } else if (code == 52000) {
                message = I18nContextHandler.getLang().equals("zh-CN") ? message :"request failure";
            } else {
                message = I18nUtil.getMessage(message, I18nContextHandler.getLang());
            }
        }
        return new ResponseResult<T>(code, message, data);
    }

    // 调用数据库层判断
    public Boolean daoResult(int result) {
        return result > 0 ? true : false;
    }

    // 接口直接返回true 或者false
    public Boolean isSuccess(ResponseResult<?> res) {
        if (res == null) {
            return false;
        }
        if (res.getCode().equals(ResponseStatus.RESPONSE_SUCCESS.getCode())) {
            return false;
        }
        return true;
    }


}
