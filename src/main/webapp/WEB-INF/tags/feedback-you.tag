<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>

<security:authorize access= "hasAuthority('USER')" var= "isUSer"/>

<c:if test= "${isUSer}">
    <c:if test = "${editFeedback == false}">
        <div class="card">
            <div class="card-header">Ваш отзыв</div>
            <div class="card-body">
                <hr />
                <form:form action="${pageContext.request.contextPath}/add/feedback" method="post" modelAttribute="feedbackForm">
                    <p><input type="hidden" name="courseId" value="${course.id}"/></p>
                    <div class="form-group">
                        <form:label path="rating">Оценка курса</form:label>
                        <form:select path="rating" cssClass="form-control" required="required" aria-describedby="ratingHelp">
                            <form:option value="">Выберите оценку</form:option>
                            <form:option value="1">1 — Очень плохо</form:option>
                            <form:option value="2">2 — Плохо</form:option>
                            <form:option value="3">3 — Нормально</form:option>
                            <form:option value="4">4 — Хорошо</form:option>
                            <form:option value="5">5 — Отлично</form:option>
                        </form:select>
                        <form:errors path="rating" cssClass="text-danger" element="div" />
                        <small id="ratingHelp" class="form-text text-muted">Оценка обязательна и должна быть от 1 до 5.</small>
                    </div>
                    <div class="form-group">
                        <form:label path="description">Текст отзыва</form:label>
                        <form:textarea path="description" cssClass="form-control" rows="6" maxlength="2000" required="required" />
                        <form:errors path="description" cssClass="text-danger" element="div" />
                    </div>
                    <p><input type="submit" class="btn btn-primary" value="Сохранить" /></p>
                </form:form>
            </div>
        </div>
    </c:if>
</c:if>
