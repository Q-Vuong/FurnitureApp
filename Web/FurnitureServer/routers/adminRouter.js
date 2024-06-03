const express = require("express");
const router = express.Router();
const adminController = require("../controllers/adminController");

router.post("/login", adminController.login);

router.post("/changePassword", adminController.changePassword);

router.post("/updateAdmin", adminController.updateAdmin);

router.get("/view", adminController.getAdmin);

router.get("/viewbyid", adminController.viewAdminById);

router.get("/changepassView", (req, res) => {
  const dangerMessage =
    req.query.danger === "emty"
      ? "Không được bỏ trống bất kì ô nhập nào!"
      : req.query.danger === "err"
      ? "Mật khẩu cũ sai!"
      : req.query.danger === "notequal"
      ? "Mật khẩu mới và xác nhận mật khẩu mới không khớp!" : null;
  const successMessage =
    req.query.success === "newpass"
      ? "Mật khẩu đổi thành công. Vui lòng đăng nhập lại với mật khẩu mới!"
      : null;
  res.render("admins/changepass.hbs", { successMessage, dangerMessage });
});

module.exports = router;
