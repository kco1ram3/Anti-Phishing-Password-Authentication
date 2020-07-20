<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<c:set var="isLogin" value="<%= session.getAttribute("isLogin") %>"/>
<c:if test='${isLogin == null || isLogin != "true"}'>
    <c:redirect url="/Login"/>
</c:if>