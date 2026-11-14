<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>

<div class="card">
	<a href="/edit"><img class="img-responsive photo" src="${profile.largePhoto}" alt="photo"></a>
	<h1 class="text-center">
		<a style="color: black;" href="/edit"><c:out value="${profile.fullName}" /></a>
	</h1>
	<h6 class="text-center">
		<strong>${profile.city}</strong>
	</h6>
	<h6 class="text-center">
		<strong>Age:</strong> ${profile.age}, <strong>Birthday: </strong> ${profile.birthDay}
	</h6>
	<div class="list-group">
		<a class="list-group-item" href="tel:+380507525137"><i class="fa fa-phone"></i> ${profile.phone}</a>
		<a class="list-group-item" href="mailto:richard-hendricks@gmail.com"><i class="fa fa-envelope"></i> ${profile.email}</a>
		<a class="list-group-item" href="javascript:void(0);"><i class="fa fa-skype"></i> ${profile.contacts.skype}</a>
		<a target="_blank" class="list-group-item" href="https://vk.com/richard-hendricks"><i class="fa fa-vk"></i> ${profile.contacts.vkontakte}</a>
		<a target="_blank" class="list-group-item" href="https://facebook.com/richard-hendricks"><i class="fa fa-facebook"></i> ${profile.contacts.facebook}</a>
		<a target="_blank" class="list-group-item" href="https://linkedin.com/richard-hendricks"><i class="fa fa-linkedin"></i> ${profile.contacts.linkedin}</a>
		<a target="_blank" class="list-group-item" href="https://github.com/richard-hendricks"><i class="fa fa-github"></i> ${profile.contacts.github}</a>
		<a target="_blank" class="list-group-item" href="https://stackoverflow.com/richard-hendricks"><i class="fa fa-stack-overflow"></i> ${profile.contacts.stackoverflow}</a>
	</div>
</div>
