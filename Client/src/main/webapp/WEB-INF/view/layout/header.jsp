<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="searchString" value="${sessionScope.searchString }" />

<header data-navbarbg="skin5" style="padding-top: 20px">
	<div>
		<div class="row" data-navbarbg="skin5">
			<div class="col-md-2 col-sm-1"></div>
			<div class="col-md-3 col-sm-3 ms-5 box-title">${param.pageHeader }</div>
			<div class="col-md-3 col-sm-3"></div>
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