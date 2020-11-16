<%--
  Created by IntelliJ IDEA.
  User: yxl15
  Date: 2020/10/7
  Time: 16:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<form action="userInfo/showImg" method="post" enctype="multipart/form-data">
    用户名：<input type="text" name="username" /> <br>
    头像：<input type="file" name="photo" /> <br>
    <input type="submit" value="上传"/>


</form>
</body>
</html>
