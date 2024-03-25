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

    const { name, image_url, color, level, col_id, allowShuffle } = req.body;

    let collection;
    try {
      collection = await prisma.collection.findUnique({
        where: { id: parseInt(col_id) },
      });
    } catch (e) {
      return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
    }

    if (!collection) {
      return next(new NewError("لا يوجد رابط بهذا المعرف", 400));
    }

    const createdDeck = await prisma.deck.create({
      data: {
        name,
        image_url,
        color,
        level,
        col_id,
        allowShuffle,
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

// queries =  page & tag & searchedTerm;

const getAllDecks = async (req, res, next) => {
  let decks;
  try {
    decks = await prisma.deck.findMany({
      include: {
        rates: true,
        cards: true,
        tags: true,
        collection: true,
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
      allowShuffle: deck.allowShuffle,
      tags: deck.tags,
      collection: deck.collection,
      cardsCount: deck.cards.length,
      ratesCounts,
      rate:
        deck.rates.reduce((total, num) => total + parseInt(num.value), 0) /
        (ratesCounts == 0 ? 1 : ratesCounts),
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

  const { name, image_url, color, level, allowShuffle, col_id } = req.body;

  const id = parseInt(req.params.id);

  let collection;
  try {
    collection = await prisma.collection.findUnique({
      where: { id: parseInt(col_id) },
    });
  } catch (e) {
    console.log(e);
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  if (!collection) {
    return next(new NewError("لا يوجد رابط بهذا المعرف", 400));
  }

  let editedDeck;
  try {
    editedDeck = await prisma.deck.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء التعديل, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!editedDeck) {
    return next(new NewError("لا يوجد مجموعة بهذا المعرف", 500));
  }

  editedDeck = await prisma.deck.update({
    where: { id },
    data: {
      ...editedDeck,
      name,
      image_url,
      color,
      level,
      allowShuffle,
      col_id,
      version: parseInt(editedDeck.version) + 1,
    },
    include: {
      rates: true,
      cards: true,
      tags: true,
      collection: true,
    },
  });

  const ratesCounts = editedDeck.rates.length;

  editedDeck = {
    id: editedDeck.id,
    name: editedDeck.name,
    image_url: editedDeck.image_url,
    color: editedDeck.color,
    level: editedDeck.level,
    version: editedDeck.version,
    allowShuffle: editedDeck.allowShuffle,
    tags: editedDeck.tags,
    collection: editedDeck.collection,
    cards: editedDeck.cards,
    ratesCounts,
    rate:
      editedDeck.rates.reduce((total, num) => total + parseInt(num.value), 0) /
      (ratesCounts == 0 ? 1 : ratesCounts),
  };

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
        collection: true,
      },
    });
  } catch (e) {
    console.log(e);
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

  const ratesCounts = deck.rates.length;

  deck = {
    id: deck.id,
    name: deck.name,
    image_url: deck.image_url,
    color: deck.color,
    level: deck.level,
    version: deck.version,
    allowShuffle: deck.allowShuffle,
    tags: deck.tags,
    collection: deck.collection,
    cards: deck.cards,
    ratesCounts,
    rate:
      deck.rates.reduce((total, num) => total + parseInt(num.value), 0) /
      (ratesCounts == 0 ? 1 : ratesCounts),
  };

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
