<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>

<% String message = (String)request.getAttribute("alertMsg");%>
<script type="text/javascript">
    var msg = "<%=message%>";
    if (msg !== "null" && msg !== ""){
        alert(msg);
    }
</script>

<div class="card small-center-block">
    <div class="card-header">
        <i class="fa fa-sign-in"></i> Восстановление доступа в Ваш личный кабинет
    </div>
    <div class="card-body">
        <form action="/password-reset" method="post">
            <div class="form-group">
                <label for="mailOrLogin">Email или login</label> <input id="mailOrLogin" type="mail" name="mailOrLogin" class="form-control" required>
            </div>
            <div class="form-group" style="display:table;width:100%;">
                <button type="submit" class="btn btn-primary pull-left">Отправить</button>
            </div>
        </form>
    </div>
</div>
