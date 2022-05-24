<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="card">
	<div class="card-header">
		<i class="fa fa-book"></i> Курсы
	</div>
	<c:if test="${fn:length(profile.courses) > 0}">
		<div class="card-body">
			<c:forEach var="course" items="${profile.courses}">
			<div class="timeline-heading">
				<h4 class="timeline-title">${course.name}</h4>
				<p>
					<small class="dates"><i class="fa fa-calendar"></i> <strong>Finish Date:</strong> <strong class="label label-danger">${course.finishDate}</strong> </small>
				</p>
			</div>
			</c:forEach>
		</div>
	</c:if>
</div>