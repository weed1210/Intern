<%@page import="org.springframework.validation.BindingResult"%>
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
		<c:import url="layout/header.jsp">
			<c:param name="pageHeader" value="会員を登録します。" />
		</c:import>
		<div class="page-wrapper">
			<div class="container-fluid">
				<div class="col-lg-12 col-xlg-12 col-md-12">
					<div class="card">
						<div class="card-body">
							<form:errors class="text-danger" path="username" />
							<form:form modelAttribute="member"
								class="form-horizontal form-material"
								action="${contextPath }/register" method="post">
								<input name="id" type="hidden" value="0"
									class="form-control p-0 border-0" />
								<div class="form-group mb-4 row">
									<label class="col-md-2 p-0 text-start box-title">名前 <span
										class="compulsary">*</span></label>
									<div class="col-md-10">
										<form:input path="username" type="text"
											placeholder="Tran Van A" class="form-control p-0 border-0" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4 row">
									<label for="example-email"
										class="col-md-2 p-0 text-start box-title">メール <span
										class="compulsary">*</span>
									</label>
									<div class="col-md-10">
										<form:input path="email" type="email"
											placeholder="johnathan@admin.com"
											class="form-control p-0 border-0" />
										<br />
									</div>
								</div>
								<%--  div class="form-group mb-4 row">
									<label class="col-md-2 p-0 text-start box-title">パスワード <span
										class="compulsary">*</span></label>
									<div class="col-md-10">
										<form:input path="password" type="password"
											placeholder="12345678" class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="password" />
										<br />
									</div>
								</div> --%>
								<form:input path="password" type="password" hidden="hidden"
									value="12345678" placeholder="12345678" />
								<div class="form-group mb-4 row">
									<label class="col-md-2 p-0 text-start box-title">電話番号 <span
										class="compulsary">*</span></label>
									<div class="col-md-10">
										<form:input path="phoneNumber" type="text"
											placeholder="123 456 7890" class="form-control p-0 border-0" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4 row">
									<label class="col-sm-2 p-0 text-start box-title">役割</label>
									<div class="col-sm-10">
										<select name="role"
											class="form-select shadow-none border-0  form-control-line">
											<c:if test="${memberRole.equals('ROLE_ADMIN') }">
												<option value="ROLE_ADMIN">Admin</option>
												<option value="ROLE_EDIT">Edit</option>
												<option value="ROLE_VIEW">View</option>
											</c:if>
											<c:if test="${memberRole.equals('ROLE_EDIT') }">
												<option value="ROLE_EDIT">Edit</option>
												<option value="ROLE_VIEW">View</option>
											</c:if>
										</select>
									</div>
								</div>
								<div class="form-group mb-4">
									<div class="d-flex justify-content-center">
										<div class="upgrade-btn d-flex justify-content-center col-6">
											<a href="${contextPath }/" class="btn">戻る</a>
										</div>
										<div class="d-flex justify-content-center col-6">
											<button class="btn">確認</button>
										</div>
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

	<!-- Modal -->
	<div class="modal fade" id="errorModal" tabindex="-1"
		aria-labelledby="exampleModalLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="exampleModalLabel">サーバーがエラーを返しました。</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="Close"></button>
				</div>
				<div class="modal-body row">
					<img width="100" class="col-2"
						src="${resourcePath }/images/warning.png" />
					<div class="col-10">
						<c:forEach items="${errors}" var="error">
							<%-- do want you want with ${error} --%>
							<div>${error.defaultMessage}</div>
						</c:forEach>
					</div>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn modal-btn" data-bs-dismiss="modal">OK</button>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="layout/script.jsp" />
<c:if test="${not empty errors}">
	<script>
		const myModal = new bootstrap.Modal(document
				.getElementById('errorModal'));
		myModal.show();
	</script>
</c:if>
</html>