const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const createDeck = async (req, res, next) => {
  try {
    const errors = validationResult(req);
    if (!errors.isEmpty()) {
      return next(new NewError(errors.array()[0].msg, 422));
    }

    const { name, image_url, color, level } = req.body;

    const createdDeck = await prisma.deck.create({
      data: {
        name,
        image_url,
        color,
        level,
        version: 0,
      },
    });

    res.status(201).json({
      message: "تم أنشاء المجموعة بنجاح",
      deck: { ...createdDeck, ratesCounts: 0, rate: 0 },
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
    decks = await prisma.deck.findMany({
      include: {
        rates: true,
        cards: true,
        tags: true,
      },
    });
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على المجموعات, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  decks = decks.map((deck) => {
    const ratesCounts = deck.rates.length;

    return {
      id: deck.id,
      name: deck.name,
      image_url: deck.image_url,
      color: deck.color,
      level: deck.level,
      version: deck.version,
      tags: deck.tags,
      cards: deck.cards,
      ratesCounts,
      rate:
        deck.rates.reduce((total, num) => total + parseInt(num.value), 0) /
        ratesCounts,
    };
  });

  res.status(201).json({
    message: "تم الحصول على المجموعات",
    decks,
  });
};

const deleteDeckById = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let deletedDeck;
  try {
    deletedDeck = await prisma.deck.findUnique({ where: { id } });
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
    await prisma.deck.delete({
      where: {
        id,
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

  const id = parseInt(req.params.id);

  let editedDeck;
  try {
    editedDeck = await prisma.deck.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  editedDeck = await prisma.deck.update({
    where: { id },
    data: {
      ...editedDeck,
      name,
      image_url,
      color,
      level,
      version: parseInt(editedDeck.version) + 1,
    },
  });

  res.json({
    message: "تم تعديل المجموعة بنجاح",
    editedDeck,
  });
};

const getOneDeck = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let deck;
  try {
    deck = await prisma.deck.findUnique({
      where: { id },
      include: {
        rates: true,
        cards: true,
        tags: true,
      },
    });
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
  const id = parseInt(req.params.id);

  let deck;
  try {
    deck = await prisma.deck.findUnique({
      where: { id },
      select: { version: true },
    });
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
