<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="my" uri="/WEB-INF/tld/myutil.tld"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach items="${commentPage.comments}" var="comment">
    <div id="comment-item-${comment.id}" class="cell">
        <table width="100%" cellspacing="0" cellpadding="0" border="0">
            <tbody><tr>
                <td align="center" width="48" valign="top">
                    <img src="${path}/${comment.user.iconUrl}" class="avatar" align="default" border="0" style="width: 48px;height: 48px"  >
                </td>
                <td width="10" valign="top"></td>
                <td align="left" width="auto" valign="top">
                    <div class="fr">
                        <div id="thank_area_5031457" class="thank_area">
                            <a href="#;" onclick="dovote('comment',${comment.id})" class="thank">支持</a>
                        </div> &nbsp;
                        <a href="#;" onclick="replyOne('EmmaYang');"><img src="//cdn.v2ex.com/static/img/reply.png" alt="Reply" align="absmiddle" border="0"></a> &nbsp;&nbsp;
                        <span  id="comment-item-agree-${comment.id}" class="no
                         <c:if test='${comment.isVoted}'>
                             voted
                         </c:if>
                        ">${comment.voteCount}
                        </span>
                    </div>
                    <div class="sep3"></div>
                    <strong><a href="/user/${comment.user.id}" class="dark">${comment.user.name}</a></strong>&nbsp; &nbsp;
                    <span class="ago">${my:dateFrom(comment.sendDate)}</span>
                    <div class="sep5"></div>
                    <div class="reply_content">${comment.content}</div>
                </td>
            </tr>
            </tbody></table>
    </div>
</c:forEach>