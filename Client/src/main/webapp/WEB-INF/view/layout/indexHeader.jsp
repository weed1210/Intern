<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="searchString" value="${sessionScope.searchString }" />

<header class="topbar" data-navbarbg="skin5" style="padding-top: 20px">
	<div>
		<div class="row">
			<div class="col-md-2 col-sm-3 col-xs-4 box-title ms-4">
				<c:if test="${!memberRole.equals('ROLE_VIEW') }">新規会員追加</c:if>
			</div>
			<div class="col-sm-2 col-xs-3" style="width: 150px">
				<c:if test="${!memberRole.equals('ROLE_VIEW') }">
					<a href="${contextPath }/register" class="btn d-grid">追加</a>
				</c:if>
			</div>
			<!-- ============================================================== -->
			<!-- Right side toggle and nav items -->
			<!-- ============================================================== -->
			<div
				class="ms-auto offset-sm-3 offset-xs-0 col-xs-4 col-sm-3 col-md-2 me-5"
				style="width: 200px">
				<div class="text-center upgrade-btn">
					<a href="${contextPath }/logout" class="btn d-grid">ログアウト</a>
				</div>
			</div>
		</div>
	</div>
</header>