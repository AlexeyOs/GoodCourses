<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" 		uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form"   	uri="http://www.springframework.org/tags/form"%>

<div class="card">
    <div class="card-body">
        <form:form action="/edit/profile" method="post">
            <div class="form-group">
                <label for="summary">Редактировать информацию о себе</label>
                <input id="summary" name="summary" value="${profile.summary}" class="form-control">
            </div>
            <div class="form-group" style="display:table;width:100%;">
                <input type="submit" class="btn btn-primary" value="Сохранить">
            </div>
        </form:form>
    </div>
</div>