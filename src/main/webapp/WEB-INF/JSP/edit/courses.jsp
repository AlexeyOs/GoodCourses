<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="card">
	<div class="card-body">
		<h4 class="data-header">Мои курсы</h4>
		<p class="text-muted">Отметьте курсы, которые хотите показывать в своём профиле.</p>
		<hr />
		<form action="${pageContext.request.contextPath}/edit/courses" method="post">
			<c:if test="${empty courses}">
				<p>Доступных курсов пока нет.</p>
			</c:if>
			<c:forEach var="course" items="${courses}">
				<div class="form-check" style="margin-bottom: 12px;">
					<label>
						<input type="checkbox" name="courseIds" value="${course.id}" ${selectedCourseIds.contains(course.id) ? 'checked="checked"' : ''}>
						<strong>${course.subjectOfStudy}</strong> — ${course.platform}<c:if test="${not empty course.author}">, ${course.author}</c:if>
					</label>
				</div>
			</c:forEach>
			<hr />
			<button type="submit" class="btn btn-primary">Сохранить</button>
			<a class="btn btn-link" href="${pageContext.request.contextPath}/my-profile">Отмена</a>
		</form>
	</div>
</div>
