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
		<c:import url="layout/loginHeader.jsp">
			<c:param name="loginError" value="${loginError }" />
		</c:import>
		<div class="page-wrapper">
			<div class="container-fluid">
				<div class="col-lg-12 col-xlg-12 col-md-12">
					<div class="card">
						<div class="card-body">
							<form action="${contextPath }/login"
								class="form-horizontal form-material" method="post">
								<div class="form-group mb-4 row">
									<label class="col-md-2 p-0 text-start box-title">ログインID</label>
									<div class="col-md-10">
										<input name="username" type="text"
											class="form-control p-0 border-0" /> <br />
									</div>
								</div>
								<div class="form-group mb-4 row">
									<label for="example-email"
										class="col-md-2 p-0 text-start box-title">パスワード</label>
									<div class="col-md-10">
										<input name="password" type="password"
											class="form-control p-0 border-0" /> <br />
									</div>
								</div>
								<div class="form-group mb-4">
									<div class="d-flex justify-content-center">
										<div class="upgrade-btn d-flex justify-content-center col-5">
											<button type="submit" style="width: 100%"
												class="btn submit px-3">ログイン</button>
										</div>
									</div>
								</div>
							</form>
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

