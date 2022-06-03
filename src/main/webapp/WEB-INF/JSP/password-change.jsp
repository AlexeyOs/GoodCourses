<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<% String message = (String)request.getAttribute("alertMsg");%>
<script type="text/javascript">
    var msg = "<%=message%>";
    if (msg !== "null" && msg !== ""){
        alert(msg);
    }

    function checkAndSendPassword() {
        if ($('#password').val() !== $('#repeatPassword').val()) {
            //TODO хардкод, нужно убрать
            alert("Пароли не совпадают");
        } else {
            const urlForSend = "/password-change";
            const urlCurrentString = window.location.href;
            const urlCurrent = new URL(urlCurrentString);
            $.ajax({
                type: "POST",
                url: urlForSend,
                data: {
                    'profileId': urlCurrent.searchParams.get("profileId"),
                    'token': urlCurrent.searchParams.get("token"),
                    'password': $('#password').val()
                },
                enctype: "multipart/form-data",
                success: [function () {
                    window.location.replace("/sign-in");
                }],
                error: [function (request) {
                    alert(request.responseText);
                }]
            });
            //TODO убрать хардкод в свойства
            alert("Попробуйте войти с новым паролем")
        }
    }
</script>

<div class="card small-center-block">
    <div class="card-header">
        <i class="fa fa-sign-in"></i> Задайте новый пароль
    </div>
    <div class="card-body">
        <form action="javascript:checkAndSendPassword()" method="post">
            <div class="form-group">
                <label for="password">Введите пароль</label> <input id="password" type="password" name="password" class="form-control" required>
            </div>
            <div class="form-group">
                <label for="repeatPassword">Пароль повторно</label> <input id="repeatPassword" type="password" name="repeatPassword" class="form-control" required>
            </div>
            <div class="form-group" style="display:table;width:100%;">
                <button type="submit" class="btn btn-primary pull-left">Отправить</button>
            </div>
        </form>
    </div>
</div>