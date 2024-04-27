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
      // Ngăn chặn hành động mặc định của liên kết (chuyển hướng đến trang xóa product Type)
      event.preventDefault();
    } else {
      event.preventDefault();
      var url = event.target.href; // Lấy đường dẫn từ thuộc tính href của liên kết
      
      // Gửi yêu cầu AJAX để xoá dữ liệu trên máy chủ
      $.ajax({
        url: url,
        type: "GET",
        success: function(response) {
          // Load trang /productType/view vào phần content
          $("#content").load("/notification/view");
        },
        error: function(xhr, status, error) {
          console.error("Đã xảy ra lỗi khi xoá dữ liệu:", error);
          alert("Đã xảy ra lỗi khi xoá dữ liệu. Vui lòng thử lại sau.");
        },
      });
    }
    return result;
  }
  

  // Lắng nghe sự kiện click trên navbar và thực hiện load nội dung tương ứng
  $(document).ready(function() {
    $(".eLink").click(function(e) {
      e.preventDefault(); // Ngăn chặn hành vi mặc định của liên kết
      var url = $(this).attr("href"); // Lấy đường dẫn từ thuộc tính href của liên kết
      loadContent(url); // Load nội dung từ trang tương ứng
    });
  });
  
  $(document).ready(function() {
    // Lắng nghe sự kiện khi form được submit
    $("#notificationForm").submit(function(e) {
        e.preventDefault(); // Ngăn chặn việc submit form mặc định

        // Tạo một đối tượng FormData để lấy tất cả dữ liệu từ form
        var formData = new FormData(this);

        // Gửi yêu cầu AJAX để thêm hoặc sửa sản phẩm
        $.ajax({
            url: "/notification/add", // Lấy url từ thuộc tính action của form
            type: "POST",
            data: formData,
            contentType: false,
            processData: false,
            success: function(response) {
                // Load trang /product/view vào phần content sau khi thêm/sửa sản phẩm thành công
                $("#content").load(response.redirectUrl);
            },
            error: function(xhr, status, error) {
                console.error(xhr.responseText);
            }
        });
    });
});

document.getElementById('searchForm').addEventListener('submit', async function(event) {
  event.preventDefault(); 
  const searchInput = document.getElementById('searchInput').value;
  try {
    var url = `/notification/search?q=${searchInput}`; // Sử dụng template literals để tạo URL
    loadContent(url);

  } catch (error) {
    console.error('Lỗi khi tìm kiếm sản phẩm:', error.message);
  }
});
