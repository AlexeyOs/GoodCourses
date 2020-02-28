<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>

<nav class="navbar navbar-expand-lg navbar-light bg-light">
    <a class="navbar-brand" href="/courses">GoodCourses</a>
    <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
    </button>

    <div class="collapse navbar-collapse " id="navbarSupportedContent">
        <%--TODO скрыт поиск, пока не реализован в backend--%>
        <%--<form class="form-inline my-2 my-lg-0">--%>
            <%--<input class="form-control mr-sm-2" type="search" placeholder="Поиск" aria-label="Search">--%>
            <%--<button class="btn btn-outline-success my-2 my-sm-0" type="submit">Поиск</button>--%>
        <%--</form>--%>
        <ul class="navbar-nav mr-auto">
            <course:link-on-courses/>
        </ul>
        <ul class="navbar-nav ml-auto">
            <course:menu />
        </ul>
    </div>
</nav>