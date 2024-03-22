const express = require("express");
const { body } = require("express-validator");
const cardController = require("../controllers/card-controllers");

const router = express.Router();

router.post("/", cardController.createCard);

module.exports = router;
