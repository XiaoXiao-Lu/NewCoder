package com.kinnon.domain;

import lombok.Data;

/**
 * @author Kinnon
 * @create 2022-08-03 0:36
 */
@Data
public class Page {
    /**
     * 当前页数
     */
    private int current = 1;
    /**
     * 显示上限
     */
    private int limit = 10;

    /**
     * 数据总数，用作计算页数
     */
    private int rows;

    /**
     * 链接路径
     */
    private String path;

    /**
     * 获取当前页起始行
     * @return
     */
    public int getOffset() {
        return ( current -1) * limit;
    }

    /**
     * 计算总页数
     * @return
     */
    public int getTotal() {
        int i = rows / limit;
        if (rows % limit == 0){
            return i;
        }else{
            return i + 1;
        }

    }

    /**
     * 获取起始页
     * @return
     */
    public int getFrom(){
        int from = current - 2 ;
        return from < 1 ? 1:from;
    }

    /**
     * 获取结束页
     * @return
     */
    public int getTo(){
        int to = current + 2;
        int total = getTotal();
        return to > total ? total:to;
    }


    /**
     * 获取页面显示的页数
     * @return
     */
    public int getCurrent() {
        return current;
    }

    /**
     * 设置页面显示的页数
     * @param current
     */
    public void setCurrent(int current) {
        if (current >=1){
            this.current = current;

        }
    }

    /**
     * 获取显示上限
     * @return
     */
    public int getLimit() {
        return limit;
    }

    /**
     * 设置显示上限
     * @param limit
     */
    public void setLimit(int limit) {
        if (limit >= 1 && limit < 100){
            this.limit = limit;

        }
    }

    /**
     * 获取数据总数
     * @return
     */
    public int getRows() {
        return rows;
    }

    /**
     * 设置数据总数
     * @param rows
     */
    public void setRows(int rows) {
        if (rows >= 0){

            this.rows = rows;
        }
    }
}
