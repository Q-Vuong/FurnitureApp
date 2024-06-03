const multer = require('multer');
const Banner = require('../models/banner');

// Thiết lập Multer để xử lý tải tệp
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "uploads/banners/"); // Thư mục lưu trữ tệp
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname); // Tên tệp sau khi lưu trữ
  },
});
const upload = multer({ storage: storage }).single('bannerImage'); // Đặt tên cho trường hình ảnh là 'image'

// Controller để tạo banner mới
exports.createBanner = async (req, res) => {
  try {
    // Sử dụng middleware upload để lấy thông tin về file được tải lên
    upload(req, res, async function(err) {
      if (err) {
        // Xử lý lỗi nếu có
        return res.status(400).send(err);
      }
      const banner = new Banner({
        bannerLink: "/banners/" + req.file.filename, // Lưu tên tệp của ảnh được tải lên
      });
      await banner.save();
      res.redirect("/banner/add-page?success=true");
    });
  } catch (err) {
    res.status(400).send(err);
  }
};



// Controller để lấy tất cả banner
exports.getAllBannersView = async (req, res) => {
  try {
    const banners = await Banner.find();
    res.render("banners/view-banner.hbs", { banners: banners });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};


// Controller để xóa banner theo ID
exports.deleteBannerById = async (req, res) => {
  try {
    const banner = await Banner.findByIdAndDelete(req.params.id);
    if (!banner) {
      return res.status(404).send();
    } else {
      res.redirect("/banner/view");
    }
    // res.send(banner);
  } catch (err) {
    res.status(500).send(err);
  }
};

//------------------------Android-------------------------------//
// Controller để lấy tất cả banner dưới dạng JSON
exports.getAllBannersForApp = async (req, res) => {
  try {
    const banners = await Banner.find();
    res.json([...banners]);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};
