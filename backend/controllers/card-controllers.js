const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const createCard = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { deck_id } = req.body;

  let deck;
  try {
    deck = await prisma.deck.findUnique({
      where: { id: parseInt(deck_id) },
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

  const createdCard = await prisma.card.create({
    data: {
      ...req.body,
    },
  });

  res.status(201).json({
    message: "تم أنشاء الكرت بنجاح",
    createdCard,
  });
};

const deleteCard = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let deletedCard;
  try {
    deletedCard = await prisma.card.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!deletedCard) {
    return next(
      new NewError("لا يجود كرت بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  try {
    await prisma.card.delete({
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
    message: "تم حذف الكرت بنجاح",
    deletedCard,
  });
};

const editCard = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { deck_id } = req.body;
  const id = parseInt(req.params.id);

  let deck;
  try {
    deck = await prisma.deck.findUnique({
      where: { id: parseInt(deck_id) },
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

  let card;
  try {
    card = await prisma.card.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!card) {
    return next(
      new NewError("لا يجود كرت بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  const updatedCard = await prisma.card.update({
    where: { id },
    data: {
      ...card,
      ...req.body,
    },
  });

  res.status(201).json({
    message: "تم تعديل الكرت بنجاح",
    updatedCard,
  });
};

const getOneCard = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let card;
  try {
    card = await prisma.card.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!card) {
    return next(
      new NewError("لا يجود كرت بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  res.json({
    message: "تم الحصول الكرت بنجاح",
    card,
  });
};

exports.createCard = createCard;
exports.deleteCard = deleteCard;
exports.editCard = editCard;
exports.getOneCard = getOneCard;
