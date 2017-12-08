<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div id="Rightbar">
    <div class="sep20"></div>
    <c:choose>
        <c:when test="${not empty currentUser}">
            <%@include file="user_inform_box.jsp"%>
        </c:when>
        <c:otherwise>
            <%@include file="login_box.jsp"%>
        </c:otherwise>
    </c:choose>
    <div class="sep20"></div>
    <%@include file="ad.jsp"%>
    <div class="sep20"></div>
    <%@include file="hottopic.jsp"%>
    <div class="sep20"></div>
    <%@include file="runstate.jsp"%>
</div>
