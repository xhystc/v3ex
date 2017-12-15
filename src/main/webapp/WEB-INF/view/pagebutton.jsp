<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/12/1
  Time: 9:49
  To change this template use File | Settings | File Templates.
--%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="cell">
    <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tr>
            <td width="92%" align="left">
                <c:forEach items="${pageButtons}" var="index">
                  <c:choose>
                      <c:when test="${index != 0}">
                          <a href="?<c:if test='${not empty currentTag}'>tagId=${currentTag}&</c:if>page=${index}"
                                  <c:choose>
                                      <c:when test="${currentPage eq index}">
                                          class="page_current"
                                      </c:when>
                                      <c:otherwise>
                                          class="page_normal"
                                      </c:otherwise>
                                  </c:choose>
                          >${index}</a> &nbsp;
                      </c:when>
                      <c:otherwise>
                          ...
                      </c:otherwise>
                  </c:choose>
                </c:forEach>
                <input type="number" class="page_input" autocomplete="off" value="${currentPage}" min="1" max="${lastPage}" onkeydown="if (event.keyCode == 13)
                    location.href = '?page=' + this.value"/></td>
            <td width="8%" align="right">
                <table cellpadding="0" cellspacing="0" border="0" width="100%">
                    <style type="text/css">
                        .disable_now {
                            color: #ccc !important;
                            background-color: #fff !important;
                        }

                        .hover_now {
                            cursor: pointer;
                            color: #333 !important;
                            background-color: #f9f9f9 !important;
                            text-shadow: 0px 1px 0px #fff !important;
                        }

                        .active_now {
                            background-color: #e2e2e2 !important;
                        }
                    </style>
                    <tr>
                        <c:choose >
                            <c:when test="${currentPage != 1}">
                                <td width="50%" align="center" class="super normal button"
                                    style="border-right: none; border-top-right-radius: 0px; border-bottom-right-radius: 0px;"
                                    onclick="location.href='?page=${currentPage-1}';" onmouseover="$(this).addClass('hover_now');"
                                    onmousedown="$(this).addClass('active_now');"
                                    onmouseleave="$(this).removeClass('hover_now'); $(this).removeClass('active_now');"
                                    title="上一页">❮
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td width="50%" align="center"
                                    class="super normal_page_right button disable_now"
                                    style="border-top-left-radius: 0px; border-bottom-left-radius: 0px;">❮
                                </td>
                            </c:otherwise>
                        </c:choose>
                        <c:choose >
                            <c:when test="${currentPage != lastPage}">
                                <td width="50%" align="center" class="super normal button"
                                    style="border-right: none; border-top-right-radius: 0px; border-bottom-right-radius: 0px;"
                                    onclick="location.href='?page=${currentPage+1}';" onmouseover="$(this).addClass('hover_now');"
                                    onmousedown="$(this).addClass('active_now');"
                                    onmouseleave="$(this).removeClass('hover_now'); $(this).removeClass('active_now');"
                                    title="下一页">❯
                                </td>
                            </c:when>
                            <c:otherwise>
                                <td width="50%" align="center"
                                    class="super normal_page_right button disable_now"
                                    style="border-top-left-radius: 0px; border-bottom-left-radius: 0px;">❯
                                </td>
                            </c:otherwise>
                        </c:choose>
                    </tr>
                </table>
            </td>
        </tr>
    </table>
</div>
