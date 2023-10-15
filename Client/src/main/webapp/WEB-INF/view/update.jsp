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
			<c:param name="pageHeader" value="会員を編集します。" />
		</c:import>
		<form:form modelAttribute="member"
			class="form-horizontal form-material" action="${contextPath }/update"
			method="post">
			<div class="page-wrapper">
				<div class="container-fluid">
					<div class="row">
						<div class="col-2"></div>
						<div class="box-title prompt col-6 ms-4">
							<form:errors class="text-danger" path="username" />
							<br />
							<form:errors class="text-danger" path="email" />
							<br />
							<form:errors class="text-danger" path="phoneNumber" />
							<br />
						</div>
					</div>
					<div class="card">
						<div class="card-body">
							<div class="col-lg-12 col-xlg-12 col-md-12">
								<div class="form-group mb-4 row">
									<label class="col-md-3 text-start box-title">ID</label>
									<div class="col-md-9">
										<input name="id" readonly type="text" value="${member.id }"
											class="form-control p-0 border-0 readonly" /> <br />
									</div>
								</div>
								<div class="form-group mb-4 row">
									<label class="col-md-3 text-start box-title">名前 <span
										class="compulsary">*</span></label>
									<div class="col-md-9">
										<form:input path="username" type="text"
											value="${member.username }" class="form-control p-0 border-0" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4 row">
									<label for="example-email"
										class="col-md-3 text-start box-title">メール <span
										class="compulsary">*</span>
									</label>
									<div class="col-md-9">
										<form:input path="email" type="email" value="${member.email }"
											class=" form-control p-0 border-0" />
										<br />
									</div>
								</div>
								<form:input path="password" type="password" hidden="hidden"
									placeholder="12345678" />
								<div class="form-group mb-4 row">
									<label class="col-md-3 text-start box-title">電話番号 <span
										class="compulsary">*</span></label>
									<div class="col-md-9">
										<form:input path="phoneNumber" type="text"
											value="${member.phoneNumber }"
											class="form-control p-0 border-0" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4 row">
									<label class="col-md-3 text-start box-title">役割</label>
									<div class="col-md-9">
										<select name="role"
											class="form-select shadow-none p-0 border-0 form-control-line">
											<c:if test="${memberRole.equals('ROLE_ADMIN') }">
												<option ${member.role.equals('ROLE_ADMIN')? 'selected': '' }
													value="ROLE_ADMIN">Admin</option>
											</c:if>
											<option ${member.role.equals('ROLE_EDIT')? 'selected': '' }
												value="ROLE_EDIT">Edit</option>
											<option ${member.role.equals('ROLE_VIEW')? 'selected': '' }
												value="ROLE_VIEW">View</option>
										</select>
									</div>
								</div>
								<div class="form-group mb-4 row">
									<div
										class="offset-md-3 col-md-9 d-flex">
										<div class="upgrade-btn d-flex col-4"
											style="padding: 0">
											<a href="${contextPath }/" class="btn" style="width: 120px">戻る</a>
										</div>
										<div class="d-flex justify-content-center col-4 p-0">
											<button
												formaction="${contextPath }/confirmDelete/${member.id}"
												formmethod="get" class="btn" style="width: 120px">削除</button>
										</div>
										<div class="d-flex justify-content-end col-4 p-0">
											<button class="btn" style="width: 120px">更新</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form:form>
	</div>
	<c:import url="layout/script.jsp" />
</body>

</html>