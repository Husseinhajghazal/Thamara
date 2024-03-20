const NewError = require("../model/new-error");
const { validationResult } = require("express-validator");
const Deck = require("../model/deck");

const createDeck = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new NewError(errors.array()[0].msg, 422));
    }

    const { name, image_url, color, level } = req.body;

    const createdDeck = await Deck.create({
      name,
      image_url,
      color,
      level,
      ratesCount: 0,
      rate: 0,
      version: 0,
    });

    res.status(201).json({
      message: "تم أنشاء المجموعة بنجاح",
      createdDeck,
    });
  } catch (error) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }
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

const deleteDeckById = async (req, res, next) => {
  const id = req.params.id;

  let deletedDeck;
  try {
    deletedDeck = await Deck.findByPk(id);
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!deletedDeck) {
    return next(
      new NewError("لا يجود مجموعة بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  try {
    await Deck.destroy({
      where: {
        id: id,
      },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.json({
    message: "تم حذف المجموعة بنجاح",
    deletedDeck,
  });
};

const editDeck = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name, image_url, color, level } = req.body;

  const id = req.params.id;

  let editedDeck;
  try {
    editedDeck = await Deck.findByPk(id);
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  await editedDeck.update({
    name,
    image_url,
    color,
    level,
    version: editedDeck.version + 1,
    ...editedDeck,
  });

  res.json({
    message: "تم تعديل المجموعة بنجاح",
    editedDeck,
  });
};

const getOneDeck = async (req, res, next) => {
  const id = req.params.id;

  let deck;
  try {
    deck = await Deck.findByPk(id);
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

  res.json({
    message: "تم الحصول على المجموعة بنجاح",
    deck,
  });
};

const checkVersion = async (req, res, next) => {
  const id = req.params.id;

  let deck;
  try {
    deck = await Deck.findByPk(id);
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

  res.json({
    message: "تم الحصول على المجموعة بنجاح",
    deckVersion: deck.version,
  });
};

exports.createDeck = createDeck;
exports.getAllDecks = getAllDecks;
exports.deleteDeckById = deleteDeckById;
exports.editDeck = editDeck;
exports.getOneDeck = getOneDeck;
exports.checkVersion = checkVersion;
