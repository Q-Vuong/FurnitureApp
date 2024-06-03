const Customer = require("../models/customer");


exports.updateCart = async (req, res) => {
  try {
    const { productId, quantity } = req.body;
    const customerId = req.params.id; // Lấy id của khách hàng từ request params

    // Tìm xem sản phẩm có tồn tại trong giỏ hàng của khách hàng không
    const existingCartItem = await Customer.findOne({ 'cart.productId': productId });

    if (existingCartItem) {
      // Nếu sản phẩm đã tồn tại trong giỏ hàng của khách hàng, cộng thêm vào số lượng
      const updatedCartItem = await Customer.findOneAndUpdate(
        { _id: customerId, 'cart.productId': productId },
        { $inc: { 'cart.$.quantity': quantity } },
        { new: true } // Trả về bản ghi đã được cập nhật
      );
      console.log("Updated Cart Item:", updatedCartItem);
    } else {
      // Nếu sản phẩm chưa tồn tại trong giỏ hàng của khách hàng, thêm vào giỏ hàng mới
      const newCartItem = {
        productId: productId,
        quantity: quantity,
      };
      const updatedCart = await Customer.findByIdAndUpdate(
        customerId,
        { $push: { cart: newCartItem } },
        { new: true } // Trả về bản ghi đã được cập nhật
      );
      console.log("Updated Cart:", updatedCart);

      if (!updatedCart) {
        return res.status(404).json({ error: "Customer not found" });
      }
    }

    return res.status(200).json({ message: "Đã thêm vào giỏ hàng" });
  } catch (err) {
    console.error("Error updating cart:", err);
    return res.status(500).json({ error: "Internal server error" });
  }
};


exports.getCartForCustomer = async (req, res) => {
    try {
        const customerId = req.params.id; // Lấy id của khách hàng từ request params
        const customer = await Customer.findById(customerId) //.populate('cart.productId'); // Sử dụng populate để tham chiếu đến các sản phẩm trong giỏ hàng

        if (!customer) {
            return res.status(404).json({ message: 'Customer not found' });
        }

        const cart = customer.cart; // Lấy giỏ hàng của khách hàng từ đối tượng khách hàng
        console.log("allCart", cart);

        res.json([...cart]); // Trả về giỏ hàng của khách hàng
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
};



// exports.getFavoriteProductsForCustomer = async (req, res) => {
//   try {
//     const customerId = req.params.customerId; // Lấy id của khách hàng từ request params
//     const customer = await Customer.findById(customerId).populate('favoriteProducts'); // Lấy thông tin của khách hàng và kết hợp các sản phẩm yêu thích

//     if (!customer) {
//       return res.status(404).json({ message: 'Customer not found' });
//     }

//     res.json(customer.favoriteProducts); // Trả về danh sách các sản phẩm yêu thích của khách hàng
//   } catch (error) {
//     console.error(error);
//     res.status(500).json({ message: 'Internal Server Error' });
//   }
// };


exports.removeFromCart = async (req, res) => {
    try {
        const customerId = req.params.id; // Lấy id của khách hàng từ request params
        const productIdToRemove = req.params.productid; // Lấy id của sản phẩm cần xoá từ request params

        const updatedCustomer = await Customer.findOneAndUpdate(
            { _id: customerId },
            { $pull: { cart: { productId: productIdToRemove } } },
            { new: true }
        );

        if (!updatedCustomer) {
            return res.status(404).json({ message: 'Customer not found' });
        }

        console.log("Xoá thành công", productIdToRemove);
        console.log("Giỏ hàng hiện tại", updatedCustomer.cart);

        res.status(200).json({ message: 'Product removed from cart successfully' });
    } catch (error) {
        console.error(error);
        res.status(500).json({ message: 'Internal Server Error' });
    }
};

