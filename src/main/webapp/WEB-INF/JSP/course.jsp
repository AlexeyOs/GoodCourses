<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="course" tagdir="/WEB-INF/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="row">
    <div class="col-md-12 col-sm-6">
        <course:course-info />
    </div>
</div>
<div class="row">
    <div class="col-md-12 col-sm-6">
        <div class="card course-rating-summary">
            <div class="card-header"><i class="fa fa-star"></i> Рейтинг курса</div>
            <div class="card-body">
                <c:choose>
                    <c:when test="${ratingSummary.count > 0}">
                        <p class="mb-0">
                            <strong>Средняя оценка:</strong>
                            <fmt:formatNumber value="${ratingSummary.average}" minFractionDigits="1" maxFractionDigits="1" /> из 5
                            <span class="text-muted">(<c:out value="${ratingSummary.count}" /> оценок)</span>
                        </p>
                    </c:when>
                    <c:otherwise>
                        <p class="text-muted mb-0">Оценок пока нет</p>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-md-12 col-sm-6">
        <course:feedbacks-view />
    </div>
</div>
<div class="row">
    <div class="col-md-12 col-sm-6">
        <course:feedback-you />
    </div>
</div>
