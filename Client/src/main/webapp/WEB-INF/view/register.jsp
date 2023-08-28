<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="memberRole" value="${sessionScope.memberRole }" />

<!DOCTYPE html>
<html dir="ltr" lang="en">

<c:import url="layout/head.jsp" />

<body>
	<div class="preloader">
		<div class="lds-ripple">
			<div class="lds-pos"></div>
			<div class="lds-pos"></div>
		</div>
	</div>
	<div id="main-wrapper" data-layout="vertical" data-navbarbg="skin5"
		data-sidebartype="full" data-sidebar-position="absolute"
		data-header-position="absolute" data-boxed-layout="full">
		<c:import url="layout/header.jsp" />
		<c:import url="layout/sidebar.jsp" />
		<div class="page-wrapper">
			<div class="container-fluid">
				<div class="col-lg-8 col-xlg-9 col-md-12">
					<h3 class="box-title">会員を登録します。</h3>
					<div class="card">
						<div class="card-body">
							<form:form modelAttribute="member"
								class="form-horizontal form-material"
								action="${contextPath }/register" method="post">
								<input name="id" type="hidden" value="0"
									class="form-control p-0 border-0" />
								<div class="form-group mb-4">
									<label class="col-md-12 p-0">名前</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="username" type="text"
											placeholder="Tran Van A" class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="username" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4">
									<label for="example-email" class="col-md-12 p-0">メール</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="email" type="email"
											placeholder="johnathan@admin.com"
											class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="email" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4">
									<label class="col-md-12 p-0">パスワード</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="password" type="password"
											placeholder="12345678" class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="password" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4">
									<label class="col-md-12 p-0">電話番号</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="phoneNumber" type="text"
											placeholder="123 456 7890" class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="phoneNumber" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4">
									<label class="col-sm-12">役割</label>
									<div class="col-sm-12 border-bottom">
										<select name="role"
											class="form-select shadow-none p-0 border-0 form-control-line">
											<c:if test="${memberRole.equals('ROLE_ADMIN') }">
												<option>ROLE_ADMIN</option>
												<option>ROLE_EDIT</option>
												<option>ROLE_VIEW</option>
											</c:if>
											<c:if test="${memberRole.equals('ROLE_EDIT') }">
												<option>ROLE_EDIT</option>
												<option>ROLE_VIEW</option>
											</c:if>
										</select>
									</div>
								</div>
								<div class="form-group mb-4">
									<div class="col-sm-12">
										<button class="btn btn-success">確認</button>
									</div>
								</div>
							</form:form>
						</div>
					</div>
				</div>
			</div>
			<c:import url="layout/footer.jsp" />
		</div>
	</div>
	<c:import url="layout/script.jsp" />
	<c:if test="${not empty registerError}">
		<script>
			$(document).ready(function() {
				toastr.error('${registerError}', 'Error');
			});
		</script>
	</c:if>
</body>

</html>