package service.impl;

import dao.WorkStudyDao;
import dao.impl.WorkStudyDaoImpl;
import domain.PageBean;
import domain.WorkStudyInfo;
import redis.clients.jedis.Jedis;
import service.WorkStudyService;
import util.utils.JedisUtil;
import util.utils.SerializeUtil;

import java.util.ArrayList;
import java.util.List;

public class WorkStudyServiceIml implements WorkStudyService {

    // 将dao作为service层的属性
    private WorkStudyDao dao = new WorkStudyDaoImpl();


    // 设计一个方法   redis/mysql的查询，返回一页的信息

    @Override
    public PageBean<WorkStudyInfo> showOnePageInfo(int currentPage, int pageSize,String content) {
        // 先定义要返回的对象
        PageBean<WorkStudyInfo> page ;
        // 一、得出page的所有属性（5个），并封装page
        // （1）（2）已经有了currentPage和pageSize
        // （3） 计算totalCount
        int totalCount = dao.getTotalCount(content);
        // （4） 计算beginIndex     beginIndex = （当前页数-1）*每页显示条数    从1开始，而不是0
        int beginIndex = (currentPage - 1) * pageSize;
        // （5） 得到list<WorkStudyInfo>    （下面得到）
        List<WorkStudyInfo> list;

        list = dao.showOnePageInfo(beginIndex,pageSize,content);



/*        // 要计算出beginIndex传给dao的方法，还要计算出totalPage封装给
        // 1. 获取jedis （jedis是Java访问redis数据库的api）
        Jedis jedis = JedisUtil.getJedis();
        // 2.查询list
        String keyName = "list"+currentPage;
        List<String> listString = jedis.lrange(keyName, 0, -1);
        // 3. 判断
        if (listString == null || listString.size() == 0) {
            System.out.println("从数据库中查询数据并保存数据到redis中");

            // 情况一：redis中没有
            // 4. 先从数据库查
            list = dao.showOnePageInfo(beginIndex,pageSize,content);
            // 5. 然后将信息存入redis
            //   redis：   第1页的内容存入key = list1;第2页存入key = list2;以此类推
            for (WorkStudyInfo info : list) {
                // 注意：jedis.lpush(param1,param2)中的两个参数类型要相同—————同为String或者byte[]（可以看源码）
                jedis.lpush(keyName.getBytes(), SerializeUtil.serialize(info));
            }
        } else {
            System.out.println("从redis中查询");

            // 情况二：redis中有
            list = new ArrayList<>();
            List<byte[]> result = jedis.lrange(keyName.getBytes(), 0, -1);
            System.out.println("这是"+keyName+"的大小" + result.size());

            for (byte[] info : result) {
                WorkStudyInfo workStudyInfo = (WorkStudyInfo) SerializeUtil.unSerialize(info);
                list.add(workStudyInfo);
            }
        }*/

        // (5)将最后一个list属性封装给page

        page = new PageBean<>(currentPage, pageSize, 0, totalCount, list);

        // （6）设置totalPage
        // 巧用条件运算符   --->    ? :
        int totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
        page.setTotalPage(totalPage);
        System.out.println("这是totalPage = "+totalPage);

        page.setTotalPage(totalPage);

        return page;
    }

/*    @Test
    public void test() {
        PageBean<WorkStudyInfo> page = this.showOnePageInfo(2, 5);
        System.out.println("page的大小= " + page.getList().size() + "page的beginIndex=" + page.getList().size());
    }*/
}
