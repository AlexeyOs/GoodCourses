<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="card">
    <div class="card-header">
        Добавить курс
    </div>
    <div class="card-body">
        <form:form action="${pageContext.request.contextPath}/add/course" method="post">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            <div class="form-group">
                <label for="platform">Платформа</label> <input id="platform" name="platform" class="form-control" value="<c:out value='${platform}'/>">
            </div>
            <div class="form-group">
                <label for="author">Автор</label> <input id="author" name="author" class="form-control" value="<c:out value='${author}'/>">
            </div>
            <div class="form-group">
                <label for="subjectOfStudy">Предмет, которому обучали</label> <input id="subjectOfStudy" name="subjectOfStudy" class="form-control" value="<c:out value='${subjectOfStudy}'/>" required>
            </div>
            <div class="form-group">
                <label for="link">Ссылка на курс</label> <input id="link" name="link" type="url" class="form-control" value="<c:out value='${link}'/>" required>
                <c:if test="${not empty courseLinkError}">
                    <p class="text-danger" role="alert"><c:out value="${courseLinkError}"/></p>
                </c:if>
            </div>
            <div class="form-group" style="display:table;width:100%;">
                <input type="submit" class="btn btn-primary" value="Сохранить">
            </div>
        </form:form>
    </div>
</div>
