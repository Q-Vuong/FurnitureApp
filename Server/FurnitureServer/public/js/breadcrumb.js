// Lấy danh sách các mục navbar
const navItems = document.querySelectorAll(".nav-item");

document.getElementById("breadcrumb").innerHTML = "Thống kê";
// Duyệt qua mỗi mục và thêm sự kiện click
navItems.forEach((navItem) => {
  navItem.addEventListener("click", () => {
    // Lấy nội dung của mục navbar được chọn
    const selectedNavItemText = navItem.textContent.trim();

    // Cập nhật nội dung của phần breadcrumb
    document.getElementById("breadcrumb").innerHTML = selectedNavItemText;
  });
});
