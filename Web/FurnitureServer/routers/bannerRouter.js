const express = require('express');
const router = express.Router();
const bannerController = require('../controllers/bannerController');

// Định nghĩa các route cho CRUD banner
router.post('/add', bannerController.createBanner);
router.get('/view', bannerController.getAllBannersView);
router.get('/delete/:id', bannerController.deleteBannerById);

router.get('/add-page', (req, res) => {
    const successMessage = req.query.success === 'true' ? 'Banner được thêm thành công!' : null;
    res.render('banners/add-banner.hbs', { successMessage });
});

///--------------android-------------
router.get('/view-app', bannerController.getAllBannersForApp);

module.exports = router;
