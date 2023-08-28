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
		<c:import url="layout/header.jsp" />
		<c:import url="layout/sidebar.jsp" />
		<div class="page-wrapper">
			<div class="container-fluid">
				<form:form modelAttribute="member"
					class="form-horizontal form-material"
					action="${contextPath }/update" method="post">
					<div class="col-lg-8 col-xlg-9 col-md-12">
						<h3 class="box-title">
							会員を編集します。 <input hidden name="email" type="email"
								value="${member.email }" />
						</h3>
						<div class="card">
							<div class="card-body">
								<input name="id" type="hidden" value="${member.id }"
									class="form-control p-0 border-0" />
								<div class="form-group mb-4">
									<label class="col-md-12 p-0">名前</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="username" type="text"
											value="${member.username }" class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="username" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4">
									<label class="col-md-12 p-0">パスワード</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="password" type="password"
											placeholder="新しいパスワード" class="form-control p-0 border-0" />
										<form:errors class="text-danger" path="password" />
										<br />
									</div>
								</div>
								<div class="form-group mb-4">
									<label class="col-md-12 p-0">電話番号</label>
									<div class="col-md-12 border-bottom p-0">
										<form:input path="phoneNumber" type="text"
											value="${member.phoneNumber }"
											class="form-control p-0 border-0" />
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
												<option ${member.role.equals('ROLE_ADMIN')? 'selected': '' }>ROLE_ADMIN</option>
											</c:if>
											<option ${member.role.equals('ROLE_EDIT')? 'selected': '' }>ROLE_EDIT</option>
											<option ${member.role.equals('ROLE_VIEW')? 'selected': '' }>ROLE_VIEW</option>
										</select>
									</div>
								</div>
								<div class="form-group mb-4">
									<div class="col-sm-12 row">
										<div class="col-sm-9">
											<button class="btn btn-success">更新</button>
										</div>
										<div class="col-sm-3">
											<button
												formaction="${contextPath }/confirmDelete/${member.id}"
												formmethod="get" class="btn btn-danger">削除</button>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</form:form>
			</div>
			<c:import url="layout/footer.jsp" />
		</div>
	</div>
	<c:import url="layout/script.jsp" />
</body>

</html>