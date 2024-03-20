const express = require("express");
const { body } = require("express-validator");
const cardController = require("../controllers/card-controllers");

const router = express.Router();

router.get("/getDeckCards/:deck-id", cardController.getDeckCards);

module.exports = router;
