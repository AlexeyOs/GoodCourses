<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<div class="card my-reviews">
    <div class="card-header">
        <i class="fa fa-comments"></i> Мои отзывы
    </div>
    <div class="card-body">
        <c:choose>
            <c:when test="${empty reviews.content}">
                <div class="text-center my-reviews-empty">
                    <p class="text-muted">Вы ещё не оставляли отзывы.</p>
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/courses">
                        Перейти к списку курсов
                    </a>
                </div>
            </c:when>
            <c:otherwise>
                <c:forEach var="review" items="${reviews.content}">
                    <article class="card my-review-item">
                        <div class="card-header">
                            <h5 class="mb-0"><c:out value="${review.course.subjectOfStudy}" /></h5>
                        </div>
                        <div class="card-body">
                            <dl class="row mb-3">
                                <dt class="col-sm-3">Платформа</dt>
                                <dd class="col-sm-9"><c:out value="${review.course.platform}" /></dd>
                                <dt class="col-sm-3">Автор курса</dt>
                                <dd class="col-sm-9"><c:out value="${review.course.author}" /></dd>
                                <dt class="col-sm-3">Оценка</dt>
                                <dd class="col-sm-9">
                                    <c:choose>
                                        <c:when test="${not empty review.rating}"><c:out value="${review.rating}" /> из 5</c:when>
                                        <c:otherwise>Без оценки</c:otherwise>
                                    </c:choose>
                                </dd>
                                <dt class="col-sm-3">Дата создания</dt>
                                <dd class="col-sm-9"><fmt:formatDate value="${review.startDate}" pattern="dd.MM.yyyy HH:mm" /></dd>
                                <c:if test="${not empty review.lastUpdate}">
                                    <dt class="col-sm-3">Дата изменения</dt>
                                    <dd class="col-sm-9"><fmt:formatDate value="${review.lastUpdate}" pattern="dd.MM.yyyy HH:mm" /></dd>
                                </c:if>
                            </dl>
                            <div class="my-review-text"><c:out value="${review.description}" /></div>
                            <div class="my-review-actions">
                                <a class="btn btn-outline-primary" href="${pageContext.request.contextPath}/course/${review.course.id}">Перейти к курсу</a>
                                <a class="btn btn-primary" href="${pageContext.request.contextPath}/my-reviews/${review.id}/edit">Редактировать</a>
                                <form class="d-inline" action="${pageContext.request.contextPath}/my-reviews/${review.id}/delete" method="post" onsubmit="return confirm('Удалить этот отзыв?');">
                                    <button class="btn btn-danger" type="submit">Удалить</button>
                                </form>
                            </div>
                        </div>
                    </article>
                </c:forEach>

                <c:if test="${reviews.totalPages > 1}">
                    <nav class="my-reviews-pagination" aria-label="Навигация по отзывам">
                        <ul class="pagination justify-content-center">
                            <li class="page-item ${reviews.first ? 'disabled' : ''}">
                                <c:choose>
                                    <c:when test="${reviews.first}"><span class="page-link">Предыдущая</span></c:when>
                                    <c:otherwise><a class="page-link" href="${pageContext.request.contextPath}/my-reviews?page=${reviews.number - 1}">Предыдущая</a></c:otherwise>
                                </c:choose>
                            </li>
                            <li class="page-item disabled"><span class="page-link">Страница ${reviews.number + 1} из ${reviews.totalPages}</span></li>
                            <li class="page-item ${reviews.last ? 'disabled' : ''}">
                                <c:choose>
                                    <c:when test="${reviews.last}"><span class="page-link">Следующая</span></c:when>
                                    <c:otherwise><a class="page-link" href="${pageContext.request.contextPath}/my-reviews?page=${reviews.number + 1}">Следующая</a></c:otherwise>
                                </c:choose>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </c:otherwise>
        </c:choose>
    </div>
</div>
