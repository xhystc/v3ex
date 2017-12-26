<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:43
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="my" uri="/WEB-INF/tld/myutil.tld"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="cell">

    <div class="topic_content">${my:linkConvert(path,commentPage.question.content)}
    </div>

</div>