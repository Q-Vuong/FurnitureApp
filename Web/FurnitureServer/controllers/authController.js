const nodemailer = require('nodemailer');
const crypto = require('crypto');

// Tạo một transporter cho Nodemailer
const transporter = nodemailer.createTransport({
  service: 'gmail',
  auth: {
    user: 'vuongdqpd06003@fpt.edu.vn', // Địa chỉ email của bạn
    pass: 'nvvr rhbf yqsz ryqi' // Mật khẩu email của bạn
  }
});

// Lưu trữ mã OTP đã gửi cho từng email (hoặc người dùng)
const otpCache = {};

// Hàm gửi mã OTP qua email
function sendOTPByEmail(email) {
  const otp = generateOTP(); // Sinh mã OTP ngẫu nhiên

  const mailOptions = {
    from: 'vuongdqpd06003@fpt.edu.vn',
    to: email,
    subject: 'Verification Code for Registration',
    text: `Your verification code is: ${otp}`
  };

  return new Promise((resolve, reject) => {
    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.error('Error sending email:', error);
        reject(error);
      } else {
        console.log('Email sent:', info.response);
        otpCache[email] = otp; // Lưu mã OTP vào cache theo email
        resolve(); // Gửi thành công
      }
    });
  });
}

// Hàm sinh mã OTP ngẫu nhiên
function generateOTP() {
  return crypto.randomInt(100000, 999999).toString();
}

// Hàm xác thực mã OTP
function verifyOTP(email, otp) {
  if (!otpCache[email]) {
    return false; // Không tồn tại mã OTP cho email này
  }

  const cachedOTP = otpCache[email];
  if (cachedOTP === otp) {
    // Xóa mã OTP khỏi cache sau khi xác thực thành công
    delete otpCache[email];
    return true; // Mã OTP hợp lệ
  } else {
    return false; // Mã OTP không hợp lệ
  }
}

// Export các hàm để sử dụng ở nơi khác
module.exports = { sendOTPByEmail, verifyOTP };
