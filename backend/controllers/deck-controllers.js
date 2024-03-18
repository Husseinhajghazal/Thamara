const NewError = require("../model/new-error");
const { validationResult } = require("express-validator");
const Deck = require("../model/deck");

const createDeck = async (req, res, next) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name, image_url, color, level } = req.body;

  let createdDeck;
  try {
    createdDeck = Deck.create({
      name: name,
      image_url: image_url,
      color: color,
      level: level,
      rate: 0,
      version: 0,
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.status(201).json({
    message: "تم أنشاء المجموعة بنجاح",
    createdDeck,
  });
};

const getAllDecks = async (req, res, next) => {
  let decks;
  try {
    decks = await Deck.findAll();
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على المجموعات, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  res.status(201).json({
    message: "تم الحصول على المجموعات",
    decks,
  });
};

exports.createDeck = createDeck;
exports.getAllDecks = getAllDecks;
