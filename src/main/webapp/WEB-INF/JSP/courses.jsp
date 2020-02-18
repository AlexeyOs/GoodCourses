<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize access= "hasAuthority('USER')" var= "isUSer"/>

<% String message = (String)request.getAttribute("alertMsg");%>
<script type="text/javascript">
    var msg = "<%=message%>";
    if (msg !== null && msg !== "null" && msg !== ""){
        alert(msg);
    }
</script>

<div class="row courses">
    <div id="profileContainer" class="col-xs-12" data-profile-total="${page.totalPages }" data-profile-number="${page.number }">
        <jsp:include page="fragment/courses-items.jsp" />
    </div>
</div>

<c:if test= "${isUSer}">
    <br/>
    <div class="row">
        <div class="col-md-8 col-sm-6">
            <p>Если в списке, не хватает хорошего IT курса, то добавьте его</p>
        </div>
        <div class="col-md-4 col-sm-6">
            <button type="submit" class="btn btn-primary" onclick="window.location.href='/add/course'">Добавить курс</button>
        </div>
    </div>
    <br/>
</c:if>