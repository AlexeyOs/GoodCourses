<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<security:authorize access= "hasAuthority('USER')" var= "isUSer"/>


<c:if test= "${not isUSer}">
    <!--TODO попробовать реализовать исключения для отображения кнопки для адресов sign-in и sign-up средствами spring-security-->
    <c:if test = "${!fn:contains(requestScope['javax.servlet.forward.request_uri'], 'sign-in') &&
    !fn:contains(requestScope['javax.servlet.forward.request_uri'], 'sign-up')}">
        <div id="signIn" class="col-xs-12 text-right">
            <a href="/sign-in" class="btn btn-primary">Авторизоваться</a>
        </div>
    </c:if>
</c:if>

<c:if test= "${isUSer}">
    <c:choose>
        <c:when test="${profile != null}">
            <%--Заглушка для страницы профиля--%>
        </c:when>
        <c:otherwise>
            <div id="myProfile" class="col-xs-16 text-right">
                <a href="/my-profile" class="btn btn-primary">Мой профиль</a>
            </div>
        </c:otherwise>
    </c:choose>
</c:if>
