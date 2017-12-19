<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="my" uri="/WEB-INF/tld/myutil.tld"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="head.jsp"%>
<div id="Main">
    <div class="sep20"></div>
    <div class="box" id="box">
        <div class="cell"><a href="${path}/">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 对话</div>
        <c:if test="${not empty problems}">
            <%@include file="problem.jsp"%>
        </c:if>
        <form method="post" action="${path}/publish_message" id="compose">
            <input name="formToken" value="${formToken}" hidden>
            <input name="to" hidden value="${with}">
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
            <a href="/${path}">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 对话
        </div>

        <c:forEach items="${messages}" var="message">
            <div class="cell" id="n_6865239">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <tbody>
                    <tr>
                        <td width="32" align="left" valign="top">
                            <a href="${path}/user/${message.from.id}">
                                <img
                                        src="${path}/${message.from.iconUrl}"
                                        height="48px" width="48px"
                                        class="avatar" border="0" align="default">
                            </a>
                        </td>
                        <td valign="middle">
                        <span class="fade">
                            <a href="${path}/user/${message.from.id}"><strong>${message.from.name}</strong></a>
                            :
                        </span> &nbsp;
                            <span class="snow">${my:dateFrom(message.sendDate)}</span>
                            &nbsp;
                            <div class="sep5"></div>
                            <div class="payload">
                                    ${message.content}
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