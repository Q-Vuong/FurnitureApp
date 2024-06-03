function loadContent(url) {
  $.get(url, function(data) {
    $("#content").html(data);
  });
}

function confirmAction(event) {
  // Kiểm tra xem sự kiện có phải là click trên một liên kết hay không
  if (event.target.tagName.toLowerCase() !== 'a') {
    return false;
  }

  // Hiển thị hộp thoại xác nhận và lưu kết quả vào biến
  var result = confirm("Thực hiện hành động?");
  if (!result) {
    // Ngăn chặn hành động mặc định của liên kết (chuyển hướng đến trang xóa banner)
    event.preventDefault();
  } else {
    event.preventDefault();
    var url = event.target.href; // Lấy đường dẫn từ thuộc tính href của liên kết
    
    // Gửi yêu cầu AJAX để xoá dữ liệu trên máy chủ
    $.ajax({
      url: url,
      type: "GET",
      success: function(response) {
        // Load trang /banner/view vào phần content
        $("#content").load("/banner/view");
      },
      error: function(xhr, status, error) {
        console.error("Đã xảy ra lỗi khi xoá dữ liệu:", error);
        alert("Đã xảy ra lỗi khi xoá dữ liệu. Vui lòng thử lại sau.");
      },
    });
  }
  return result;
}



// Lắng nghe sự kiện click  và thực hiện load nội dung tương ứng
$(document).ready(function() {
  $(".eLink").click(function(e) {
    e.preventDefault(); // Ngăn chặn hành vi mặc định của liên kết
    var url = $(this).attr("href"); // Lấy đường dẫn từ thuộc tính href của liên kết
    loadContent(url); // Load nội dung từ trang tương ứng
  });
});


$(document).ready(function() {
  // Lắng nghe sự kiện khi form được submit
  $("#bannerForm").submit(function(e) {
    e.preventDefault(); // Ngăn chặn việc submit form mặc định

    // Tạo một đối tượng FormData để lấy dữ liệu từ form
    var formData = new FormData($(this)[0]);

    // Gửi yêu cầu AJAX để tạo banner mới
    $.ajax({
      url: "/banner/add",
      type: "POST",
      data: formData,
      async: true,
      cache: false,
      contentType: false,
      processData: false,
      success: function(response) {
        // Load trang /banner vào phần content
        $("#content").load("/banner/add-page?success=true");
      },
      error: function(xhr, status, error) {
        console.error(xhr.responseText);
      },
    });
  });
});
