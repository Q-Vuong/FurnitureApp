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
          $("#content").load("/customer/view");
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

  
  document.getElementById('searchForm').addEventListener('submit', async function(event) {
    event.preventDefault(); 
    const searchInput = document.getElementById('searchInput').value;
    try {
      var url = `/customer/search?q=${searchInput}`; // Sử dụng template literals để tạo URL
      loadContent(url);
  
    } catch (error) {
      console.error('Lỗi khi tìm kiếm sản phẩm:', error.message);
    }
  });