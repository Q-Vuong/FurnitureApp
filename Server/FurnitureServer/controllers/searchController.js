const Product = require('../models/product');
const Discount = require('../models/discount');



//_________Android______________________
// Endpoint cho tìm kiếm và hiển thị thông tin sản phẩm
exports.searchProduct = async (req, res) => {
    const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu

    try {
        // Tìm kiếm sản phẩm dựa trên tên sản phẩm hoặc mô tả
        const products = await Product.find({
            $or: [
                { namePr: { $regex: query, $options: 'i' } }, // Tìm kiếm theo tên sản phẩm không phân biệt chữ hoa chữ thường
                { description: { $regex: query, $options: 'i' } } // Tìm kiếm theo mô tả không phân biệt chữ hoa chữ thường
            ]
        })

        res.json(products); // Trả về thông tin chi tiết của các sản phẩm
    } catch (err) {
        console.error(err);
        res.status(500).json({ message: 'Lỗi khi tìm kiếm sản phẩm.' });
    }
};
