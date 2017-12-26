<%--
  Created by IntelliJ IDEA.
  User: 87173
  Date: 2017/11/29
  Time: 9:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="box" id="TopicsHot">
    <div class="cell"><span class="fade">当前热议主题</span></div>

   <c:forEach items="${hotQuestions}" var="hot">
       <div class="cell from_266181 hot_t_410165">
           <table cellpadding="0" cellspacing="0" border="0" width="100%">
               <tr>
                   <td width="24" valign="middle" align="center">
                       <a href="${path}/user/${hot.user}">
                           <img src="${path}/${hot.user.iconUrl}" class="avatar" border="0" align="default" style="max-width: 24px; max-height: 24px;" />
                       </a>
                   </td>
                   <td width="10"></td>
                   <td width="auto" valign="middle">
                <span class="item_hot_topic_title">
                <a href="${path}/q/${hot.id}">${hot.title}</a>
                </span>
                   </td>
               </tr>
           </table>
       </div>
   </c:forEach>

</div>