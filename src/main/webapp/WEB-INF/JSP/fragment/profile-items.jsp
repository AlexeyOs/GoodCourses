<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="profile" items="${profiles }">
	<div class="card">
		<div class="card-body">
			<div class="media-left media-top">
				<a href="/${profile.uid }"><img alt="${profile.fullName }" src="${profile.smallPhoto }" class="photo"></a>
			</div>
			<div class="media-body search-result-item">
				<a href="/${profile.uid }" class="btn btn-primary pull-right">Детали</a>
				<h4 class="media-heading">
					<a href="/${profile.uid }">${profile.id}) ${profile.fullName }, ${profile.age }</a>
				</h4>
				<c:if test="${not empty profile.aboutMe}">
					<p><c:out value="${profile.aboutMe}" /></p>
				</c:if>
				<p>${profile.city },${profile.country }</p>
			</div>
		</div>
	</div>
</c:forEach>
