<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="memberRole" value="${sessionScope.memberRole }" />
<c:set var="memberEmail" value="${sessionScope.memberEmail }" />
<c:set var="searchString" value="${sessionScope.searchString }" />

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
				<!-- ============================================================== -->
				<!-- Start Page Content -->
				<!-- ============================================================== -->
				<div class="row">
					<div class="col-sm-12">
						<div class="white-box">
							<h3 class="box-title">会員管理</h3>
							<div class="table-responsive">
								<c:choose>
									<c:when test="${members.getNumberOfElements() > 0 }">
										<table class="table text-nowrap">
											<thead>
												<tr>
													<th class="border-top-0"></th>
													<th class="border-top-0">名前</th>
													<th class="border-top-0">メール</th>
													<th class="border-top-0">電話番号</th>
													<th class="border-top-0">役割</th>
													<c:if
														test="${memberRole.equals('ROLE_ADMIN') or memberRole.equals('ROLE_EDIT')}">
														<th class="border-top-0"></th>
													</c:if>
												</tr>
											</thead>
											<tbody>

												<c:forEach var="member" items="${members.content}">
													<tr>
														<td>${member.id }</td>
														<td>${member.username }</td>
														<td>${member.email }</td>
														<td>${member.phoneNumber }</td>
														<td>${member.role }</td>
														<c:if test="${!memberEmail.equals(member.email) }">
															<c:if
																test="${sessionScope.memberRole.contains('ROLE_ADMIN') }">
																<td><a href="${contextPath }/update/${member.id }">
																		<button type="button" class="btn btn-primary">更新</button>
																</a></td>
															</c:if>
															<c:if
																test="${sessionScope.memberRole.contains('ROLE_EDIT') and !member.role.equals('ROLE_ADMIN') }">
																<td><a href="${contextPath }/update/${member.id }">
																		<button type="button" class="btn btn-primary">更新</button>
																</a></td>
															</c:if>
														</c:if>
													</tr>
												</c:forEach>
											</tbody>
										</table>
										<div class="container">
											<ul class="pagination justify-content-center pagination-sm">
												<c:if test="${members.totalPages > 0}">
													<c:forEach var="pageNumber" begin="1" end="${members.totalPages}">
														<c:set var="isActive"
															value="${pageNumber == members.number+1}" />
														<li class="page-item ${isActive ? 'active' : ''}"><a
															href="<c:url value='/'>
											                        <c:param name="page" value="${pageNumber}" />
											                        <c:param name="searchString" value="${searchString}" />
											                    </c:url>"
															class="page-link"> <c:out value="${pageNumber}" />
														</a></li>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</c:when>
									<c:otherwise>データなし</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
				<!-- ============================================================== -->
				<!-- End Page Content -->
			</div>
			<c:import url="layout/footer.jsp" />
		</div>
	</div>
	<c:import url="layout/script.jsp" />
	<c:if test="${not empty getInfoError}">
		<script>
			$(document).ready(function() {
				toastr.error('${getInfoError}', 'Error');
			});
		</script>
	</c:if>
</body>

</html>