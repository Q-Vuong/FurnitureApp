const mongoose = require("mongoose");

const notificationSchema = new mongoose.Schema({
  image: String,
  title: String,
  date: { type: Date, default: Date.now },
  content: String,
  notificationType: { type: String, enum: ["Khuyến mãi", "Sản phẩm", "Cửa hàng", "Khác"] }
});

const Notification = mongoose.model("Notification", notificationSchema);

module.exports = Notification;
