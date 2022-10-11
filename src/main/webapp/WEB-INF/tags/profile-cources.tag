<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="card">
	<div class="card-header">
		<i class="fa fa-book"></i> Уникальные курсы добавленные вами на платформу
	</div>
	<c:if test="${fn:length(profile.courses) > 0}">
		<div class="card-body">
			<c:forEach var="course" items="${profile.courses}">
			<div class="timeline-heading">
				<h4 class="timeline-title">
					<a target="_blank" href="${pageContext.request.contextPath}/course/${course.id}">
							${course.subjectOfStudy} &nbsp; ${course.platform}
					</a>
				</h4>
				<p>
					<c:choose>
						<c:when test="${course.visible} == true">
							Опубликован администратором
							<br/>
						</c:when>
						<c:otherwise>
							Не опубликован администратором
							<br/>
						</c:otherwise>
					</c:choose>
					${course.description}
				</p>
			</div>
			</c:forEach>
		</div>
	</c:if>
</div>