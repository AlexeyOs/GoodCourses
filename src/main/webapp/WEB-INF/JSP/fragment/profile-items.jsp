<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<c:forEach var="profile" items="${profiles }">
	<div class="card">
		<div class="card-body">
			<div class="media-left media-top">
				<c:choose>
					<c:when test="${not empty profile.smallPhoto}">
						<c:set var="photoUrl" value="${profile.smallPhoto}" />
					</c:when>
					<c:otherwise>
						<c:set var="photoUrl" value="${pageContext.request.contextPath}/static/img/profile-placeholder.png" />
					</c:otherwise>
				</c:choose>
				<a href="/<c:out value="${profile.uid}" />"><img alt="<c:out value="${profile.fullName}" />" src="<c:out value='${photoUrl}' />" class="photo"></a>
			</div>
			<div class="media-body search-result-item">
				<a href="/<c:out value="${profile.uid}" />" class="btn btn-primary pull-right">Детали</a>
				<h4 class="media-heading">
					<a href="/<c:out value="${profile.uid}" />">${profile.id}) <c:out value="${profile.fullName}" /></a>
				</h4>
				<c:if test="${not empty profile.aboutMe}">
					<p><c:out value="${profile.aboutMe}" /></p>
				</c:if>
				<p>${profile.city },${profile.country }</p>
			</div>
		</div>
	</div>
</c:forEach>
