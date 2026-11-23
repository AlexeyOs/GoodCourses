<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card">
	<c:choose>
		<c:when test="${not empty profile.largePhoto}">
			<c:set var="photoUrl" value="${profile.largePhoto}" />
		</c:when>
		<c:otherwise>
			<c:set var="photoUrl" value="${pageContext.request.contextPath}/static/img/profile-placeholder.png" />
		</c:otherwise>
	</c:choose>
	<img class="img-responsive photo" src="<c:out value='${photoUrl}' />" alt="photo">
	<h1 class="text-center">
		<c:out value="${profile.fullName}" />
	</h1>
	<h6 class="text-center">
		<strong>${profile.city}</strong>
	</h6>
	<div class="list-group">
		<a class="list-group-item" href="mailto:richard-hendricks@gmail.com"><i class="fa fa-envelope"></i> ${profile.email}</a>
	</div>
</div>
