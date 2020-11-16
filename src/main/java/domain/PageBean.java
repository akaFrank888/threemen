package domain;

import java.util.List;

/**

    分页的对象

 */


public class PageBean<T> {

    // PageBean对象中的list属性是泛型，所以定义该类为泛型

    // 需要从前端传参的属性是：currentPage（当前页码），pageSize（每页显示条数）

    private int currentPage;
    private int pageSize;

    // 计算得出的属性：totalPage（总页数）

    private int totalPage;

    // 从数据库查询得出的属性：totalCount（总记录数），list（每页显示的数据集合）

    private int totalCount;
    private List<T> list;

    public PageBean() {

    }
    public PageBean(int currentPage, int pageSize, int totalPage, int totalCount, List<T> list) {
        this.currentPage = currentPage;
        this.pageSize = pageSize;
        this.totalPage = totalPage;
        this.totalCount = totalCount;
        this.list = list;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }
}
