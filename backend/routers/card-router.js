const express = require("express");
const { body } = require("express-validator");
const cardController = require("../controllers/card-controllers");

const router = express.Router();

router.get("/", cardController.getAllCards);
router.post("/", cardController.createCard);
router.put("/:id", cardController.editCard);
router.delete("/:id", cardController.deleteCard);
router.get("/:id", cardController.getOneCard);

module.exports = router;
