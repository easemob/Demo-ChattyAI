package com.easemob.chattyai.enums;

/**
 * @BelongsProject: FUtil
 * @BelongsPackage: com.futil.api.beans.enums
 * @Author: alonecoder
 * @CreateTime: 2023-04-23  21:37
 * @Description: 返回结果值的枚举类
 * @Version: 1.0
 */
public enum ResponseModelEnum {
    ERROR("-1","Failed"),
    OK("1","Successful"),
    ;


    String code;
    String message;

    ResponseModelEnum(String code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
