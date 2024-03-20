const express = require("express");
const { body } = require("express-validator");
const tagController = require("../controllers/tag-controllers");

const router = express.Router();

router.get("/", tagController.getAllTags);
router.get("/:id", tagController.getOneTag);
router.post(
  "/",
  [body("name").trim().notEmpty().withMessage("أسم التاغ مطلوب")],
  tagController.createTag
);
router.put(
  "/:id",
  [body("name").trim().notEmpty().withMessage("أسم التاغ مطلوب")],
  tagController.editTag
);
router.delete("/:id", tagController.deleteTag);

module.exports = router;
