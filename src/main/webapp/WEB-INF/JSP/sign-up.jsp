<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>

<div class="card small-center-block">
    <div class="card-header">
        <i class="fa fa-sign-in"></i> Регистрация
    </div>
    <div class="card-body">
        <form action="/sign-up" method="post">
            <c:if test="${sessionScope.SPRING_SECURITY_LAST_EXCEPTION != null}">
                <div class="alert alert-danger" role="alert">
                    <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                        ${sessionScope.SPRING_SECURITY_LAST_EXCEPTION.message }
                    <c:remove var="SPRING_SECURITY_LAST_EXCEPTION" scope="session" />
                </div>
            </c:if>
            <div class="help-block">Вы можете использовать Ваши UID или Email или Phone в качестве логина</div>
            <div class="form-group">
                <label for="first_name">Имя</label> <input id="first_name" name="first_name" class="form-control" placeholder="Иван" required autofocus>
            </div>
            <div class="form-group">
                <label for="last_name">Фамилия</label> <input id="last_name" name="last_name" class="form-control" placeholder="Иванов" required autofocus>
            </div>
            <div class="form-group">
                <label for="uid">Логин</label> <input id="uid" name="uid" class="form-control" placeholder="UID или Email или Phone" required autofocus>
            </div>
            <div class="form-group">
                <label for="password">Пароль</label> <input id="password" type="password" name="password" class="form-control" placeholder="Password" required>
            </div>
            <div class="form-group" style="display:table;width:100%;">
                <button type="submit" class="btn btn-primary pull-left">Зарегестрироваться</button>
            </div>
        </form>
    </div>
</div>