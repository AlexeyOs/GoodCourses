<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib uri = "jakarta.tags.functions" prefix = "fn" %>

<c:if test = "${!fn:contains(requestScope['jakarta.servlet.forward.request_uri'], 'courses')}">
    <li class="nav-item">
        <a href="/courses" class="nav-link active">Список курсов</a>
    </li>
</c:if>
