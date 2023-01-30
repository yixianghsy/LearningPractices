//领取优惠券功能
$(".receiveCoupon").click(function () {
    var couponId = $(this).attr("couponId");
    $.ajax({
        url: ctx + "/coupon/saveUserCoupon/",
        method: "post",
        data: {
            "couponId": couponId,
            "userId": 1
        },
        success: function (result) {
            alert(result)
            // if(result.status=="SUCCESS"){
            //     $('#addAddressSuccess').show();
            //     toastr.info("添加成功");
            // } else {
            //     toastr.error(result.message);
            // }
        },
        error: function () {
            toastr.error("发生错误...");
        }
    })
});