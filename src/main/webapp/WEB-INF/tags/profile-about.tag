<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:if test="${not empty profile.aboutMe or canEdit}">
    <div class="card">
        <div class="card-header">
            <i class="fa fa-user"></i> О себе
            <c:if test="${canEdit}">
                <a class="btn btn-primary btn-sm pull-right"
                   href="${pageContext.request.contextPath}/edit/about">
                    <i class="fa fa-pencil"></i> Редактировать
                </a>
            </c:if>
        </div>
        <div class="card-body">
            <c:choose>
                <c:when test="${empty profile.aboutMe}">
                    <p class="text-muted">Расскажите немного о себе.</p>
                </c:when>
                <c:otherwise>
                    <p style="white-space: pre-wrap;"><c:out value="${profile.aboutMe}" /></p>
                </c:otherwise>
            </c:choose>
        </div>
    </div>
</c:if>
