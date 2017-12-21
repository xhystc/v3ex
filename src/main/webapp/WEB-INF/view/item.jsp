<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/29
  Time: 9:27
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="my" uri="/WEB-INF/tld/myutil.tld"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<c:forEach items="${questions}" var="question">
    <div class="cell item" style="">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
                <td width="48" valign="top" align="center">
                    <a href="${path}/user/${question.user.id}">
                        <img src="${path}/${question.user.iconUrl}" class="avatar" border="0" align="default" height="48px" width="48px"  />
                    </a>
                </td>
                <td width="10"></td>
                <td width="auto" valign="middle">
                <span class="item_title">
                     <a href="${path}/q/${question.id}">${question.title}</a>
                </span>
                    <div class="sep5"></div>
                    <span class="small fade">
                        <div id="topic_410683_votes" class="votes
            <c:if test='${question.isVoted}'>
                voted
            </c:if>
         ">
        <a href="javasrcipt:"
           onclick="dovote('question',${question.id})"
           class="vote" id="vote-button-question-${question.id}">^&nbsp;${question.voteCount}</a>
    </div> &nbsp;
                    <strong>
                        <a href="/user/${question.user.id}">${question.user.name}</a>
                    </strong> &nbsp;•&nbsp; ${my:dateFrom(question.createDate)}
                    </span>
                </td>
                <td width="70" align="right" valign="middle">
                    <a href="${path}/q/${question.id}" class="count_livid">${question.commentInform.commentCount}</a>
                </td>
            </tr>
        </table>
    </div>
</c:forEach>