<%@ tag pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>

<security:authorize access= "hasAuthority('USER') or hasAuthority('ADMIN')" var= "isUSer"/>

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
    <c:if test = "${!fn:contains(requestScope['javax.servlet.forward.request_uri'], 'sign-in') &&
    !fn:contains(requestScope['javax.servlet.forward.request_uri'], 'sign-up')}">
        <li class="nav-item">
            <a href="/sign-in" class="nav-link">Авторизоваться</a>
        </li>
    </c:if>
</c:if>

<c:if test= "${isUSer}">
    <li class="nav-item">
        <div class="b-container" id="popup0">
            <a href="javascript:PopUpShow()">Меню</a>
        </div>
        <div class="b-popup" id="popup1" hidden>
            <c:if test="${profile == null || fn:contains(requestScope['javax.servlet.forward.request_uri'], 'edit')}">
                <div class="b-popup-content">
                    <a href="/my-profile" >Перейти на страницу профиля</a>
                </div>
            </c:if>
            <div class="b-popup-content">
                <a href="/sign-out" >Выйти</a>
            </div>
            <div class="b-popup-content">
                <a href="javascript:PopUpHide()">Свернуть</a>
            </div>
        </div>
    </li>
</c:if>
