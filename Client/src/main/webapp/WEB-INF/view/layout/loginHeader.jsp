<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="searchString" value="${sessionScope.searchString }" />

<header class="topbar" data-navbarbg="skin5" style="padding-bottom: 10%">
	<nav class="navbar top-navbar navbar-expand-md navbar-dark">
		<div class="navbar-collapse collapse row" id="navbarSupportedContent"
			data-navbarbg="skin5">
			<div class="col-2"></div>
			<div class="col-5 ms-5 box-title">ログインID及びパスワードを入力して下さい。</div>
		</div>
	</nav>
	<nav class="navbar top-navbar navbar-expand-md navbar-dark">
		<div class="navbar-collapse collapse row" id="navbarSupportedContent"
			data-navbarbg="skin5">
			<div class="col-2"></div>
			<div class="col-5 ms-5 box-title" style="color: red;">${param.loginError }</div>
		</div>
	</nav>
</header>