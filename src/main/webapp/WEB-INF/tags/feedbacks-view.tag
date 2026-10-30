<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<div class="card">
    <div class="card-header">Отзывы</div>
    <div class="card-body">
        <br/>
        <c:forEach var="feedback" items="${feedbacks}">
            <table class="table table-bordered">
                <tbody>
                <tr>
                    <td>
                        <p class="course-feedback-rating mb-2">
                            <strong>Оценка:</strong>
                            <c:choose>
                                <c:when test="${not empty feedback.rating}"><c:out value="${feedback.rating}" /> из 5</c:when>
                                <c:otherwise>Без оценки</c:otherwise>
                            </c:choose>
                        </p>
                        <div class="course-feedback-text"><c:out value="${feedback.description}" /></div>
                    </td>
                </tr>
                </tbody>
            </table>
        </c:forEach>
    </div>
</div>
