<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/7
  Time: 18:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
    ${formToken}
    <c:if test='${question.isVoted}'>
        voted
    </c:if>
</body>
</html>
