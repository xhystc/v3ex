<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/29
  Time: 9:17
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<head>
    <meta name="Content-Type" content="text/html;charset=utf-8" />
    <meta name="Referrer" content="unsafe-url" />
    <meta content="True" name="HandheldFriendly" />
    <meta name="theme-color" content="#333344" />

    <title>V2EX</title>
    <link rel="stylesheet" type="text/css" media="screen" href="${path}/static/css/basic.css" />

    <link rel="stylesheet" type="text/css" media="screen" href="${path}/static/css/style.css" />
    <link rel="stylesheet" type="text/css" media="screen" href="${path}/static/css/desktop.css" />
    <link rel="icon" sizes="192x192" href="${path}/static/img/v2ex_192.png" />
    <link rel="shortcut icon" href="${path}/static/img/icon_rayps_64.png" type="image/png" />
    <link rel="stylesheet" type="text/css" href="${path}/static/css/font-awesome.min.css" />
    <script>
        var path = '${path}'
    </script>
    <script src="${path}/static/js/vote.js"  type="text/javascript"></script>
</head>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html lang="zh-CN">
<body>
<div id="Top">
    <div class="content">
        <div style="padding-top: 0px;">
            <table cellpadding="0" cellspacing="0" border="0" width="100%">
                <tr>
                    <td width="110" align="left">
                        <a href="${path}/index" name="top" title="way to explore">
                        <img src="${path}/static/img/v2ex.png" border="0" align="default" alt="V2EX" width="94" height="30" />
                      </a>
                    </td>
                    <td width="auto" align="left">
                        <div id="Search">
                            <form action="https://www.google.com" onsubmit="return dispatch()" target="_blank" style="padding-top: 6px">
                                <div style="width: 276px; height: 28px; background-size: 276px 28px;
                                        background-image: url('${path}/static/img/qbar_light@2x.png');
                                        background-repeat: no-repeat; display: inline-block;">
                                    <input type="text" maxlength="40" name="q" id="q" value="" />
                                </div>
                            </form>
                        </div>
                    </td>
                    <c:choose>
                        <c:when test="${empty currentUser}">
                            <td width="570" align="right" style="padding-top: 2px;"><a href="/" class="top">首页</a>&nbsp;&nbsp;&nbsp;
                                <a href="${path}/regist" class="top">注册</a>&nbsp;&nbsp;&nbsp;
                                <a href="${path}/login" class="top">登录</a>
                            </td>
                        </c:when>
                        <c:otherwise>
                            <td width="570" align="right" style="padding-top: 2px;">
                                <a href="${path}/" class="top">首页</a>&nbsp;&nbsp;&nbsp;
                                <a href="${path}/user/${currentUser.id}" class="top">${currentUser.name}</a>&nbsp;&nbsp;&nbsp;
                                <a href="" class="top">记事本</a>&nbsp;&nbsp;&nbsp;
                                <a href="" class="top">时间轴</a>&nbsp;&nbsp;&nbsp;
                                <a href="" class="top">设置</a>&nbsp;&nbsp;&nbsp;
                                <a href="#;" onclick="if (confirm('确定要从 V2EX 登出？')) { location.href= '${path}/logout'; }" class="top">登出</a>
                            </td>
                        </c:otherwise>
                    </c:choose>
                </tr>
            </table>
        </div>
    </div>
</div>
<div id="Wrapper">
    <div class="content">