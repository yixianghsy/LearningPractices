<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <%@ include file="/common/global.jsp" %>
    <title>商品列表</title>
    <%@ include file="/common/meta.jsp" %>
    <%@ include file="/common/include-base-styles.jsp" %>
    <link type="text/css" rel="stylesheet" href="${ctx}/css/product.css"/>
</head>

<body>
<%@include file="/common/header-nav.jsp" %>
<div class="container">
    <div class="row">
        <c:forEach items="${page.result}" var="coupon">
            <c:set var="task" value="${leave.task}"/>
            <c:set var="pi" value="${leave.processInstance }"/>
            <div class="col-md-3 col-sm-4 col-xs-6 text-center">
                <img src="${ctx}${coupon.picUrl}" height="200" width="200"/>

                <p>${coupon.title}</p>

                <p class="price">${coupon.reduceAmount}</p>

                <p>
                    <a class="btn btn-info receiveCoupon" role="button" couponId="${coupon.id}">领取</a>
                </p>
            </div>
        </c:forEach>
    </div>
    <div class="text-center">
        <tags:pagination page="${page}" paginationSize="${page.pageSize}"/>
    </div>
</div>
<!-- /container -->
<%@include file="/common/footer.jsp" %>
<%@ include file="/common/include-base-js.jsp" %>
<script src="${ctx }/js/coupon.js" type="text/javascript"></script>
</body>
</html>