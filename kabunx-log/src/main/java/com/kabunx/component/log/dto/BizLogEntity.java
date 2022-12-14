package com.kabunx.component.log.dto;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.Map;

@Data
@Builder
public class BizLogEntity {

    /**
     * 租户
     */
    private String tenantName;

    /**
     * 保存的操作日志的类型，比如：订单类型、商品类型
     */
    @NotBlank(message = "type required")
    @Length(max = 200, message = "type max length is 200")
    private String type;

    /**
     * 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
     */
    private String subType;

    /**
     * 日志绑定的业务标识
     */
    @NotBlank(message = "bizNo required")
    @Length(max = 200, message = "bizNo max length is 200")
    private String bizNo;

    /**
     * 操作人
     */
    @NotBlank(message = "operator required")
    @Length(max = 64, message = "operator max length 64")
    private String operator;

    /**
     * 日志内容
     */
    @NotBlank(message = "detail required")
    @Length(max = 500, message = "detail max length 500")
    private String detail;

    /**
     * 计数使用
     */
    private Integer count;

    /**
     * 标记为失败或成功
     */
    private String flag;

    /**
     * 日志的额外信息
     */
    private String extra;

    /**
     * 日志的创建时间
     */
    private Date createTime;

    /**
     * 打印日志的代码信息
     * CodeVariableType 日志记录的ClassName、MethodName
     */
    private Map<CodeVariableType, Object> codeVariable;
}
