const express = require('express');
const router = express.Router();
const authController = require('../controllers/authController');

// Đường dẫn để gửi mã OTP đến email
router.post('/send-otp', async (req, res) => {
  const { email } = req.body;

  try {
    const otp = await authController.sendOTPByEmail(email);
    res.status(200).json({ message: 'OTP sent successfully', otp });
  } catch (error) {
    res.status(500).json({ message: 'Failed to send OTP' });
  }
});

// Đường dẫn để xác thực mã OTP
router.post('/verify-otp', async (req, res) => {
    const { email, otp } = req.body;
  
    try {
      const isVerified = authController.verifyOTP(email, otp);
      if (isVerified) {
        res.status(200).json({ message: 'OTP verified successfully' });
      } else {
        res.status(400).json({ message: 'Invalid OTP' });
      }
    } catch (error) {
      console.error('Error verifying OTP:', error);
      res.status(500).json({ message: 'Failed to verify OTP' });
    }
  });

module.exports = router;
