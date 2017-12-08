<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/30
  Time: 13:46
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="head.jsp"%>
        <div id="Main">
            <div class="sep20"></div>
            <div class="box">
                <div class="header"><a href="/">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 登录 &nbsp;<li class="fa fa-lock"></li></div>
                <c:if test="${not empty problems}">
                    <%@include file="problem.jsp"%>
                </c:if>
                <div class="cell">
                    <form method="post" action="${path}/do_login">
                        <table cellpadding="5" cellspacing="0" border="0" width="100%">
                            <tbody><tr>
                                <td width="120" align="right">用户名</td>
                                <td width="auto" align="left"><input type="text" class="sl" name="username" value="" autofocus="autofocus" autocorrect="off" spellcheck="false" autocapitalize="off" placeholder="用户名或电子邮箱地址"></td>
                            </tr>
                            <tr>
                                <td width="120" align="right">密码</td>
                                <td width="auto" align="left"><input type="password" class="sl" name="password" value="" autocorrect="off" spellcheck="false" autocapitalize="off"></td>
                            </tr>
                            <tr>
                                <td width="120" align="right"></td>
                                <td width="auto" align="left"><input type="hidden" value="19789" name="once"><input type="submit" class="super normal button" value="登录"></td>
                            </tr>
                            <tr>
                                <td width="120" align="right"></td>
                                <td width="auto" align="left"><a href="/forgot">我忘记密码了</a></td>
                            </tr>
                            </tbody></table>

                        <input type="hidden" value="${formToken}" name="formToken">

                    </form>
                </div>

            </div>
        </div>


<%@include file="bottom.jsp"%>
<script src="${path}/static/js/jquery-3.2.1.js" type="text/javascript"></script>
</body>
</html>