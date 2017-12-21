<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:48
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="my" uri="/WEB-INF/tld/myutil.tld"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="cell">
    <div class="fr" style="margin: -3px -5px 0px 0px;">
        <a href="${path}/index?tagId=${commentPage.tag.id}" class="tag">
            <li class="fa fa-tag"></li>
            ${commentPage.tag.name}</a>
    </div>
    <span class="gray">${commentPage.question.commentInform.commentCount} 回复 &nbsp;<strong class="snow">|</strong> &nbsp;
        ${my:dateFrom(commentPage.question.commentInform.lastCommentTime)} 被顶</span>
</div>
