<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />

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
			<c:param name="pageHeader" value="${title }" />
		</c:import>
		<div class="page-wrapper">
			<div class="container-fluid">
				<div class="row">
					<div class="row">
						<div class="col-2"></div>
						<p class="box-title prompt col-6 ms-4">${prompt }</p>
					</div>
					<div class="col-md-6 mx-auto">
						<div>
							<form class="cform-horizontal form-material"
								action="${contextPath }/${confirmAction }" method="post">
								<table>
									<input id="id" name="id" type="hidden" value="${member.id }"
										class="form-control p-0 border-0">
									<div class="form-group mb-4">
										<label class="col-md-12 p-0">名前</label>
										<div class="col-md-12 border-bottom p-0">
											<input readonly name="username" type="text"
												placeholder="Username" value="${member.username }"
												class="form-control p-0 border-0">
										</div>
									</div>
									<div class="form-group mb-4">
										<label for="example-email" class="col-md-12 p-0">メール</label>
										<div class="col-md-12 border-bottom p-0">
											<input readonly name="email" type="email"
												placeholder="johnathan@admin.com" value="${member.email }"
												class="form-control p-0 border-0" name="example-email">
										</div>
									</div>
									<%-- <div class="form-group mb-4">
									<label class="col-md-12 p-0">パスワード</label>
									<div class="col-md-12 border-bottom p-0">
										<input readonly name="password" type="password"
											placeholder="古いパスワード" value="${member.password }"
											class="form-control p-0 border-0">
									</div>
								</div> --%>
									<input readonly name="password" type="password" hidden
										placeholder="古いパスワード" value="${member.password }"
										class="form-control p-0 border-0">
									<div class="form-group mb-4">
										<label class="col-md-12 p-0">電話番号</label>
										<div class="col-md-12 border-bottom p-0">
											<input readonly name="phoneNumber" type="text"
												placeholder="123 456 7890" value="${member.phoneNumber }"
												class="form-control p-0 border-0">
										</div>
									</div>
									<div class="form-group mb-4">
										<label class="col-md-12 p-0">役割</label>
										<div class="col-md-12 border-bottom p-0">
											<input readonly name="role" type="text"
												placeholder="123 456 7890" value="${member.role }"
												class="form-control p-0 border-0">
										</div>
									</div>
									<div class="form-group mb-4">
										<div class="col-sm-12 row">
											<div class="col-sm-9">
												<button class="btn btn-success">確認</button>
											</div>
											<div class="col-sm-3">
												<button formaction="${contextPath }/${cancelAction }"
													formmethod="get" class="btn btn-danger">戻る</button>
											</div>
										</div>
									</div>
								</table>
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
				<div class="modal-body">
					お客様のリクエストは実行できません。 <br /> ${confirmError }
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-danger"
						data-bs-dismiss="modal">近い</button>
				</div>
			</div>
		</div>
	</div>
</body>
<c:import url="layout/script.jsp" />
<c:if test="${not empty confirmError}">
	<script>
		const myModal = new bootstrap.Modal(document
				.getElementById('errorModal'));
		myModal.show();
	</script>
</c:if>

</html>