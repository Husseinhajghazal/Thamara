const express = require("express");
const fileUpload = require("../multer");
const imageController = require("../controllers/image-controllers");

const router = express.Router();

router.post("/", fileUpload.single("image"), imageController.uploadImage);

module.exports = router;
