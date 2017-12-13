<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="cell">
    <div class="fr" style="margin: -3px -5px 0px 0px;">
        <c:forEach items="${commentPage.tags}" var="tag">
            <a href="${path}/tag/${tag.id}" class="tag">
            <li class="fa fa-tag"></li>
            ${tag.name}</a>
        </c:forEach>
    </div>
    <span class="gray">${commentPage.question.commentCount} 回复 &nbsp;<strong class="snow">|</strong> &nbsp; ${commentPage.question.activeTimeShowString} 被顶</span>
</div>
