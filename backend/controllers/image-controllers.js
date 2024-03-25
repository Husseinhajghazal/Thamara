const NewError = require("../models/new-error");
const cloudinary = require("../util/cloudinary");

const uploadImage = async (req, res, next) => {
  try {
    const image = await cloudinary.uploader.upload(req.file.path);
    res
      .status(201)
      .json({ message: "uploading image", image_url: image.secure_url });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء رفع الصورة, الرجاء المحاولة لاحقا", 500)
    );
  }
};

exports.uploadImage = uploadImage;
