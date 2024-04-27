// Biến trạng thái của thanh điều hướng (nav) để kiểm soát việc mở rộng hoặc thu nhỏ
let navState = 0;

// Lấy tham chiếu đến phần tử toggleNavbar
var image = document.getElementById("toggleNavbar");

// Hàm thu nhỏ thanh điều hướng
function collapseNav() {
  // Kiểm tra nếu thanh điều hướng không có class 'small-nav' và navState đang ở trạng thái 1
  if (
    !document.getElementById("navbarColumn").classList.contains("small-nav") &&
    navState === 1
  ) {
    // Thêm class 'small-nav' vào thanh điều hướng
    document.getElementById("navbarColumn").classList.add("small-nav");

    // Thêm class 'max-with' vào cột nội dung bên phải
    document.getElementById("contentColumn").classList.add("max-with");

    // Lặp qua tất cả các phần tử có class 'myLink' và thêm class 'hidden-link'
    document
      .querySelectorAll(".myLink")
      .forEach((link) => link.classList.add("hidden-link"));
  }
}

// Hàm mở rộng thanh điều hướng
function expandNav() {
  // Kiểm tra nếu thanh điều hướng có class 'small-nav' và navState đang ở trạng thái 1
  if (
    document.getElementById("navbarColumn").classList.contains("small-nav") &&
    navState === 1
  ) {
    // Xóa class 'small-nav' khỏi thanh điều hướng
    document.getElementById("navbarColumn").classList.remove("small-nav");

    // Sử dụng setTimeout để đảm bảo các thay đổi được áp dụng sau một khoảng thời gian nhất định
    setTimeout(function () {
      // Lặp qua tất cả các phần tử có class 'myLink' và xóa class 'hidden-link'
      document
        .querySelectorAll(".myLink")
        .forEach((link) => link.classList.remove("hidden-link"));
    }, 350); // Thời gian trễ trước khi loại bỏ class 'hidden-link'
  }
}

document.addEventListener("DOMContentLoaded", function () {
  var url = "/statistical/view"; // Lấy đường dẫn từ thuộc tính href của liên kết
  loadContent(url); // Load nội dung từ trang tương ứng
  
  // Lắng nghe sự kiện mouseenter trên thanh điều hướng để mở rộng
  document
    .getElementById("navbarColumn")
    .addEventListener("mouseenter", expandNav);

  // Lắng nghe sự kiện mouseleave trên thanh điều hướng để thu nhỏ
  document
    .getElementById("navbarColumn")
    .addEventListener("mouseleave", collapseNav);
});
// Lắng nghe sự kiện click trên toggleNavbar để thay đổi trạng thái của thanh điều hướng
document.getElementById("toggleNavbar").addEventListener("click", function () {
  // Kiểm tra trạng thái hiện tại của thanh điều hướng và cập nhật nó
  if (navState === 1) {
    // Nếu đang ở trạng thái mở rộng, chuyển về trạng thái thu nhỏ
    image.src = "/icons/left-arrow.png";
    document.getElementById("navbarColumn").classList.remove("small-nav");
    document.getElementById("contentColumn").classList.remove("max-with");
    setTimeout(function () {
      document
        .querySelectorAll(".myLink")
        .forEach((link) => link.classList.remove("hidden-link"));
    }, 350);
    navState = 0;
  } else {
    // Ngược lại, chuyển về trạng thái mở rộng
    image.src = "/icons/right-arrow.png";
    document.getElementById("navbarColumn").classList.add("small-nav");
    document.getElementById("contentColumn").classList.add("max-with");
    document
      .querySelectorAll(".myLink")
      .forEach((link) => link.classList.add("hidden-link"));
    navState = 1;
  }
});

// Hàm để load nội dung từ các trang khi người dùng click vào navbar
function loadContent(url) {
  $.get(url, function (data) {
    $("#content").html(data);
  });
}

// Lắng nghe sự kiện click trên navbar và thực hiện load nội dung tương ứng
$(document).ready(function () {
  $(".myLink").click(function (e) {
    e.preventDefault(); // Ngăn chặn hành vi mặc định của liên kết
    var url = $(this).attr("href"); // Lấy đường dẫn từ thuộc tính href của liên kết
    loadContent(url); // Load nội dung từ trang tương ứng
  });
});
