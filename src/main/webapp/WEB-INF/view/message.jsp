<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="/WEB-INF/tld/myutil.tld"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="head.jsp"%>
<div id="Main">
    <div class="sep20"></div>
    <div class="box" id="box">
        <div class="cell"><a href="${path}/">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 发送消息</div>
        <c:if test="${not empty problems}">
            <%@include file="problem.jsp"%>
        </c:if>
        <form method="post" action="${path}/publish_message" id="compose">
            <input name="formToken" value="${formToken}" hidden>
            <div class="cell"><div class="fr fade" id="title_remaining">120</div>
                发送给：
            </div>
            <div class="cell" style="padding: 0px; background-color: #fff;">
                <input type="text" class="msl" rows="1" maxlength="120" id="topic_title" name="to" autofocus="autofocus">
            </div>
            <div class="cell"><div class="fr fade" id="content_remaining" maxlength="200">200</div>
                正文
            </div>
            <div class="cell">
                <textarea name="content" style="resize: none; width: 753px;height:100px;"></textarea>
            </div>
            <div class="cell">
                <div class="fr">
                    <input type="submit" class="super normal button" value="发送"> &nbsp;</input>
                </div>

            </div>
        </form>

    </div>
    <div class="sep20"></div>
<div class="box">
    <div class="header">
        <a href="/${path}">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 私信
    </div>

    <c:forEach items="${conversations}" var="conversation">
        <div class="cell" id="n_6865239">
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tbody>
                <tr>
                    <td width="32" align="left" valign="top">
                        <a href="${path}/user/${conversation.lastMessage.from.id}">
                        <img
                            src="${path}/${conversation.lastMessage.from.iconUrl}"
                            height="48px" width="48px"
                            class="avatar" border="0" align="default">
                        </a>
                    </td>
                    <td valign="middle">
                        <span class="fade">
                            <a href="${path}/user/${conversation.lastMessage.from.id}"><strong>${conversation.lastMessage.from.name}</strong></a>
                            :
                        </span> &nbsp;
                        <span class="snow">${my:dateFrom(conversation.lastMessage.sendDate)}</span>
                        &nbsp;
                        <a href="${path}/conversation?conversationId=${conversation.id}" onclick="deleteNotification(6865239, 49048)" class="node">查看</a>
                        <div class="sep5"></div>
                        <div class="payload">
                                ${conversation.lastMessage.content}
                        </div>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </c:forEach>

</div>
</div>


    <%@include file="bottom.jsp"%>
    <script src="${path}/static/js/jquery-3.2.1.js" type="text/javascript"></script>
    </body>
    </html>