<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/WEB-INF/view/head.jsp"%>
<%@include file="/WEB-INF/view/rightbar.jsp"%>
        <div id="Main">
            <div class="sep20"></div>
            <div class="box" style="border-bottom: 0px;">
                <%@include file="/WEB-INF/view/question_head.jsp"%>
                <%@include file="/WEB-INF/view/question_content.jsp"%>
                <%@include file="/WEB-INF/view/question_button.jsp"%>
            </div>

            <div class="sep20"></div>
            <div class="box">
                <%@include file="/WEB-INF/view/question_inform.jsp"%>
                <%@include file="/WEB-INF/view/question_comment_item.jsp"%>
                <c:choose>
                    <c:when test="${question.commentCount gt 20}">
                        <%@include file="/WEB-INF/view/question_pagebutton.jsp"%>
                    </c:when>
                </c:choose>
            </div>
            <div class="sep20"></div>
            <%@include file="/WEB-INF/view/question_comment_edit.jsp"%>

        </div>
<%@include file="/WEB-INF/view/bottom.jsp"%>
<script src="${path}/static/js/jquery-3.2.1.js" type="text/javascript"></script>
</body>
</html>