const express = require("express");
const { body } = require("express-validator");
const tagController = require("../controllers/tag-controllers");

const router = express.Router();

router.get("/", tagController.getAllTags);
router.get("/connection", tagController.getAllLinks);
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
router.delete("/connection", tagController.disconnectTag);
router.delete("/:id", tagController.deleteTag);
router.post(
  "/connection",
  [
    body("tag_id").trim().notEmpty().withMessage("معرف التاغ مطلوب"),
    body("deck_id").trim().notEmpty().withMessage("معرف التاغ مطلوب"),
  ],
  tagController.connectTag
);

module.exports = router;
