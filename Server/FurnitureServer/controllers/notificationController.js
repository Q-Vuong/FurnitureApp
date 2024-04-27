const Notification = require("../models/notification");
const multer = require("multer");
const admin = require("firebase-admin");

// Thiết lập Multer để xử lý tải tệp
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "uploads/notifications/"); // Thư mục lưu trữ tệp
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname); // Tên tệp sau khi lưu trữ
  },
});
const upload = multer({ storage: storage }).single("notificationImage"); // Đặt tên cho trường hình ảnh là 'bannerImage'

//create or update
exports.createNotification = async (req, res) => {
  try {
    // Gọi hàm upload trước khi kiểm tra req.body.id
    upload(req, res, async function(err) {
      if (err) {
        // Xử lý lỗi nếu có
        return res.status(400).send(err);
      }

      console.log("id: ------ ", req.body.id);

      if (req.body.id == "") {
        // Nếu không có ID được cung cấp, sẽ thêm
        await addRecord(req, res);
      } else {
        console.log("update ", req.body.id);
        await updateRecord(req, res);
      }
    });
  } catch (err) {
    console.error("Error creating/updating type of product:", err);
    return res.status(500).send("Error creating/updating type of product");
  }
};

async function addRecord(req, res) {
  try {
    if (!req.file) {
      // Kiểm tra xem có file được tải lên không
      return res.status(400).send("No file uploaded");
    }

    // Tạo một thể hiện sản phẩm mới với các trường đã được trích xuất từ req.body
    const newNotification = new Notification({
      image:  "/notifications/" + req.file.filename,
      title: req.body.title,
      date: req.body.date,
      content: req.body.content,
      notificationType: req.body.notificationType,
    });
    
    // Lưu sản phẩm mới vào cơ sở dữ liệu
    await newNotification.save();

    // Gửi thông báo tới thiết bị sử dụng Firebase Admin SDK
    const message = {
      notification: {
        title: req.body.title,
        body: req.body.content,
      },
      // Ghi đè data với thông tin chi tiết của thông báo
      data: {
        image: "/notifications/" + req.file.filename,
        notificationType: req.body.notificationType,
      },
      topic: 'news'
    };
    // Gửi thông báo sử dụng Firebase Admin SDK
    const response = await admin.messaging().send(message);
    console.log('Successfully sent message:', response);

    // Chuyển hướng người dùng đến trang thành công hoặc gửi phản hồi thành công
    return res.json({ redirectUrl: "/notification/add-page?success=true"});

  } catch (err) {
    // Xử lý lỗi nếu có trong quá trình thực hiện
    console.error("Error creating product:", err);
    return res.json({ redirectUrl: "/notification/add-page?success=false" });
  }
}

async function updateRecord(req, res) {
  try {
    const updatedNotification = await Notification.findByIdAndUpdate(req.body.id, req.body, { new: true });
    if (!updatedNotification) {
      return res.status(404).send("Type of product not found");
    }
    return res.json({ redirectUrl: "/notification/view?success=true" });
  } catch (err) {
    console.error("Error updating type of product:", err);
    const idValue = req.body.id;
    const redirectUrl = `/notification/update/${idValue}?success=errUpdate`;
    return res.json({ redirectUrl });

  }
};

// Update a notifiaction by ID
exports.updateNotificationById = async (req, res) => {
  try {
    const updatedNotification = await Notification.findById(req.params.id);
    if (!updatedNotification) {
      return res.status(404).send("Type of product not found");
    }
    console.log(updatedNotification);
    const dangerMessage = req.query.success === 'errUpdate' ? 'Đã xảy ra lỗi khi sửa thông, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null ;
    return res.render("notifications/addOrEdit_notifications.hbs", {viewTitle: "Sửa thông báo", notification: updatedNotification, dangerMessage });
  } catch (err) {
    console.error("Error updating type of product:", err);
    return res.status(500).send("Error updating type of product");
  }
};


// Get all types of products
exports.getAllNotifications = async (req, res) => {
  try {
    const notifications = await Notification.find();
    const successMessage = req.query.success === 'true' ? ' Thông được sửa thành công!' : null;
    res.render("notifications/view_notifications.hbs", { notifications: notifications, successMessage });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

exports.deleteNotificationById = async (req, res) => {
  try {
    const notification = await Notification.findByIdAndDelete(req.params.id);
    if (!notification) {
      return res.status(404).send();
    } else {
      res.redirect("/notification/view");
    }
    // res.send(banner);
  } catch (err) {
    res.status(500).send(err);
  }
};

exports.searchNotification = async (req, res) => {
  const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu

  try {
      // Tìm kiếm sản phẩm dựa trên tên sản phẩm hoặc mô tả
      const notifications = await Notification.find({
          $or: [
              { title: { $regex: query, $options: 'i' } }, 
              { content: { $regex: query, $options: 'i' } },
              { notificationType: { $regex: query, $options: 'i' } }
          ]
      })

      res.render("notifications/view_notifications.hbs", { notifications: notifications });
  } catch (err) {
      console.error(err);
      res.status(500).json({ message: 'Lỗi khi tìm kiếm sản phẩm.' });
  }
};

//________android_______

exports.getAllNotificationsForApp = async (req, res) => {
  try {
    const notifications = await Notification.find();
    res.json([...notifications]);
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};