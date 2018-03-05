<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/29
  Time: 9:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="head.jsp"%>
        <div id="Rightbar">
            <div class="sep20"></div>
            <%@include file="login_box.jsp"%>
        </div>
        <div id="Main">
            <div class="sep20"></div>

            <div class="box">
                <div class="header"><a href="/">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 注册</div>

                <c:if test="${not empty problems}">
                   <%@include file="problem.jsp"%>
                </c:if>
                <div class="inner">
                    <form method="post" action="${path}/do_regist" onsubmit="">
                        <input type="text" hidden value="${formToken}" name="formToken">
                        <table cellpadding="5" cellspacing="0" border="0" width="100%">
                            <tr>
                                <td width="120" align="right" valign="top"><div class="sep5"></div>用户名</td>
                                <td width="auto" align="left"><input type="text" class="sls" name="username"  autocomplete="off" autocorrect="off" spellcheck="false" autocapitalize="off" required="required" /><div class="sep5"></div><span class="fade">用户名长度应大于3位小于28位</span></td>
                            </tr>
                            <tr>
                                <td width="120" align="right">密码</td>
                                <td width="auto" align="left"><input type="password" class="sls" name="password" autocomplete="new-password" autocorrect="off" spellcheck="false" autocapitalize="off" required="required" /><div class="sep5"></div><span class="fade">密码长度应大于6位小于28位</span></td>
                            </tr>
                            <tr>
                                <td width="120" align="right">重复密码</td>
                                <td width="auto" align="left"><input type="password" class="sls" autocomplete="new-password" autocorrect="off" spellcheck="false" autocapitalize="off" required="required" /></td>
                            </tr>
                            <tr>
                                <td width="120" align="right" valign="top"><div class="sep5"></div>电子邮件</td>
                                <td width="auto" align="left"><input type="email" class="sls" name="email" autocorrect="off" spellcheck="false" autocapitalize="off" required="required" />
                                    <div class="sep5"></div>
                                    <span class="fade">请使用真实电子邮箱注册，我们不会将你的邮箱地址分享给任何人</span>
                                </td>
                            </tr>

                            <tr>
                                <td width="120" align="right"></td>
                                <td width="auto" align="left">
                                    <input type="submit" class="super normal button" value="注册" />
                                </td>
                            </tr>
                        </table>
                    </form>
                </div>
            </div>

        </div>

<%@include file="bottom.jsp"%>
<script src="${path}/static/jquery-3.2.1.js" type="text/javascript"></script>
</body>
</html>