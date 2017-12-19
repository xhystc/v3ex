<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="header">
    <div class="fr"><a href="${path}/user/${question.user.id}"><img
            src="${path}/${commentPage.question.user.iconUrl}"
            class="avatar" border="0" align="default" width="48" height="48"/></a></div>
    <a href="${path}">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> <a href="${path}/tag/jobs">酷工作</a>
    <div class="sep10"></div>
    <h1>${commentPage.question.title}
    <small class="gray"></h1>
        <div id="topic_410683_votes" class="votes
            <c:if test='${commentPage.question.isVoted}'>
                voted
            </c:if>
         ">
            <a href="javasrcipt:"
               onclick="dovote('question',${commentPage.question.id})"
               class="vote" id="vote-button-question-${commentPage.question.id}">^&nbsp;${commentPage.question.voteCount}</a>
        </div> &nbsp;
        <a href="${path}/user/${commentPage.question.user.id}">
            ${commentPage.question.user.name}
        </a> · ${commentPage.question.createDateShowString} 发布· ${commentPage.question.commentInform.commentCount} 次回复 &nbsp;</small>
</div>
