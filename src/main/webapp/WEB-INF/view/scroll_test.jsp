<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 16:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Document</title>
    <script type="text/javascript" src="${path}/static/js/jquery-3.2.1.js"></script>
    <style type="text/css">
        body{
            height: 2000px;
        }
    </style>
</head>
<body>

</body>
<script type="text/javascript">
    $(document).on("scroll", function () {
        var pageHeight = Math.max(document.body.scrollHeight, document.body.offsetHeight);
        var viewportHeight = window.innerHeight || document.documentElement.clientHeight || document.body.clientHeight || 0;
        var scrollHeight = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop || 0;
        if(pageHeight - viewportHeight - scrollHeight <=0){
            alert("hh");
        }
    });
</script>
</html>