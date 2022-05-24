<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<security:authorize access="isAuthenticated()">
	<security:authentication property="principal.username" var="userName"/>
</security:authorize>

<div class="card">
	<h1 class="text-center">
		<a style="color: black;">${profile.fullName}</a>
	</h1>
	<div class="list-group">
		<c:if test="${fn:contains(requestScope['javax.servlet.forward.request_uri'], userName)}">
			<div class="card-header">
				<a class="edit-block" href="/edit/profile">Редактировать</a>
			</div>
		</c:if>
		<a class="list-group-item"><i class="fa fa-envelope"></i> ${profile.email}</a>
		<a class="list-group-item"><i>О себе: </i> ${profile.summary}</a>
	</div>
</div>