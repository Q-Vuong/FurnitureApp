const express = require("express");
const router = express.Router();
const notificationController = require("../controllers/notificationController");

// Get all 
router.get("/view", notificationController.getAllNotifications);
// Create a new
router.post("/add", notificationController.createNotification);

router.get('/add-page', (req, res) => {
    const successMessage = req.query.success === 'true' ? 'Thông báo được thêm thành công!' : null;
    const dangerMessage = req.query.success === 'errAdd' ? 'Đã xảy ra lỗi khi thêm thông báo, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null; 
    res.render('notifications/addOrEdit_notifications.hbs', {viewTitle: "Thêm thông báo", successMessage, dangerMessage });
});

router.get("/delete/:id", notificationController.deleteNotificationById);

router.get("/update/:id", notificationController.updateNotificationById);

router.get("/search", notificationController.searchNotification );

//__________________android___________________
router.get('/view-app', notificationController.getAllNotificationsForApp);



module.exports = router;
