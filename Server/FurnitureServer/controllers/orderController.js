const Order = require("../models/order");
const Customer = require("../models/customer");
const Product = require("../models/product");
const mongoose = require('mongoose');

// Get all products
exports.getAllOrders = async (req, res) => {
  try {
    // Sử dụng populate để lấy thông tin của ProductType cho mỗi sản phẩm
    const orders = await Order.find().populate("userId");
    const successMessage =
      req.query.success === "true" ? "Nội thất được sửa thành công!" : null;

    res.render("orders/view_order.hbs", {
      orders: orders,
      successMessage,
    });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

// Get all products
exports.getDetailOrderById = async (req, res) => {
  try {
    const detailOrder = await Order.findById(req.params.id)
      .populate("userId")
      .populate("products.productId");
    console.log("___detail order", detailOrder);
    res.json({ detailOrder: detailOrder });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};


exports.searchOrder = async (req, res) => {
  const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu
  console.log(query);

  try {
    // Tìm kiếm Product và Customer dựa trên query
    const productSearch = await Product.find({ namePr: { $regex: query, $options: "i" } });
    const customerSearch = await Customer.find({ fullName: { $regex: query, $options: "i" } });

    // Lấy ra các id tương ứng từ kết quả tìm kiếm
    const productIds = productSearch.map(product => product._id);
    const customerIds = customerSearch.map(customer => customer._id);

    // Tạo một mảng các điều kiện tìm kiếm cho đơn hàng
    const orderConditions = [
      { note: { $regex: query, $options: "i" } },
      { userId: { $in: customerIds } },
      { "products.productId": { $in: productIds } }
    ];

    // Kiểm tra xem `query` có phải là một ObjectId hợp lệ không
    const isValidObjectId = mongoose.isValidObjectId(query);
    if (isValidObjectId) {
      orderConditions.push({ _id: query }); // Thêm điều kiện tìm kiếm theo _id nếu `query` là một ObjectId hợp lệ
    }

    // Tìm các đơn hàng liên quan dựa trên orderConditions
    const orders = await Order.find({ $or: orderConditions })
      .populate("userId")
      .populate("products.productId");

    console.log(orders);

    res.render("orders/view_order.hbs", { orders: orders });
  } catch (err) {
    console.error(err);
    res.status(500).json({ message: "Lỗi khi tìm kiếm đơn hàng." });
  }
};

exports.confirmOrder = async (req, res) => {
  const orderId = req.params.id;

  try {
    const order = await Order.findById(orderId);
    if (!order) {
      return res.status(404).json({ message: "Đơn hàng không tồn tại" });
    }

    order.confirmed = true; // Đánh dấu đơn hàng là đã được xác nhận
    await order.save();
    res.render("orders/view_order.hbs");
    
  } catch (error) {
    console.error("Error confirming order:", error);
    res.status(500).json({ error: "Lỗi xác nhận đơn hàng" });
  }
};


//_____________Android__________________
// Controller để tạo một đơn hàng mới
exports.createOrder = async (req, res) => {
  try {
    // Lấy dữ liệu từ request body của client
    const { dateOrder, note, userId, priceToPay, products } = req.body;

    // Tạo một đối tượng Order mới
    const newOrder = new Order({
      dateOrder,
      note,
      products,
      userId,
      priceToPay,
      confirmed: false,
    });

    // Lưu đối tượng Order vào cơ sở dữ liệu
    await newOrder.save();
    console.log("---------------", newOrder);

    // Trả về phản hồi thành công với đơn hàng đã tạo
    res.status(201).json({ message: "Cảm ơn bạn vì đã đặt hàng" });
  } catch (error) {
    // Xử lý lỗi nếu có bất kỳ lỗi nào xảy ra
    console.error("Error creating order:", error);
    res.status(500).json({ error: "Internal server error" });
  }
};

// Hàm để lấy danh sách các đơn đặt hàng
exports.getAllOrdersForApp = async (req, res) => {
  try {
    const orders = await Order.find();
    res.json([...orders]);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};
