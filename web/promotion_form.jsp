<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<c:if test="${empty sessionScope.Employee}">
    <c:redirect url="loginEmployee.jsp"/>
</c:if>
<html lang="en"
      class="light-style layout-menu-fixed"
      dir="ltr"
      data-theme="theme-default"
      data-assets-path="${pageContext.request.contextPath}/assets/"
      data-template="vertical-menu-template-free">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>${promotion != null ? 'Cập nhật' : 'Thêm mới'} chương trình khuyến mãi</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/core.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/css/theme-default.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/css/demo.css" />
        <link rel="stylesheet" href="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.css" />
        <script src="${pageContext.request.contextPath}/assets/vendor/js/helpers.js"></script>
    </head>

    <body>
        <div class="layout-wrapper layout-content-navbar">
            <div class="layout-container">
                <jsp:include page="sidebar.jsp" />

                <div class="layout-page">
                    <jsp:include page="navBar.jsp" />

                    <div class="content-wrapper">
                        <div class="container-xxl flex-grow-1 container-p-y">
                            <h4 class="fw-bold py-3 mb-4">
                                <span class="text-muted fw-light">Quản lý /</span> ${promotion != null ? 'Cập nhật' : 'Thêm mới'} khuyến mãi
                            </h4>

                            <div class="card mb-4">
                                <div class="card-header">
                                    <h5 class="mb-0">${promotion != null ? 'Cập nhật' : 'Thêm mới'} chương trình</h5>
                                </div>
                                <div class="card-body">
                                    <form action="PromotionServlet" method="post">
                                        <input type="hidden" name="action" value="${promotion != null ? 'update' : 'insert'}" />
                                        <c:if test="${promotion != null}">
                                            <input type="hidden" name="id" value="${promotion.promotionId}" />
                                        </c:if>

                                        <div class="mb-3">
                                            <label for="promotionName" class="form-label">Tên chương trình</label>
                                            <input type="text" class="form-control" id="promotionName" name="promotionName"
                                                   value="${promotion != null ? promotion.promotionName : ''}" required />
                                        </div>
                                        <div class="mb-3">
                                            <label for="categoryId" class="form-label">Danh mục áp dụng</label>
                                            <select class="form-select" id="categoryId" name="categoryId" required>
                                                <option value="">-- Chọn danh mục --</option>
                                                <c:forEach var="c" items="${categoryList}">
                                                    <option value="${c.categoryID}" ${promotion != null && promotion.categoryId == c.categoryID ? 'selected' : ''}>
                                                        ${c.categoryName}
                                                    </option>
                                                </c:forEach>
                                            </select>
                                        </div>
                                        <div class="mb-3">
                                            <label for="discountRate" class="form-label">Tỷ lệ giảm (%)</label>
                                            <input type="number" class="form-control" id="discountRate" name="discountRate"
                                                   value="${promotion != null ? promotion.discountRate : 0}"
                                                   min="1" max="99" required />
                                        </div>
                                        <div class="mb-3">
                                            <label for="startDate" class="form-label">Từ ngày</label>
                                            <input type="date" class="form-control" id="startDate" name="startDate"
                                                   value="<fmt:formatDate value='${promotion != null ? promotion.startDate : null}' pattern='yyyy-MM-dd'/>" required />
                                        </div>

                                        <div class="mb-3">
                                            <label for="endDate" class="form-label">Đến ngày</label>
                                            <input type="date" class="form-control" id="endDate" name="endDate"
                                                   value="<fmt:formatDate value='${promotion != null ? promotion.endDate : null}' pattern='yyyy-MM-dd'/>" required />
                                        </div>

                                        <div class="mb-3">
                                            <label for="status" class="form-label">Trạng thái</label>
                                            <select class="form-select" id="status" name="status" required>
                                                <option value="1" ${promotion != null && promotion.status ? 'selected' : ''}>Đang hoạt động</option>
                                                <option value="0" ${promotion != null && !promotion.status ? 'selected' : ''}>Không hoạt động</option>
                                            </select>
                                        </div>

                                        <button type="submit" class="btn btn-primary">${promotion != null ? 'Cập nhật' : 'Thêm mới'}</button>
                                        <a href="PromotionServlet" class="btn btn-secondary">Quay lại</a>
                                    </form>
                                </div>
                            </div>
                        </div>

                        <jsp:include page="footer.jsp" />
                        <div class="content-backdrop fade"></div>
                    </div>
                </div>
            </div>
        </div>

        <!-- JS -->
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/jquery/jquery.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/popper/popper.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/bootstrap.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/libs/perfect-scrollbar/perfect-scrollbar.js"></script>
        <script src="${pageContext.request.contextPath}/assets/vendor/js/menu.js"></script>
        <script src="${pageContext.request.contextPath}/assets/js/main.js"></script>
    </body>
</html>
