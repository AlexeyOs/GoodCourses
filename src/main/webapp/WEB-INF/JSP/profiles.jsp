<%@ page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="resume" tagdir="/WEB-INF/tags"%>

<div class="row profiles">
	<div id="profileContainer" class="col-md-6" data-profile-total="${page.totalPages }" data-profile-number="${page.number }">
		<jsp:include page="fragment/profile-items.jsp" />
	</div>
</div>
<c:if test="${page.number < page.totalPages - 1}">
	<br/>
	<div class="row">
		<div id="loadMoreContainer" class="col-md-6">
			<!--TODO убрать хардкод в конфиг-->
			<a href="javascript:resume.moreProfiles();" class="btn btn-primary">Загрузить ещё</a>
		</div>
		<div id="loadMoreIndicator" class="col-md-6 text-center" style="display:none;">
			<img src="${pageContext.request.contextPath}/static/img/large-loading.gif" alt="loading..."/>
		</div>
	</div>
</c:if>