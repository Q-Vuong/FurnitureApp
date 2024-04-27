const Admin = require("../models/admin");
let id = "";

exports.login = (req, res) => {
  const { username, password } = req.body;

  Admin
    .findOne({ username, password })
    .then((admin) => {
      if (!admin) {
        console.log("Thông tin đăng nhập không hợp lệ");
        return res.redirect(`/?danger=true`);
      } else {
        console.log(`Đăng nhập thành công với thông tin: ${admin}`);
        // Lấy thông tin fullname của admin từ cơ sở dữ liệu
        id = admin.id;
        const fullname = admin.fullName;
        console.log(`fullname: ${fullname}`);
        return res.redirect(`/main?fullname=${fullname}`);
      }
    })
    .catch((err) => {
      console.error("Đã xảy ra lỗi khi đăng nhập", err);
      return res.status(500).send("Đăng nhập thất bại");
    });
};

exports.changePassword = (req, res) => {
  const { username, currentPassword, newPassword } = req.body;

  Admin
    .findOne({ username, password: currentPassword })
    .then((admin) => {
      if (!admin) {
        console.log("Thông tin đổi mật khẩu không hợp lệ");
        return res.status(400).send("Thông tin đổi mật khẩu không hợp lệ");
      }

      // Cập nhật mật khẩu mới
      admin.password = newPassword;
      // Lưu lại thông tin người dùng
      return admin.save();
    })
    .then(() => {
      console.log("Đổi mật khẩu thành công");
      return res.status(200).send("Đổi mật khẩu thành công");
    })
    .catch((err) => {
      console.error("Đã xảy ra lỗi khi đổi mật khẩu", err);
      return res.status(500).send("Đổi mật khẩu thất bại");
    });
};

exports.updateAdmin = (req, res) => {
  Admin
    .findByIdAndUpdate(req.body.id, req.body, { new: true })
    .then((doc) => {
      return res.status(200).send("Cập nhật thông tin admin thành công");
    })
    .catch((err) => {
      console.log(err);
      return res.status(500).send("Lỗi cập nhật thông tin admin");
    });
};

exports.getAdmin = async (req, res) => {
  try {
    const admin = await Admin.findById(id);
    console.log("admin", admin)
    res.render("admins/view_admin.hbs", { admin: admin });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

