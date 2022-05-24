<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="profile" items="${profiles }">
	<div class="card">
		<div class="card-header">${profile.uid }</div>
		<div class="card-body">
			<a href="/${profile.uid }" class="btn btn-primary pull-right">Детали</a>
			<h4 class="media-heading">
				<a href="/${profile.uid}">${profile.id} - ${profile.fullName }</a>
				<blockquote>
					<small>${profile.summary }</small>
				</blockquote>
			</h4>
		</div>
	</div>
</c:forEach>