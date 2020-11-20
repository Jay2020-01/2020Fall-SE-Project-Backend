package com.backend.server.entity.pojo;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.ToString;

/**
 * 返回结果实体类
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@ToString
public class Result {

    private Integer code;// 返回码
    private String message;//返回信息
    private Object data;// 返回数据

    private Result() {}

    private Result(Integer code, String message) {
        super();
        this.code = code;
        this.message = message;
    }

    private Result(Integer code, String message, Object data) {
        super();
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static Result create(Integer code, String message) {
        return new Result(code, message);
    }

    public static Result create(Integer code, String message, Object data) {
        return new Result(code, message, data);
    }

}
