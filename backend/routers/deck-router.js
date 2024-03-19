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
    body("level")
      .trim()
      .notEmpty()
      .withMessage("مستوى المجموعة مطلوب")
      .custom((value) => {
        if (!["سهل", "متوسط", "صعب"].includes(value)) {
          throw new Error(
            "المستوى يجب أن يكون أحد الكلمات التالية: سهل، متوسط، صعب"
          );
        }
        return true;
      }),
  ],
  deckController.createDeck
);
router.get("/", deckController.getAllDecks);
router.get("/:id", deckController.getOneDeck);
router.delete("/:id", deckController.deleteDeckById);
router.put("/:id", deckController.editDeck);

module.exports = router;
