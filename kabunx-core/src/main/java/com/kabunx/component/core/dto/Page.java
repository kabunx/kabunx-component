package com.kabunx.component.core.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用分页信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(title = "分页信息")
public class Page implements Serializable {
    private int page = 1;

    private int pageSize = 20;

    public int getFrom() {
        return (page - 1) * pageSize;
    }

    public int getNextPageSize() {
        return pageSize + 1;
    }
}
