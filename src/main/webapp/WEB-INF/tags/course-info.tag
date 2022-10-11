<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<div class="card">
	<div class="card-header">
		О курсе
	</div>
	<div class="card-body">
			<table class="table table-bordered">
				<tbody>
				<tr>
					<td>Платформа: </td>
					<td>${course.platform}</td>
				</tr>
				<tr>
					<td>Автор: </td>
					<td>${course.author}</td>
				</tr>
				<tr>
					<td>Предмет, которому обучали: </td>
					<td>${course.subjectOfStudy}</td>
				</tr>
				<tr>
					<td>Сслыка на курс: </td>
					<td><a target="_blank" href="${course.link}">${course.link}</a></td>
				</tr>
				<tr>
					<td>Описание : </td>
					<td>${course.description}</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>