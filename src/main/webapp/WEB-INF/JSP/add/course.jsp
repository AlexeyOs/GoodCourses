<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<div class="card">
    <div class="card-header">
        Добавить курс
    </div>
    <div class="card-body">
        <form:form action="/add/course" method="post" commandName="courseForm">
            <div class="form-group">
                <label for="platform">Платформа</label> <input id="platform" name="platform" class="form-control">
            </div>
            <div class="form-group">
                <label for="author">Автор</label> <input id="author" name="author" class="form-control">
            </div>
            <div class="form-group">
                <label for="subjectOfStudy">Предмет, которому обучали</label> <input id="subjectOfStudy" name="subjectOfStudy" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="link">Сслыка на курс</label> <input id="link" name="link" class="form-control" required>
            </div>
            <div class="form-group" style="display:table;width:100%;">
                <input type="submit" class="btn btn-primary" value="Сохранить">
            </div>
        </form:form>
    </div>
</div>