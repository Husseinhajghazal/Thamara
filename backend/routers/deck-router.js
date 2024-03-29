const express = require("express");
const { body } = require("express-validator");
const deckController = require("../controllers/deck-controllers");

const router = express.Router();

router.post(
  "/",
  [
    body("name").trim().notEmpty().withMessage("اسم المجموعة مطلوب"),
    body("image_url").trim().notEmpty().withMessage("صورة المجموعة مطلوب"),
    body("color").trim().notEmpty().withMessage("لون المجموعة مطلوب"),
    body("col_id").notEmpty().withMessage("معرف الرابطة مطلوب"),
    body("allowShuffle")
      .notEmpty()
      .withMessage("أمكانية الخربطة مطلوبة")
      .isBoolean()
      .withMessage("يجب أن يكون السماح بالخربطة صح أو خطأ"),
    body("level")
      .notEmpty()
      .withMessage("مستوى المجموعة مطلوب")
      .isInt({ min: 0, max: 10 })
      .withMessage("يجب أن يكون مستوى الصعوبة بين 0 و 10"),
  ],
  deckController.createDeck
);
router.get("/", deckController.getAllDecks);
router.get("/version/:id", deckController.checkVersion);
router.get("/:id", deckController.getOneDeck);
router.delete("/:id", deckController.deleteDeckById);
router.put(
  "/:id",
  [
    body("name").trim().notEmpty().withMessage("اسم المجموعة مطلوب"),
    body("image_url").trim().notEmpty().withMessage("صورة المجموعة مطلوب"),
    body("color").trim().notEmpty().withMessage("لون المجموعة مطلوب"),
    body("col_id").notEmpty().withMessage("معرف الرابطة مطلوب"),
    body("allowShuffle")
      .notEmpty()
      .withMessage("أمكانية الخربطة مطلوبة")
      .isBoolean()
      .withMessage("يجب أن يكون السماح بالخربطة صح أو خطأ"),
    body("level")
      .notEmpty()
      .withMessage("مستوى المجموعة مطلوب")
      .isInt({ min: 0, max: 10 })
      .withMessage("يجب أن يكون مستوى الصعوبة بين 0 و 10"),
  ],
  deckController.editDeck
);

module.exports = router;
