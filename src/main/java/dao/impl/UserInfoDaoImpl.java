package dao.impl;

import dao.BaseDao;
import dao.UserInfoDao;
import domain.User;
import domain.UserInfo;

import java.io.*;
import java.util.Base64;

public class UserInfoDaoImpl extends BaseDao implements UserInfoDao {
/*    @Override
    public boolean saveAfterRegister(User user) {

        boolean flag = false;

        int i = this.update("insert user_Info values(null,?,?,null,?,null,null,null,null,null)",
                user.getAccount(), user.getEmail(), user.getNickname());

        if (i > 0) {
            flag = true;
        }
        return flag;*/


        /*boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "insert user_Info values(null,?,?,null,?,null,null,null,null,null)";
            // 4. 执行
            int i = query.update(connection, sql, user.getAccount(), user.getEmail(), user.getNickname());
            // 5. 判断
            if (i > 0) {
                flag = true;
                System.out.println("已将账户为：" + user.getAccount() + "的信息更新进user_info表！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }*/



    @Override
    public UserInfo showUserInfo(String token) {

        return this.queryForOne(UserInfo.class, "select " +
                "user_department as department, " +
                "user_master as master, " +
                "user_address as address, " +
                "user_phone as phone " +
                "from user_info where user_token=?", token);
    }

    @Override
    public User showUserInfoElse(String token) {
        return this.queryForOne(User.class, "select " +
                "user_account as account, " +
                "user_nickname as nickname, " +
                "user_email as email " +
                "from user_register where user_key=?", token);
    }

    @Override
    public String showImg(String token) {

        String sql = "select user_img from threemen.user_info where user_token=?";

        Object result = this.queryForSingleValue(sql, token);
        if (result != null) {
            return result.toString();
        } else {
            return null;
        }

    }

    @Override
    public boolean saveInfo(UserInfo info) {

        boolean flag = false;
        String sql = "update user_info set user_department=?,user_master=?,user_address=?,user_phone=? where user_token=?";

        int i = this.update(sql, info.getDepartment(), info.getMaster(), info.getAddress(), info.getPhone(), info.getToken());
        if (i > 0) {
            flag = true;
        }
        return flag;

/*        boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "insert user_info(user_id,user_department,user_master,user_address) values(?,?,?,?)";
            // 4. 执行
            int i = query.update(connection, sql, null, info.getDepartment(), info.getMaster(), info.getAddress());
            // 5. 判断
            if (i > 0) {
                flag = true;
                System.out.println("账户为：" + info.getAccount() + "的一条记录插入成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/
    }

    @Override
    public boolean alterNickname(String nickname,String token) {
        boolean flag = false;
        int i = this.update("update user_register set user_nickname=? where user_key=?", nickname, token);
        if (i > 0) {
            flag = true;
        }
        return flag;
    }

    @Override
    public boolean saveImg(String token, InputStream fis) {

        boolean b = false;

        try {
            // 创建byte[]缓存
            // fis.available()    在IO操作之前得知数据流里有多少字节可以读取
            byte[] bytes = new byte[fis.available()];
            fis.read(bytes);

            // 将byte[]数组转成String
            Base64.Encoder encoder = Base64.getEncoder();
            String result = encoder.encodeToString(bytes);

            // 将 Base64编码 后的字符串存入数据库中
            int i = this.update("update user_info set user_img=? where user_token=?", result, token);
            if (i > 0) {
                b = true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return b;








        /*boolean flag = false;
        Connection conn = null;
        try {
            conn = JDBCUtil.getConnection();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int len = -1;
            byte[] buf = new byte[1024];
            while(true) {
                try {
                    if ((len = fis.read(buf)) == -1) break;
                } catch (IOException e) {
                    e.printStackTrace();
                }//汇总字节流到内存
                baos.write(buf, 0, len);
            }

            byte[] bytes = baos.toByteArray();//从内存取出字节流数组
        */


/*            Blob pic = conn.createBlob();
            pic.setBytes(1, bytes);//把字节流设置给blob类
            String sql = "update threemen.user_info set user_img=? where user_token=?";
            PreparedStatement ppst = conn.prepareStatement(sql);
            ppst.setBlob(1, pic);
            ppst.setString(2, token);
            flag = ppst.execute();
            System.out.println("这是！！！"+flag);*/
/*        } catch (SQLException e) {
            e.printStackTrace();
        }*/







/*        boolean flag = false;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = -1;
        byte[] buf = new byte[1024];
        while(true) {
            try {
                if ((len = inputStream.read(buf)) == -1) break;
            } catch (IOException e) {
                e.printStackTrace();
            }//汇总字节流到内存
            baos.write(buf, 0, len);
        }
        try {
            baos.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] bytes = baos.toByteArray();//从内存取出字节流数组

        Connection conn = null;
        PreparedStatement ppst = null;
        Blob pic = null;

        *//*String sql = "insert into threemen.user_info(user_img) values (?) where user_token=?";*//*
        String sql = "update threemen.user_info set user_img=? where user_token=?";
        try {

            conn = JDBCUtil.getConnection();
            ppst = conn.prepareStatement(sql);
            pic = conn.createBlob();
            pic.setBytes(1, bytes);//把字节流设置给blob类

            ppst.setBlob(1, pic);
            ppst.setString(2, token);
            int i = ppst.executeUpdate();
            if (i > 0) {
                flag = true;
            }
            ppst.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/

/*
        int i = this.update("insert into user_info(user_img) values(?) where user_token=?", pic, token);
*/



/*        boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "insert user_info(user_img) values(?) where user_account = ?";
            // 4. 执行
            int i = query.update(connection, sql, inputStream, account);
            // 5. 判断
            if (i > 0) {
                flag = true;
                System.out.println("一张img插入成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/
    }

    @Override
    public boolean updatePassword(String token, String newPassword) {

        boolean flag = false;
        int i = this.update("update user_register set user_password = ? where user_key = ?", newPassword, token);
        if (i > 0) {
            flag = true;
        }
        return flag;

/*        boolean flag = false;
        try {
            // 1. 创建QueryRunner对象
            QueryRunner query = new QueryRunner();
            // 2. 获得连接
            Connection connection = JDBCUtil.getConnection();
            // 3. 定义sql
            String sql = "update user_info set user_password = ? where user_account = ?";
            // 4. 执行
            int i = query.update(connection, sql, newPassword, account);
            // 5. 判断
            if (i > 0) {
                flag = true;
                System.out.println("用户密码修改成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;*/

    }


    // 但只需要userInfo里面的address和phone
    @Override
    public UserInfo getUserInfoByToken(String token) {
        UserInfo info = null;
        info = this.queryForOne(UserInfo.class, "select " +
                "user_address as address, " +
                "user_phone as phone " +
                "from user_info where user_token=?", token);
        return info;

    }
}
