package com.newcoder.communitydemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Page {
    // 当前页码
    private int current = 1;
    // 显示上限
    private int limit = 10;
    // 数据总数
    private int rows;
    // 查询路径（复用分页的连接
    private String path;

    public int getOffset() {
        return (current - 1) * limit;
    }

    public int getTotal() {
        return rows % limit == 0 ? rows / limit : rows / limit + 1;
    }

    public int getFrom() {
        int from = current - 2;
        return from < 1 ? 1 : from;
    }

    public int getTo() {
        int to = current + 2;
        int total = getTotal();
        return to > total ? total : to;
    }

}
