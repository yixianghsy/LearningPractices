package com.mall.pojo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EsPage<T> {

    /**
     * 当前页（不要）
     */
    private int currentPage;
    /**
     * 每页显示多少条（不要）
     */
    private int pageSize;
    /**
     * 总记录数
     */
    private int recordCount;
    /**
     * 本页的数据列表
     */
    private List<T> itemList;
    /**
     * 总页数
     */
    private int totalPages;
    /**
     * 页码列表的开始索引（包含）（不要）
     */
    private int beginPageIndex;
    /**
     * 页码列表的结束索引（包含）（不要）
     */
    private int endPageIndex;

    /**
     * 只接受前4个必要的属性，会自动的计算出其他3个属性的值
     * @param currentPage
     * @param pageSize
     * @param recordCount
     * @param itemList
     */
    public EsPage(int currentPage, int pageSize, int recordCount, List<T>  itemList) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.recordCount = recordCount;
        this.itemList = itemList;
        // 计算总页码
        totalPages = (recordCount + pageSize - 1) / pageSize;
        // 计算 beginPageIndex 和 endPageIndex
        // 总页数不多于10页，则全部显示
        if (totalPages <= 10) {
            beginPageIndex = 1;
            endPageIndex = totalPages;
        }
        // 总页数多于10页，则显示当前页附近的共10个页码
        else {
            // 当前页附近的共10个页码（前4个 + 当前页 + 后5个）
            beginPageIndex = currentPage - 4;
            endPageIndex = currentPage + 5;
            // 当前面的页码不足4个时，则显示前10个页码
            if (beginPageIndex < 1) {
                beginPageIndex = 1;
                endPageIndex = 10;
            }
            // 当后面的页码不足5个时，则显示后10个页码
            if (endPageIndex > totalPages) {
                endPageIndex = totalPages;
                beginPageIndex = totalPages - 10 + 1;
            }
        }
    }

}
