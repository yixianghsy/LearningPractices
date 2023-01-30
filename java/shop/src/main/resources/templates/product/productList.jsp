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
        <c:forEach items="${page.result}" var="product">
            <c:set var="task" value="${leave.task }"/>
            <c:set var="pi" value="${leave.processInstance }"/>
            <div class="col-md-3 col-sm-4 col-xs-6 text-center">
                <a href="${ctx}/product/${product.id}"><img class="img-thumbnail" src="${ctx}${product.masterPic.url}"
                                                            style="width: 140px; height: 140px;"></a>

                <p>${product.title}</p>

                <p class="price">${product.point}</p>

                <p>
                    <a class="btn btn-info" href="${ctx}/product/${product.id}" role="button">查看</a>
                    <a class="btn btn-primary addCart" productid="${product.id}" role="button">购买</a>
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
<script src="${ctx }/js/product.js" type="text/javascript"></script>
</body>
</html>