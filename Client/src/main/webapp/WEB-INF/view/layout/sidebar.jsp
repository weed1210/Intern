<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<c:set var="contextPath" value="${pageContext.request.contextPath }" />
<c:set var="resourcePath" value="${contextPath }/resources" />
<c:set var="memberRole" value="${sessionScope.memberRole }" />

<aside class="left-sidebar" data-sidebarbg="skin6">
	<!-- Sidebar scroll-->
	<div class="scroll-sidebar">
		<!-- Sidebar navigation-->
		<nav class="sidebar-nav">
			<ul id="sidebarnav">
				<li class="sidebar-item"><a
					class="sidebar-link waves-effect waves-dark sidebar-link"
					href="${contextPath }/" aria-expanded="false"> <i
						class="fa fa-table" aria-hidden="true"></i> <span
						class="hide-menu">ホーム</span>
				</a></li>
				<c:if test="${!memberRole.equals('ROLE_VIEW') }">
					<li class="sidebar-item"><a
						class="sidebar-link waves-effect waves-dark sidebar-link"
						href="${contextPath }/register" aria-expanded="false"> <i
							class="fa fa-user" aria-hidden="true"></i> <span
							class="hide-menu">追加</span>
					</a></li>
				</c:if>
				<li class="text-center p-20 upgrade-btn"><a
					href="${contextPath }/logout"
					class="btn d-grid btn-danger text-white" target="_blank">ログアウト</a></li>
			</ul>

		</nav>
		<!-- End Sidebar navigation -->
	</div>
	<!-- End Sidebar scroll-->
</aside>