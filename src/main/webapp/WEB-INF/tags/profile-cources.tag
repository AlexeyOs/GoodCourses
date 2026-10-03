<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="card">
	<div class="card-header">
		<i class="fa fa-book"></i> Courses
		<c:if test="${canEdit}">
			<a class="btn btn-primary btn-sm pull-right" href="${pageContext.request.contextPath}/edit/courses">
				<i class="fa fa-pencil"></i> Редактировать курсы
			</a>
		</c:if>
	</div>
	<c:if test="${fn:length(profile.courses) > 0}">
		<div class="card-body">
			<c:forEach var="course" items="${profile.courses}">
			<div class="timeline-heading">
				<h4 class="timeline-title"><a href="${pageContext.request.contextPath}/course/${course.id}">${course.subjectOfStudy}</a></h4>
				<p>${course.platform}<c:if test="${not empty course.author}"> — ${course.author}</c:if></p>
				<p>
					<small class="dates"><i class="fa fa-calendar"></i> <strong>Finish Date:</strong> <strong class="label label-danger">${course.finishDate}</strong> </small>
				</p>
			</div>
			</c:forEach>
		</div>
	</c:if>
</div>
