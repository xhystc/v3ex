<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="box">
    <c:if test="${not empty problems}">
        <%@include file="/WEB-INF/view/problem.jsp"%>
    </c:if>
    <div class="cell">
        <div class="fr"><a href="#" onclick="goTop()"><strong>↑</strong> 回到顶部</a></div>
        添加一条新回复
    </div>
    <div class="cell">
        <form method="post" action="${path}/do_comment">
            <textarea name="content" maxlength="10000" class="mll" id="reply_content"></textarea>
            <div class="sep10"></div>
            <div class="fr">
                <div class="sep5"></div>
                <span class="gray">请尽量让自己的回复能够对别人有帮助</span></div>
            <input type="hidden" value="${formToken}" name="formToken"/>
            <input type="hidden" value="${commentPage.question.id}" name="questionId"/>
            <input type="submit" value="回复" class="super normal button"/>
        </form>
    </div>
    <div class="inner">
        <div class="fr"><a href="/">← V2EX</a></div>
        &nbsp;
    </div>
</div>