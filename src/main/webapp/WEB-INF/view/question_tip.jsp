<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/30
  Time: 14:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="box">
    <div class="cell">发帖提示</div>
    <div class="inner">
        <ul style="margin-top: 0px;">
            <li><span class="f13">主题标题</span><div class="sep10"></div>
                请在标题中描述内容要点。如果一件事情在标题的长度内就已经可以说清楚，那就没有必要写正文了。
                <div class="sep10"></div>
            </li>
            <li><div class="fr" style="margin-top: -5px; margin-right: 5px;"><img src="${path}/static/img/markdown.png" border="0" width="32"></div><span class="f13">正文</span><div class="sep10"></div>
                可以在正文中为你要发布的主题添加更多细节。V2EX 支持 <span style="font-family: Consolas, 'Panic Sans', mono"><a href="https://help.github.com/articles/github-flavored-markdown" target="_blank">GitHub Flavored Markdown</a></span> 文本标记语法。
                <div class="sep10"></div>
                在正式提交之前，你可以点击本页面左下角的“预览主题”来查看 Markdown 正文的实际渲染效果。
                <div class="sep10"></div>
            </li>
            <li><span class="f13">选择节点</span><div class="sep10"></div>
                在最后，请为你的主题选择一个节点。恰当的归类会让你发布的信息更加有用。
                <div class="sep10"></div>
                你可以在主题发布后 300 秒内，对标题或者正文进行编辑。同时，在 300 秒内，你可以重新为主题选择节点。
            </li>
        </ul>
    </div>
</div>