package com.root.redis.vo;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ErrorVO {

    private String errorCode;
    private String errorMsg;
    private String url;

}
