<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<div class="card">
	<div class="card-header">
		О курсе
	</div>
	<div class="card-body">
			<table class="table table-bordered">
				<tbody>
				<tr>
					<td>Платформа: </td>
					<td><c:out value="${course.platform}" /></td>
				</tr>
				<tr>
					<td>Автор: </td>
					<td><c:out value="${course.author}" /></td>
				</tr>
				<tr>
					<td>Предмет, которому обучали: </td>
					<td><c:out value="${course.subjectOfStudy}" /></td>
				</tr>
				<tr>
					<td>Сслыка на курс: </td>
					<td>
						<c:choose>
							<c:when test="${fn:startsWith(course.link, 'https://') or fn:startsWith(course.link, 'http://')}">
								<a target="_blank" rel="noopener noreferrer" href="<c:out value="${course.link}" />"><c:out value="${course.link}" /></a>
							</c:when>
							<c:otherwise><c:out value="${course.link}" /></c:otherwise>
						</c:choose>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
