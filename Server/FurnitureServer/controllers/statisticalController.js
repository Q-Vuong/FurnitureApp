const Product = require("../models/product");
const Order = require("../models/order");
const ProductType = require("../models/productType");

// Get all products
exports.getAllProducts = async (req, res) => {
  try {
    //Lấy tất cả sản phẩm
    const products = await Product.find();
    const orders = await Order.find();
    const productTypes = await ProductType.find();

    const productCount = await productTotal(products); // Gọi hàm productTotal để tính tổng số sản phẩm
    const totalQuantitySold = await orderBoughtTotal(orders);
    const productTypeCount = await productTypeTotal(productTypes);
    const monthlyRevenue = await calculateMonthlyRevenue(orders);
    const orderCount = await orderTotal(orders);
    const { confirmed, unconfirmed } = await countConfirmedOrders(orders);

    res.render("statistical/view_statistical.hbs", {
      totalQuantitySold: totalQuantitySold,
      productCount: productCount,
      productTypeCount: productTypeCount,
      monthlyRevenue: monthlyRevenue,
      orderCount: orderCount,
      confirmed,
      unconfirmed
    });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

//Hàm tính Tổng sản phẩm còn trong kho
async function productTotal(products) {
  try {
    const productCount = products.length;
    return productCount;
  } catch (error) {
    console.error("Error: ", error);
    throw error;
  }
}

//Hàm tính tổng số lượng đơn hàng đã bán
async function orderBoughtTotal(orders) {
  try {
    let totalQuantitySold = 0;

    orders.forEach((order) => {
      // Duyệt qua từng sản phẩm trong đơn hàng
      order.products.forEach((productItem) => {
        const quantitySold = productItem.quantity; // Số lượng sản phẩm đã bán
        totalQuantitySold += quantitySold; // Cộng số lượng vào tổng số lượng đã bán
      });
    });

    return totalQuantitySold;
  } catch (error) {
    console.error("Error: ", error);
    throw error;
  }
}

//Hàm tính tổng loại sản phẩm
async function productTypeTotal(productTypes) {
  try {
    const productTypeCount = productTypes.length;
    return productTypeCount;
  } catch (error) {
    console.error("Error: ", error);
    throw error;
  }
}

//hàm tính tổng đơn hàng
async function orderTotal(orders) {
  try {
    const orderCount = orders.length;
    return orderCount;
  } catch (error) {
    console.error("Error: ", error);
    throw error;
  }
}

//
async function calculateMonthlyRevenue(orders) {
  try {
    // Khởi tạo một đối tượng để lưu trữ doanh thu theo từng tháng
    const monthlyRevenue = {};

    // Duyệt qua từng đơn hàng để tính toán doanh thu
    orders.forEach((order) => {
      const orderDate = new Date(order.dateOrder); // Chuyển đổi ngày đặt hàng sang đối tượng Date
      const monthYear = `${orderDate.getFullYear()}-${
        orderDate.getMonth() + 1
      }`; // Chuỗi tháng-năm (VD: '2024-4')

      // Nếu chưa có doanh thu cho tháng này, khởi tạo là 0
      if (!monthlyRevenue[monthYear]) {
        monthlyRevenue[monthYear] = 0;
      }

      // Cộng tổng tiền của đơn hàng vào doanh thu của tháng tương ứng
      monthlyRevenue[monthYear] += parseFloat(order.priceToPay);
    });

    const monthlyRevenueJSON = JSON.stringify(monthlyRevenue);
    console.log("monthlyRevenue", monthlyRevenueJSON);
    return monthlyRevenueJSON;
  } catch (error) {
    console.error("Lỗi khi tính toán doanh thu hàng tháng:", error);
    throw error;
  }
}

//hàm lấy danh sách đơn hàng đã xác nhận
async function countConfirmedOrders(orders) {
  try {
    // Lọc ra các đơn hàng đã xác nhận và chưa được xác nhận
    const confirmedOrders = orders.filter(order => order.confirmed === true);
    const unconfirmedOrders = orders.filter(order => order.confirmed === false);

    // Đếm số lượng đơn hàng đã xác nhận và chưa được xác nhận
    const countConfirmedTrue = confirmedOrders.length;
    const countConfirmedFalse = unconfirmedOrders.length;

    // Trả về một đối tượng chứa số lượng đơn hàng đã xác nhận và chưa được xác nhận
    return {
      confirmed: countConfirmedTrue,
      unconfirmed: countConfirmedFalse
    };
  } catch (error) {
    console.error("Error counting confirmed orders: ", error);
    throw error;
  }
}

