<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:51
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<c:forEach items="${commentPage.comments}" var="comment">
    <div id="r_5031457" class="cell">
        <table width="100%" cellspacing="0" cellpadding="0" border="0">
            <tbody><tr>
                <td align="center" width="48" valign="top">
                    <img src="${path}/${comment.user.iconUrl}" class="avatar" align="default" border="0">
                </td>
                <td width="10" valign="top"></td>
                <td align="left" width="auto" valign="top">
                    <div class="fr"><div id="thank_area_5031457" class="thank_area">
                        <a href="#;" onclick="if (confirm('确认花费 10 个铜币向 @EmmaYang 的这条回复发送感谢？')) { thankReply(5031457, 'hnqqkodjbmnmhdrlcxbdflluaasqmrxo'); }" class="thank">点赞</a></div> &nbsp;
                        <a href="#;" onclick="replyOne('EmmaYang');"><img src="//cdn.v2ex.com/static/img/reply.png" alt="Reply" align="absmiddle" border="0"></a> &nbsp;&nbsp;
                        <span class="no">${comment.agree}</span>
                    </div>
                    <div class="sep3"></div>
                    <strong><a href="/user/${comment.user.id}" class="dark">${comment.user.name}</a></strong>&nbsp; &nbsp;
                    <span class="ago">${comment.sendDateShowString}</span>
                    <div class="sep5"></div>
                    <div class="reply_content">${comment.content}</div>
                </td>
            </tr>
            </tbody></table>
    </div>
</c:forEach>