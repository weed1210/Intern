<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="searchString" value="${sessionScope.searchString }" />

<header class="topbar" data-navbarbg="skin5">
	<nav class="navbar top-navbar navbar-expand-md navbar-dark">
		<div class="navbar-collapse collapse" id="navbarSupportedContent"
			data-navbarbg="skin5">
			<ul class="navbar-nav d-none d-md-block d-lg-none">
				<li class="nav-item"><a
					class="nav-toggler nav-link waves-effect waves-light text-white"
					href="javascript:void(0)"><i class="ti-menu ti-close"></i></a></li>
			</ul>
			<!-- ============================================================== -->
			<!-- Right side toggle and nav items -->
			<!-- ============================================================== -->
			<ul class="navbar-nav ms-auto d-flex align-items-center">

				<!-- ============================================================== -->
				<!-- Search -->
				<!-- ============================================================== -->
				<li class=" in">
					<form action="${contextPath }/" id="search_form"
						class="app-search d-none d-md-block me-3">
						<input type="text" placeholder="会員を検索します..." name="searchString"
							value="${searchString }" class="form-control mt-0"> <a
							href="javascript:{}"
							onclick="document.getElementById('search_form').submit();"> <i
							class="fa fa-search"></i>
						</a>
					</form>
				</li>
			</ul>
		</div>
	</nav>
</header>