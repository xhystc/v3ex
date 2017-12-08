<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/30
  Time: 14:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="head.jsp"%>
<div id="Rightbar">
    <div class="sep20"></div>
    <%@include file="question_tip.jsp"%>
    <%@include file="community_rule.jsp"%>
</div>
<div id="Main">
    <div class="sep20"></div>
    <div class="box" id="box">
        <div class="cell"><a href="${path}/">V2EX</a> <span class="chevron">&nbsp;›&nbsp;</span> 创作新主题</div>
        <c:if test="${not empty problems}">
            <%@include file="problem.jsp"%>
        </c:if>
        <form method="post" action="${path}/publish" id="compose">
            <div class="cell"><div class="fr fade" id="title_remaining">120</div>
                主题标题
            </div>
            <div class="cell" style="padding: 0px; background-color: #fff;"><textarea class="msl" rows="1" maxlength="120" id="topic_title" name="title" autofocus="autofocus" placeholder="请输入主题标题，如果标题能够表达完整内容，则正文可以为空"></textarea></div>
            <div class="cell"><div class="fr fade" id="content_remaining">20000</div>
                正文
            </div>
            <div class="cell">
                <textarea name="content" style="resize: none; width: 753px;height:293px;"></textarea>
            </div>
            <div class="cell">
                <select name="tag" id="nodes" style="width: 300px; font-size: 14px; display: none;" data-placeholder="请选择一个标签">
                    <option></option>
                    <option value="2015">node / test</option>
                </select><div class="select2-container" style="width: 300px">
                <a href="javascript:void(0)" class="select2-choice">   <span>MacBook Pro / mbp</span><abbr class="select2-search-choice-close" style="display:none;"></abbr>   <div><b></b></div></a>    <div class="select2-drop select2-offscreen">   <div class="select2-search">       <input type="text" autocomplete="off" class="select2-input">   </div>   <ul class="select2-results">   </ul></div></div>
            </div>
            <input name="formToken" value="${formToken}" hidden>
            <div class="cell">
                <div class="fr">
                <span id="error_message"></span> &nbsp;
                <input type="submit" class="super normal button"> &nbsp;发布主题</input>
            </div>
    </div>
        </form>

    </div>
</div>
<%@include file="bottom.jsp"%>
<script src="${path}/static/js/jquery-3.2.1.js" type="text/javascript"></script>
</body>
</html>
