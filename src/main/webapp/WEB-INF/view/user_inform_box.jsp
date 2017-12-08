<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/30
  Time: 11:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="box">
    <div class="cell">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tbody><tr>
                <td width="48" valign="top"><a href="/user/${currentUser.id}"><img src="${path}/${currentUser.iconUrl}" class="avatar" border="0" align="default" style="max-width: 48px; max-height: 48px;"></a></td>
                <td width="10" valign="top"></td>
                <td width="auto" align="left"><span class="bigger"><a href="${path}/user/${currentUser.id}">${currentUser.name}</a></span>

                </td>
            </tr>
            </tbody></table>
        <div class="sep10"></div>
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tbody><tr>
                <td width="33%" align="center"><a href="${path}/my/nodes" class="dark" style="display: block;"><span class="bigger">0</span><div class="sep3"></div><span class="fade">节点收藏</span></a></td>
                <td width="34%" style="border-left: 1px solid rgba(100, 100, 100, 0.4); border-right: 1px solid rgba(100, 100, 100, 0.4);" align="center"><a href="${path}/my/topics" class="dark" style="display: block;"><span class="bigger">0</span><div class="sep3"></div><span class="fade">主题收藏</span></a></td>
                <td width="33%" align="center"><a href="${path}/my/following" class="dark" style="display: block;"><span class="bigger">0</span><div class="sep3"></div><span class="fade">特别关注</span></a></td>
            </tr>
            </tbody></table>
    </div>
    <div class="cell">
        <div style="width: 250px; background-color: #f0f0f0; height: 3px; display: inline-block; vertical-align: middle;"><div style="width: 8px; background-color: #ccc; height: 3px; display: inline-block;"></div></div>
    </div>

    <div class="cell" style="padding: 5px;">
        <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tbody><tr>
                <td width="32"><a href="/new"><img src="${path}/static/img/flat_compose.png" width="32" border="0"></a></td>
                <td width="10"></td>
                <td width="auto" valign="middle" align="left"><a href="${path}/edit_question">创作新主题</a></td>
            </tr>
            </tbody></table>
    </div>
    <div class="inner"><a href="/notifications" class="fade">0 条未读提醒</a></div>

</div>
