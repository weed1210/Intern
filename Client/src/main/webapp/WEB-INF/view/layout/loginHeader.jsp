<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="searchString" value="${sessionScope.searchString }" />

<header class="topbar" data-navbarbg="skin5" style="padding-top: 20px">
	<div class="row" style="min-height: 35px">
		<div class="offset-sm-3 box-title">ログインID及びパスワードを入力して下さい。</div>
	</div>
	<div class="row" style="min-height: 35px">
		<div class="offset-sm-3" style="color: red; font-weight: 400">${param.loginError }</div>
	</div>
</header>