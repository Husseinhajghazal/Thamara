const express = require("express");
const { body } = require("express-validator");
const collectionController = require("../controllers/collection-controllers");

const router = express.Router();

router.post(
  "/",
  [body("name").trim().notEmpty().withMessage("اسم المجموعة مطلوب")],
  collectionController.createCollection
);
router.get("/", collectionController.getAllCollections);
router.get("/:id", collectionController.getOneCollection);
router.put(
  "/:id",
  [body("name").trim().notEmpty().withMessage("اسم المجموعة مطلوب")],
  collectionController.editCollection
);
router.delete("/:id", collectionController.deleteCollection);
module.exports = router;
