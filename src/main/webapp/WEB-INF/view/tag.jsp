<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/29
  Time: 9:33
  To change this template use File | Settings | File Templates.
--%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>

    <div class="inner" style="background-color: #fff; border-top-left-radius: 3px;
    border-top-right-radius: 3px;" id="Tabs">
        <a href="${path}/index"
                <c:choose>
                    <c:when test="${empty currentTag}">
                        class="tab_current"
                    </c:when>
                    <c:otherwise>
                        class="tab"
                    </c:otherwise>
                </c:choose>
        >全部</a>
        <c:forEach items="${tags}" var="tag">
            <a href="${path}/index?tagId=${tag.id}"
                    <c:choose>
                        <c:when test="${currentTag == tag.id}">
                            class="tab_current"
                        </c:when>
                        <c:otherwise>
                            class="tab"
                        </c:otherwise>
                    </c:choose>
            >${tag.name}</a>
        </c:forEach>

    </div>