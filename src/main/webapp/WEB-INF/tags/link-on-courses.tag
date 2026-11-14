<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib uri = "jakarta.tags.functions" prefix = "fn" %>

<c:if test = "${!fn:contains(requestScope.REQUEST_URL, 'courses')}">
    <li class="nav-item">
        <a href="${pageContext.request.contextPath}/courses" class="nav-link active">Список курсов</a>
    </li>
</c:if>
