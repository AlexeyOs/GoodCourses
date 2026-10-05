<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="card">
    <div class="card-body">
        <h4 class="data-header">О себе</h4>
        <hr />
        <form:form action="${pageContext.request.contextPath}/edit/about" method="post" modelAttribute="aboutMeForm">
            <div class="form-group">
                <form:textarea path="aboutMe" cssClass="form-control" rows="10"
                               maxlength="5000" placeholder="Расскажите о себе, своих интересах и целях обучения" />
                <form:errors path="aboutMe" cssClass="text-danger" />
            </div>
            <button type="submit" class="btn btn-primary">Сохранить</button>
            <a class="btn btn-default" href="${pageContext.request.contextPath}/my-profile">Отмена</a>
        </form:form>
    </div>
</div>
