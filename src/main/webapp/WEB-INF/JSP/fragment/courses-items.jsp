<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:forEach var="course" items="${courses }">
    <div class="card">
        <div class="card-header">
            <p>${course.platform}</p>
        </div>
        <div class="card-body">
            <p>Предмет: ${course.subjectOfStudy}</p>
            <button  onclick="window.location.href='/course/${course.id}'" class="btn btn-primary pull-right">Подробнее</button>
        </div>
    </div>
</c:forEach>
