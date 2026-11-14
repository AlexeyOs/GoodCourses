<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri = "jakarta.tags.functions" prefix = "fn" %>

<security:authorize access= "hasAuthority('USER')" var= "isUSer"/>

<script>
    //Функция отображения PopUp
    function PopUpShow(){
        document.getElementById("popup0").hidden = true;
        document.getElementById("popup1").hidden = false;
    }
    //Функция скрытия PopUp
    function PopUpHide(){
        document.getElementById("popup0").hidden = false;
        document.getElementById("popup1").hidden = true;
    }
</script>


<c:if test= "${not isUSer}">
    <!--TODO попробовать реализовать исключения для отображения кнопки для адресов sign-in и sign-up средствами spring-security-->
    <c:if test = "${!fn:contains(requestScope.REQUEST_URL, 'sign-in') &&
    !fn:contains(requestScope.REQUEST_URL, 'sign-up')}">
        <li class="nav-item">
            <a href="${pageContext.request.contextPath}/sign-in" class="nav-link">Авторизоваться</a>
        </li>
    </c:if>
</c:if>

<c:if test= "${isUSer}">
    <li class="nav-item">
        <div class="b-container" id="popup0">
            <a href="javascript:PopUpShow()">Меню</a>
        </div>
        <div class="b-popup" id="popup1" hidden>
            <c:if test="${profile == null}">
                <div class="b-popup-content">
                    <a href="${pageContext.request.contextPath}/my-profile" >Перейти на страницу профиля</a>
                </div>
            </c:if>
            <div class="b-popup-content">
                <a href="${pageContext.request.contextPath}/my-reviews">Мои отзывы</a>
            </div>
            <div class="b-popup-content">
                <form action="${pageContext.request.contextPath}/sign-out" method="post">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                    <button type="submit" class="btn btn-link p-0">Выйти</button>
                </form>
            </div>
            <div class="b-popup-content">
                <a href="javascript:PopUpHide()">Свернуть</a>
            </div>
        </div>
    </li>
</c:if>
