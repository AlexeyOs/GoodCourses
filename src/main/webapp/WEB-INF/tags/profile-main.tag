<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>

<div class="card">
	<a href="/edit"><img class="img-responsive photo" src="${profile.largePhoto}" alt="photo"></a>
	<h1 class="text-center">
		<a style="color: black;" href="/edit">${profile.fullName}</a>
	</h1>
	<div class="list-group">
		<div class="card-header">
			<a class="edit-block" href="/edit/profile">Редактировать</a>
		</div>
		<a class="list-group-item"><i class="fa fa-envelope"></i> ${profile.email}</a>
		<a class="list-group-item"><i>О себе: </i> ${profile.summary}</a>
	</div>
</div>