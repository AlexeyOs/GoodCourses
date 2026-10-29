<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<div class="card edit-feedback">
    <div class="card-header"><i class="fa fa-pencil"></i> Редактирование отзыва</div>
    <div class="card-body">
        <h5><c:out value="${feedback.course.subjectOfStudy}" /></h5>
        <p class="text-muted">
            <c:out value="${feedback.course.platform}" />
            <c:if test="${not empty feedback.course.author}"> — <c:out value="${feedback.course.author}" /></c:if>
        </p>
        <form:form action="${pageContext.request.contextPath}/my-reviews/${feedback.id}/edit" method="post" modelAttribute="feedbackForm">
            <div class="form-group">
                <form:label path="description">Текст отзыва</form:label>
                <form:textarea path="description" cssClass="form-control" rows="8" maxlength="2000" required="required" />
                <form:errors path="description" cssClass="text-danger" element="div" />
                <small class="form-text text-muted">Не более 2000 символов.</small>
            </div>
            <div class="edit-feedback-actions">
                <button class="btn btn-primary" type="submit">Сохранить</button>
                <a class="btn btn-secondary" href="${pageContext.request.contextPath}/my-reviews">Отмена</a>
            </div>
        </form:form>
    </div>
</div>
