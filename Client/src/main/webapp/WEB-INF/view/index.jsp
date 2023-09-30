<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="memberRole" value="${sessionScope.memberRole }" />
<c:set var="memberEmail" value="${sessionScope.memberEmail }" />
<c:set var="searchString" value="${sessionScope.searchString }" />
<c:set var="maxPage" value="${5 }" />
<c:set var="halfPage" value="${3 }" />
<c:set var="currentPage" value="${members.number + 1 }" />
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
		<c:import url="layout/indexHeader.jsp" />
		<div class="page-wrapper">
			<div class="container-fluid" style="height: 91vh;">
				<!-- ============================================================== -->
				<!-- Start Page Content -->
				<!-- ============================================================== -->
				<div class="row">
					<div class="col-sm-12">
						<div>
							<div class="box-title">名前で会員を検索します。</div>
							<div class="row mb-3">
								<form action="${contextPath }/" id="search_form">
									<input type="text" name="searchString" value="${searchString }"
										class="search-input col-2">
									<button class="btn col-1">検索</button>
								</form>
							</div>
							<div class="table-responsive">
								<c:choose>
									<c:when test="${members.getNumberOfElements() > 0 }">
										<table class="table text-nowrap table-bordered">
											<thead>
												<tr>
													<th class="border-top-0">Id</th>
													<th class="border-top-0">名前</th>
													<th class="border-top-0">メール</th>
													<th class="border-top-0">電話番号</th>
													<th class="border-top-0">役割</th>
												</tr>
											</thead>
											<tbody>
												<c:if test="${!memberRole.contains('ROLE_VIEW') }">
													<c:forEach var="member" items="${members.content}"
														varStatus="status">
														<c:if
															test="${memberRole.contains('ROLE_ADMIN') || memberRole.contains('ROLE_EDIT') && !member.role.contains('ROLE_ADMIN') }">
															<tr>
																<td><a href="${contextPath }/update/${member.id }">
																		${status.index+1 + members.size*members.number } </a></td>
																<td>${fn:escapeXml(member.username) }</td>
																<td>${fn:escapeXml(member.email) }</td>
																<td>${member.phoneNumber.replaceFirst("(\\d{4})(\\d{3})(\\d+)", "$1-$2-$3") }</td>
																<td>${member.role }</td>
															</tr>
														</c:if>
													</c:forEach>
												</c:if>
											</tbody>
										</table>
										<div class="container pagination-container">
											<ul class="pagination justify-content-end pagination-sm">
												<c:if test="${members.totalPages > 0}">
													<c:forEach var="pageNumber" begin="1"
														end="${members.totalPages}">
														<!-- First three page, no previous button -->
														<c:if test="${currentPage <= halfPage }">
															<!-- Display first 5 page -->
															<c:if test="${pageNumber <= maxPage }">
																<c:set var="isActive"
																	value="${pageNumber == currentPage}" />
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${pageNumber}" />
																	<c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link"> <c:out value="${pageNumber}" />
																</a></li>
															</c:if>
															<!-- Page 6 will become next button -->
															<c:if
																test="${pageNumber == 1+maxPage && pageNumber < members.totalPages }">
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${currentPage+1}" />
											                        <c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link">&gt&gt</a></li>
															</c:if>
														</c:if>
														<!-- between first and three page, previous and next button, current page at middle -->
														<c:if
															test="${currentPage > halfPage && currentPage <= members.totalPages - halfPage }">
															<!-- Current page -3 as previous button -->
															<c:if
																test="${pageNumber == currentPage-halfPage && pageNumber > 0 }">
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${currentPage-1}" />
											                        <c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link">&lt&lt</a></li>
															</c:if>
															<!-- Display current page and 2 page before and after -->
															<c:if
																test="${pageNumber < (currentPage + halfPage) && pageNumber > (currentPage - halfPage) }">
																<c:set var="isActive"
																	value="${pageNumber == currentPage}" />
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${pageNumber}" />
											                        <<c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link"> <c:out value="${pageNumber}" />
																</a></li>
															</c:if>
															<!-- Current page +3 as next button -->
															<c:if
																test="${pageNumber == currentPage+halfPage && pageNumber <= members.totalPages }">
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${currentPage+1}" />
											                        <c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link">&gt&gt</a></li>
															</c:if>
														</c:if>
														<!-- Last 5 page -->
														<c:if
															test="${currentPage > members.totalPages - halfPage }">
															<c:if
																test="${pageNumber > members.totalPages - maxPage }">
																<c:set var="isActive"
																	value="${pageNumber == currentPage}" />
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${pageNumber}" />
											                        <c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link"> <c:out value="${pageNumber}" />
																</a></li>
															</c:if>
															<c:if
																test="${pageNumber == members.totalPages-maxPage && pageNumber > 0 }">
																<li class="page-item ${isActive ? 'active' : ''}"><a
																	href="<c:url value='/'>
											                        <c:param name="page" value="${currentPage-1}" />
											                        <c:if test="${not empty searchString }">
																		<c:param name="searchString" value="${searchString}" />
																	</c:if>
											                    </c:url>"
																	class="page-link">&lt&lt</a></li>
															</c:if>
														</c:if>
													</c:forEach>
												</c:if>
											</ul>
										</div>
									</c:when>
									<c:otherwise>
										<div class="error-message">データなし</div>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</div>
				<!-- ============================================================== -->
				<!-- End Page Content -->
			</div>
		</div>
	</div>
	<c:import url="layout/script.jsp" />
	<c:if test="${not empty getInfoError}">
		<script>
			$(document).ready(function() {
				toastr.error('${getInfoError}', 'エラー f');
			});
		</script>
	</c:if>

	<c:if test="${not empty successMessage}">
		<script>
			$(document).ready(function() {
				toastr.success('${successMessage }', '成功');
			});
		</script>
	</c:if>

	<c:if test="${not empty serverError}">
		<script>
			$(document).ready(function() {
				toastr.error('${serverError }', 'エラー');
			});
		</script>
	</c:if>
</body>

</html>