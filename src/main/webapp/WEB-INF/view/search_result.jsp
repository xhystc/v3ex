<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2018/2/22
  Time: 14:16
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="head.jsp"%>
<%@include file="rightbar.jsp"%>
<div id="Main">
    <div class="sep20"></div>
    <div class="box">
        <%@include file="pagebutton.jsp"%>
        <div class="cell" style="background-color: #f9f9f9; padding: 10px 10px 10px 20px;">
        </div>
        <%@include file="item.jsp"%>
    </div>
</div>
<%@include file="bottom.jsp"%>
<script src="${path}/static/jquery-3.2.1.js" type="text/javascript"></script>
<%--<script src="${path}/static/js/indexScroll.js"></script>--%>
</body>
</html>

