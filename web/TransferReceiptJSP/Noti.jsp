<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi" class="light-style layout-menu-fixed" dir="ltr" data-theme="theme-default" data-assets-path="../assets/" data-template="vertical-menu-template-free">

    <head>
        <jsp:include page="LinkCSS.jsp" />
        <title>Thông báo</title>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="../sidebar.jsp" />
                <div class="layout-page">
                    <jsp:include page="../navBar.jsp" />
                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <div class="card">
                                <div class="card-header d-flex justify-content-between align-items-center">
                                    <h5 class="mb-0">Thông báo</h5>
                                    <c:if test="${sizeNoti > 0}">
                                        <a href="#" class="btn btn-outline-primary btn-sm" onclick="markAllAsRead()">Đánh dấu tất cả là đã đọc</a>
                                    </c:if>
                                </div>

                                <div class="card-body">
                                    <ul class="nav nav-tabs mb-3" role="tablist">
                                        <li class="nav-item">
                                            <button class="nav-link ${currentTab == 'all' || currentTab == null ? 'active fw-bold' : ''}" 
                                                    onclick="loadTab('all')" role="tab">

                                                Tất cả
                                            </button>
                                        </li>
                                        <li class="nav-item">
                                            <button class="nav-link ${currentTab == 'unread' ? 'active fw-bold' : ''}" 
                                                    onclick="loadTab('unread')" role="tab">

                                                Chưa đọc
                                                <c:if test="${sizeNoti > 0}">
                                                    <span class="badge bg-danger ms-2">${sizeNoti}</span>
                                                </c:if>
                                            </button>
                                        </li>
                                    </ul>

                                    <div class="tab-content">
                                        <div class="tab-pane fade show active" role="tabpanel">
                                            <div id="notification-content">
                                                <c:choose>
                                                    <c:when test="${empty vectorNoti}">
                                                        <div class="text-center py-5">
                                                            <c:choose>
                                                                <c:when test="${currentTab == 'unread'}">
                                                                    <i class="bx bx-check-circle display-3 text-success"></i>
                                                                    <p class="text-muted mt-3 mb-0">Bạn đã đọc hết tất cả thông báo.</p>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <i class="bx bx-bell display-3 text-muted"></i>
                                                                    <p class="text-muted mt-3 mb-0">Không có thông báo nào</p>
                                                                </c:otherwise>
                                                            </c:choose>
                                                        </div>
                                                    </c:when>
                                                    <c:otherwise>
                                                        <c:forEach var="n" items="${vectorNoti}">
                                                            <a href="NotiController?service=SetIsRead&NotiID=${n.notiID}&link=${n.link}" 
                                                               class="text-dark text-decoration-none">
                                                                <div class="d-flex align-items-start border-bottom py-3 px-2 ${n.isRead == 0 ? 'bg-light' : ''} notification-item">

                                                                    <img src="img/logoSale.png" class="rounded-circle me-3" width="48" height="48">
                                                                    <div class="flex-grow-1">
                                                                        <h6 class="mb-1 ${n.isRead == 0 ? 'fw-bold text-primary' : ''}">${n.title}</h6>
                                                                        <p class="mb-1">${n.message}</p>
                                                                        <small class="text-muted">
                                                                            <c:forEach var="m" items="${mapNotiDate}">
                                                                                <c:if test="${m.key eq n.notiID}">
                                                                                    <c:choose>
                                                                                        <c:when test="${m.value == 0}">Vừa xong</c:when>
                                                                                        <c:when test="${m.value < 60}">${m.value} phút trước</c:when>
                                                                                        <c:when test="${m.value < 1440}">${m.value / 60} giờ trước</c:when>
                                                                                        <c:otherwise>${n.createdDate}</c:otherwise>
                                                                                    </c:choose>
                                                                                </c:if>
                                                                            </c:forEach>
                                                                        </small>
                                                                    </div>
                                                                    <c:if test="${n.isRead == 0}">
                                                                        <span class="badge bg-primary rounded-circle" style="width:10px;height:10px;"></span>
                                                                    </c:if>
                                                                </div>
                                                            </a>
                                                        </c:forEach>
                                                    </c:otherwise>
                                                </c:choose>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </div>

        <!-- Loading overlay -->
        <div id="loading-overlay" class="d-none position-fixed top-0 start-0 w-100 h-100" 
             style="background: rgba(255,255,255,0.8); z-index: 9999;">
            <div class="d-flex justify-content-center align-items-center h-100">
                <div class="spinner-border text-primary" role="status">
                    <span class="visually-hidden">Đang tải...</span>
                </div>
            </div>
        </div>

        <jsp:include page="LinkJS.jsp" />
        <script>
            let currentTab = '${currentTab != null ? currentTab : "all"}';

            function showLoading() {
                document.getElementById('loading-overlay').classList.remove('d-none');
            }

            function hideLoading() {
                document.getElementById('loading-overlay').classList.add('d-none');
            }

            function loadTab(tab) {
                if (currentTab === tab)
                    return; // Không load lại nếu đã ở tab đó

                currentTab = tab;
                showLoading();

                let url = 'NotiController?service=';
                if (tab === 'all') {
                    url += 'LoadAll';
                } else if (tab === 'unread') {
                    url += 'LoadUnread';
                }

                // Update active tab immediately for better UX
                document.querySelectorAll('.nav-link').forEach(link => {
                    link.classList.remove('active');
                });
                event.target.classList.add('active');

                window.location.href = url;
            }


            function markAllAsRead() {
                if (confirm('Bạn có chắc chắn muốn đánh dấu tất cả thông báo là đã đọc?')) {
                    showLoading();

                    // Update UI immediately
                    document.querySelectorAll('.notification-item').forEach(el => {
                        el.classList.remove('bg-light');
                        const badge = el.querySelector('.badge.bg-primary.rounded-circle');
                        if (badge)
                            badge.remove();
                        const title = el.querySelector('h6');
                        if (title) {
                            title.classList.remove('fw-bold', 'text-primary');
                        }
                    });

                    fetch('NotiController?service=SetAllRead', {
                        method: 'GET'
                    }).then(response => {
                        if (response.ok) {
                            setTimeout(() => {
                                window.location.href = 'NotiController?service=LoadAll';
                            }, 500);
                        }
                    }).catch(error => {
                        console.error('Error:', error);
                        location.reload();
                    });
                }
            }

            // Auto-hide loading after page load
            document.addEventListener('DOMContentLoaded', function () {
                hideLoading();
            });
        </script>
    </body>
</html>