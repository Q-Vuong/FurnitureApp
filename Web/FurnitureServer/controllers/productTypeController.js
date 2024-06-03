const ProductType = require("../models/productType");
const Product = require("../models/product");
const multer = require("multer");
const { log } = require("handlebars");

// Thiết lập Multer để xử lý tải tệp
const storage = multer.diskStorage({
  destination: function (req, file, cb) {
    cb(null, "uploads/productTypes/"); // Thư mục lưu trữ tệp
  },
  filename: function (req, file, cb) {
    cb(null, file.originalname); // Tên tệp sau khi lưu trữ
  },
});
const upload = multer({ storage: storage }).single("productTypeImage"); // Đặt tên cho trường hình ảnh là 'productTypeImage'


//create or update
exports.createProductType = async (req, res) => {
  console.log(req.body);
  console.log(req.body.id);
  try {
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
      // Kiểm tra nếu không có tệp được tải lên
      return res.status(400).json({ error: 'No file uploaded' });
    }
    const newProductType = new ProductType({ 
      nameType: req.body.nameType, 
      imgPr_T: "/productTypes/" + req.file.filename
    }); 
    // Lưu loại sản phẩm vào cơ sở dữ liệu
    await newProductType.save(); 
    return res.json({ redirectUrl: "/productType/add-page?success=true" });
  } catch (err) {
    // Xử lý lỗi nếu có bất kỳ lỗi nào xảy ra trong quá trình tạo loại sản phẩm
    console.error("Error creating type of product:", err);
    return res.json({ redirectUrl: "/productType/add-page?success=errAdd" });
  }
}


async function updateRecord(req, res) {
  try {
    let updatedFields = req.body; // Lấy thông tin cập nhật từ req.body
    if (req.file) {
      // Nếu có tệp mới được tải lên, cập nhật đường dẫn ảnh mới
      updatedFields.imgPr_T = "/productTypes/" + req.file.filename;
    }
    const updatedProduct = await ProductType.findByIdAndUpdate(req.body.id, updatedFields, { new: true });
    if (!updatedProduct) {
      return res.status(404).send("Product type not found");
    }
    return res.json({ redirectUrl: "/productType/view?success=true" });
  } catch (err) {
    console.error("Error updating product:", err);
    const idValue = req.body.id;
    const redirectUrl = `/productType/update/${idValue}?success=errUpdate`;
    return res.json({ redirectUrl });
  }
}



// Update a type of product by ID
exports.updateProductTypeById = async (req, res) => {
  try {
    const updatedProductType = await ProductType.findById(req.params.id);
    if (!updatedProductType) {
      return res.status(404).send("Type of product not found");
    }
    console.log(updatedProductType);
    const dangerMessage = req.query.success === 'errUpdate' ? 'Đã xảy ra lỗi khi sửa danh mục nội thất, có thể dữ liệu này đã có trên hệ thống. Vui lòng kiểm tra lại!' : null ;
    return res.render("productTypes/addOrEdit_productType.hbs", {viewTitle: "Sửa loại nội thất", producttype: updatedProductType, dangerMessage });
  } catch (err) {
    console.error("Error updating type of product:", err);
    return res.status(500).send("Error updating type of product");
  }
};




// Create a new type of product
// exports.createProductType = async (req, res) => {
//   try {
//     const { nameType } = req.body;
//     const newProductType = new ProductType({ nameType }); 
//     // Lưu loại sản phẩm vào cơ sở dữ liệu
//     await newProductType.save();  
//     res.redirect("/productType/add-page?success=true"); 
//   } catch (err) {
//     // Xử lý lỗi nếu có bất kỳ lỗi nào xảy ra trong quá trình tạo loại sản phẩm
//     console.error("Error creating type of product:", err);
//     return res.status(500).send("Error creating type of product");
//   }
// };

// Get all types of products
exports.getAllProductTypes = async (req, res) => {
  try {
    const productTypes = await ProductType.find();
    const productCounts = await getProductCounts();
  
    // Gán số lượng sản phẩm vào từng loại
    const productTypeData = productTypes.map(type => {
      const countObj = productCounts.find(count => String(count._id) === String(type._id));
      return {
        ...type.toObject(),
        count: countObj ? countObj.count : 0
      };
    });
  
    const successMessage = req.query.success === 'true' ? 'Danh mục nội thất được sửa thành công!' : null;
    res.render("productTypes/view_productType.hbs", { productTypes: productTypeData, successMessage });
  } catch (error) {
    console.error(error);
    res.status(500).send("Internal Server Error");
  }
};

// // Get a type of product by ID
// exports.getProductTypeById = (req, res) => {
//   const productTypeId = req.params.id;
//   ProductType.findById(productTypeId, (err, productType) => {
//     if (err) {
//       console.error("Error getting type of product:", err);
//       return res.status(500).send("Error getting type of product");
//     }
//     if (!productType) {
//       return res.status(404).send("Type of product not found");
//     }
//     return res.status(200).json(productType);
//   });
// };


// Delete a type of product by ID
exports.deleteProductTypeById = async (req, res) => {
  try {
    const productTypes = await ProductType.findByIdAndDelete(req.params.id);
    if (!productTypes) {
      return res.status(404).send();
    } else {
      res.redirect("/productType/view");
    }
    // res.send(banner);
  } catch (err) {
    res.status(500).send(err);
  }
};

//Hàm để lấy số lượng sản phẩm cho mỗi loại
async function getProductCounts() {
  try {
    // Truy vấn CSDL để đếm số lượng sản phẩm cho mỗi loại
    const productCounts = await Product.aggregate([
      { $group: { _id: '$type', count: { $sum: 1 } } }
    ]);
    // Trả dữ liệu về
    return productCounts;
  } catch (error) {
    console.error('Lỗi khi lấy số lượng sản phẩm của mỗi loại:', error);
    res.status(500).send('Đã xảy ra lỗi trong quá trình xử lý yêu cầu.');
  }
}

exports.searchProductType = async (req, res) => {
  const query = req.query.q; // Lấy tham số truy vấn từ yêu cầu

  try {
      // Tìm kiếm sản phẩm dựa trên tên sản phẩm hoặc mô tả
      const productTypes = await ProductType.find({
              nameType: { $regex: query, $options: 'i' } // Tìm kiếm theo tên sản phẩm không phân biệt chữ hoa chữ thường
      })
      console.log(productTypes);
      res.render("productTypes/view_productType.hbs", { productTypes: productTypes });
  } catch (err) {
      console.error(err);
      res.status(500).json({ message: 'Lỗi khi tìm kiếm sản phẩm.' });
  }
};


//------------------------Android-------------------------------//
// Controller để lấy tất cả product dưới dạng JSON
exports.getAllProductTypeForApp = async (req, res) => {
  try {
    const productTypes = await ProductType.find();
    res.json([...productTypes]);
  } catch (error) {
    console.error(error);
    res.status(500).json({ message: 'Internal Server Error' });
  }
};
