<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="card">
	<div class="card-header">
		<i class="fa fa-code"></i> Технические навыки
		<c:if test="${canEdit}">
			<a class="btn btn-primary btn-sm pull-right" href="${pageContext.request.contextPath}/edit/skills">
				<i class="fa fa-pencil"></i> Редактировать навыки
			</a>
		</c:if>
	</div>
	<div class="card-body">
		<c:if test="${empty profile.skills}">
			<p class="text-muted">Навыки пока не добавлены.</p>
			<c:if test="${canEdit}">
				<a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/edit/skills">
					<i class="fa fa-plus"></i> Добавить первый навык
				</a>
			</c:if>
		</c:if>
		<c:if test="${not empty profile.skills}">
		<table class="table table-striped table-bordered">
			<tbody>
			<tr>
				<th style="width: 140px;">Category</th>
				<th>Frameworks and technologies</th>
			</tr>
			<c:forEach var="skill" items="${profile.skills}">
			<tr>
				<td>${skill.category}</td>
				<td>${skill.value}</td>
			</tr>
			</c:forEach>
			</tbody>
		</table>
		</c:if>
	</div>
</div>
