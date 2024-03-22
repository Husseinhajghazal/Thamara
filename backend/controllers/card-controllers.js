const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");

const createCard = async (req, res, next) => {
  const deck_id = req.params.deck_id;

  let deck;
  try {
    deck = await Deck.findByPk(deck_id);
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على المجموعة, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  if (!deck) {
    return next(
      new NewError("لا يجود مجموعة بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  let cards;
  try {
    cards = Card.findAll({ where: { deck_id } });
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على كروت المجموعة, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  res.status(201).json({
    message: "تم الحصول على كروت المجموعة بنجاح",
    cards,
  });
};

const deleteCard = async (req, res, next) => {};

const editCard = async (req, res, next) => {};

const getOneCard = async (req, res, next) => {};

exports.createCard = createCard;
