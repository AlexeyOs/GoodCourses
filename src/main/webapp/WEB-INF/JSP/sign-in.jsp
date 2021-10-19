<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<div class="card small-center-block">
	<div class="card-header">
		<i class="fa fa-sign-in"></i> Вход в Ваш личный кабинет
	</div>
	<div class="card-body">
		<form action="/sign-in-handler" method="post">
			<c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null}">
				<div class="alert alert-danger" role="alert">
					<button type="button" class="close" data-dismiss="alert" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
						${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }
					<c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
				</div>
			</c:if>
			<div class="form-group">
				<label for="uid">Логин или email</label> <input id="uid" name="uid" class="form-control" placeholder="Login or email" required autofocus>
			</div>
			<div class="form-group">
				<label for="password">Пароль</label> <input id="password" type="password" name="password" class="form-control" placeholder="Password" required>
			</div>
			<div class="form-group">
				<label><input type="checkbox" name="remember-me" value="true"> Запомнить меня</label>
			</div>
			<div class="form-group" style="display:table;width:100%;">
				<button type="submit" class="btn btn-primary pull-left">Войти</button>
				<a href="/sign-up" class="pull-right">Зарегистрироваться</a>
				<br/>
				<br/>
				<a href="/password-reset" class="pull-right">Восстановить доступ</a>
			</div>
			<%--TODO скрыта кнопка FACEBOOK, пока нельзя авторизоваться--%>
			<%--<div class="form-group text-center">--%>
				<%--<a href="/fbLogin" class="btn btn-warning">Войти через Facebook</a>--%>
			<%--</div>--%>
		</form>
	</div>
</div>