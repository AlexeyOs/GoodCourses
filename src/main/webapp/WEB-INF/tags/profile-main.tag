<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card">
	<img class="img-responsive photo" src="${profile.largePhoto}" alt="photo">
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
