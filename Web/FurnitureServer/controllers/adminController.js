const Admin = require("../models/admin");
const multer = require("multer");
let id = "";

// Thiết lập Multer để xử lý tải tệp
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "uploads/admins/"); // Thư mục lưu trữ tệp
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname); // Tên tệp sau khi lưu trữ
  },
});
const upload = multer({ storage: storage }).single("avatarAdmin"); // Đặt tên cho trường hình ảnh là ''

exports.login = (req, res) => {
  const { username, password } = req.body;

  Admin.findOne({ username, password })
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
  const { password, newPassword, rePassword } = req.body;

  if (!newPassword || !rePassword || !password) {
    return res.redirect(`/admin/changepassView?danger=emty`);;
      
  }

  if (newPassword !== rePassword) {
    console.log("Mật khẩu mới và xác nhận mật khẩu mới không khớp");
    return res.redirect(`/admin/changepassView?danger=notequal`);;
      
  }

  Admin.findOne({ password })
    .then((admin) => {
      if (!admin) {
        console.log("Thông tin đổi mật khẩu không hợp lệ");
        return res.redirect(`/admin/changepassView?danger=err`);;
      }

      // Cập nhật mật khẩu mới
      admin.password = newPassword;

      // Lưu lại thông tin người dùng và xử lý phản hồi thành công
      admin.save()
        .then(() => {
          console.log("Đổi mật khẩu thành công");
          return res.redirect(`/?success=newpass`);;
        })
        .catch((saveErr) => {
          console.error("Lỗi khi lưu thông tin người dùng", saveErr);
          return res.status(500).send("Đổi mật khẩu thất bại");
        });
    })
    .catch((findErr) => {
      console.error("Đã xảy ra lỗi khi đổi mật khẩu", findErr);
      return res.status(500).send("Đổi mật khẩu thất bại");
    });
};


// exports.updateAdmin = (req, res) => {
//   Admin
//     .findByIdAndUpdate(req.body.id, req.body, { new: true })
//     .then((doc) => {
//       return res.status(200).send("Cập nhật thông tin admin thành công");
//     })
//     .catch((err) => {
//       console.log(err);
//       return res.status(500).send("Lỗi cập nhật thông tin admin");
//     });
// };

exports.getAdmin = async (req, res) => {
  try {
    const admin = await Admin.findById(id);
    console.log("admin", admin);
    res.render("admins/view_admin.hbs", { admin: admin });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

exports.updateAdmin = async (req, res) => {
  try {
    upload(req, res, async function (err) {
      if (err) {
        console.error("Error uploading file:", err);
        return res.status(500).send("Error uploading file");
      }

      let updatedFields = req.body; // Lấy thông tin cập nhật từ req.body
      if (req.file) {
        updatedFields.avatar = "/admins/" + req.file.filename; // Cập nhật đường dẫn ảnh mới
      }

      const admin = await Admin.findByIdAndUpdate(id, updatedFields, {
        new: true,
      });
      if (!admin) {
        return res.status(404).send("Admin not found");
      }

      return res.json({ redirectUrl: "/admin/view?success=true" });
    });
  } catch (err) {
    console.error("Error updating admin:", err);
    const idValue = req.body.id;
    const redirectUrl = `/admin/update/${idValue}?success=errUpdate`;
    return res.json({ redirectUrl });
  }
};

// Update a notifiaction by ID
exports.viewAdminById = async (req, res) => {
  try {
    const viewAdmin = await Admin.findById(id);
    if (!viewAdmin) {
      return res.status(404).send("Type of product not found");
    }
    const dangerMessage =
      req.query.success === "errUpdate"
        ? "Đã xảy ra lỗi khi sửa thông tin, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!"
        : null;
    return res.render("admins/edit_admin.hbs", {
      viewTitle: "Sửa thông tin",
      admin: viewAdmin,
      dangerMessage,
    });
  } catch (err) {
    console.error("Error updating type of product:", err);
    return res.status(500).send("Error updating type of product");
  }
};
