<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="container">
    <div class="row">
        <div class="col-md-4 col-sm-6">
            <course:course-school />
        </div>
    </div>
    <div class="row">
        <div class="col-md-4 col-sm-6">
            <course:feedbacks-view />
        </div>
    </div>
    <div class="row">
        <div class="col-md-4 col-sm-6">
            <course:feedback-you />
        </div>
    </div>
</div>