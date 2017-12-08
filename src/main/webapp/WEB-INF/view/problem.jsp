<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/29
  Time: 16:47
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="problem">请解决以下问题然后再提交：
    <ul>
       <c:forEach items="${problems}" var="problem">
           <li>
               ${problem.value}&nbsp;&nbsp;
               <strong>${problem.reason}</strong>
           </li>
       </c:forEach>
    </ul>
</div>