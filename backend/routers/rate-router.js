const express = require("express");
const { body } = require("express-validator");
const rateController = require("../controllers/rate-controllers");

const router = express.Router();

router.post(
  "/",
  [
    body("deck_id")
      .trim()
      .notEmpty()
      .withMessage("معرف المجموعة مطلوب")
      .isInt()
      .withMessage("يجب أن يكون معرف المجموعة رقم"),
    body("rate")
      .trim()
      .notEmpty()
      .withMessage("التقيم مطلوب مطلوب")
      .isInt({ min: 0, max: 5 })
      .withMessage("يجب أن يكون التقيم بين 0 و 5"),
    body("user_id").trim().notEmpty().withMessage("معرف المستخدم مطلوب"),
  ],
  rateController.postRate
);

module.exports = router;
