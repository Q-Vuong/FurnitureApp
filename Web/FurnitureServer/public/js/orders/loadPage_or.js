function loadContent(url) {
  $.get(url, function (data) {
    $("#content").html(data);
  });
}

$(document).on("click", ".eLink", function (event) {
  event.preventDefault();

  var href = $(this).attr("href");
  var orderId = href.substring(href.lastIndexOf("/") + 1);

  $.ajax({
    url: "order/detail/" + orderId,
    method: "GET",
    success: function (response) {
      var detailOrder = response.detailOrder;
      var orderDate = new Date(detailOrder.dateOrder);
      var formattedDate = `${orderDate.getDate()}/${orderDate.getMonth() + 1}/${orderDate.getFullYear()} ${orderDate.getHours()}:${orderDate.getMinutes()}:${orderDate.getSeconds()}`;

      $("#myModal .modal-title").text("Chi tiết đơn hàng #" + orderId);
      $("#myModal .modal-body").html(`
        <p><strong>Ngày đặt hàng:</strong> ${formattedDate}</p>
        <p><strong>Ghi chú:</strong> ${detailOrder.note}</p>
        <p><strong>Khách hàng:</strong> ${detailOrder.userId.fullName}</p>
        <p><strong>Địa chỉ:</strong> ${detailOrder.userId.address}</p>
        <p><strong>Số điện thoại:</strong> ${detailOrder.userId.phoneNumber}</p>
        <p><strong>Sản phẩm:</strong></p>
        <ul>
          ${detailOrder.products.map(product => `<li>${product.productId.namePr} - Số lượng: ${product.quantity}</li>`).join('')}
        </ul>
        <p><strong>Tổng tiền:</strong> ${detailOrder.priceToPay}</p>
      `);

      var confirmButtonHtml = ''; // Chuỗi HTML để chứa nút Xác nhận

      // Kiểm tra trạng thái confirmed của đơn hàng
      if (!detailOrder.confirmed) {
        confirmButtonHtml = `<a href="order/confirm/${orderId}" data-bs-toggle="modal" data-bs-target="#myModal" class="btn btn-primary btn-sm">Xác nhận</a>`;
      }

      $("#myModal .modal-footer").html(`
        ${confirmButtonHtml}
        <button type="button" class="btn btn-danger" data-bs-dismiss="modal">Close</button>
      `);

      $("#myModal").modal("show");

      // Xử lý khi xác nhận được click
      $("#myModal .modal-footer .btn-primary").on("click", function () {
        $.ajax({
          url: "order/confirm/" + orderId,
          method: "PUT", // Sử dụng phương thức PUT để xác nhận đơn hàng
          success: function (response) {
            alert("Đơn hàng đã được xác nhận thành công!");
            loadContent(url); // Tải lại trang sau khi xác nhận thành công
          },
          error: function (error) {
            console.error("Lỗi xác nhận đơn hàng:", error);
            alert("Có lỗi xảy ra khi xác nhận đơn hàng!");
          }
        });
      });
    },
    error: function (error) {
      console.error("Lỗi khi lấy chi tiết đơn hàng:", error);
    }
  });
});


document
  .getElementById("searchForm")
  .addEventListener("submit", async function (event) {
    event.preventDefault();
    const searchInput = document.getElementById("searchInput").value;
    try {
      var url = `/order/search?q=${searchInput}`; // Sử dụng template literals để tạo URL
      loadContent(url);
    } catch (error) {
      console.error("Lỗi khi tìm kiếm sản phẩm:", error.message);
    }
  });
