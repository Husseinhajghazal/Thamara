const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const createTag = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name } = req.body;

  let foundedTag;
  try {
    foundedTag = await prisma.tag.findUnique({ where: { value: name } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (foundedTag) {
    return next(new NewError("يوجد تاغ مسبق بهذا الأسم", 500));
  }

  const createdTag = await prisma.tag.create({
    data: {
      value: name,
    },
  });

  res.status(201).json({
    message: "تم أنشاء التاغ بنجاح",
    createdTag,
  });
};

const getAllTags = async (req, res, next) => {
  let tags;
  try {
    tags = await prisma.tag.findMany();
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على التاغات, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  res.status(201).json({
    message: "تم الحصول على التاغات",
    tags,
  });
};

const editTag = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name } = req.body;

  const id = parseInt(req.params.id);

  let updatedTag;
  try {
    updatedTag = await prisma.tag.findUnique({ where: { id } });
  } catch (e) {
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  if (!updatedTag) {
    return next(new NewError("لا يوجد تاغ مسبق بهذا الأسم", 404));
  }

  updatedTag = await prisma.tag.update({
    where: { id },
    data: {
      value: name,
    },
  });

  res.status(201).json({
    message: "تم تعديل التاغ بنجاح",
    updatedTag,
  });
};

const deleteTag = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let foundedTag;
  try {
    foundedTag = await prisma.tag.findUnique({ where: { id } });
  } catch (e) {
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  if (!foundedTag) {
    return next(new NewError("لا يوجد تاغ مسبق بهذا الأسم", 404));
  }

  try {
    await prisma.tag.delete({
      where: {
        id,
      },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.status(201).json({
    message: "تم الحذف التاغ بنجاح",
    foundedTag,
  });
};

const getOneTag = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let tag;
  try {
    tag = await prisma.tag.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError(
        "حصلت مشكلة أثناء الحصول على المجموعة, الرجاء المحاولة لاحقا",
        500
      )
    );
  }

  if (!tag) {
    return next(
      new NewError("لا يجود مجموعة بهذا المعرف, الرجاء المحاولة لاحقا", 404)
    );
  }

  res.json({
    message: "تم الحصول على التاغ بنجاح",
    tag,
  });
};

const connectTag = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { tag_id, deck_id } = req.body;

  let connection;
  try {
    connection = await prisma.deckTag.findMany({
      where: {
        deck_id: parseInt(deck_id),
        tag_id: parseInt(tag_id),
      },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الربط, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (connection.length == 1) {
    return next(new NewError("المجموعة والتاغ مربوطين مسبقا", 400));
  }

  connection = await prisma.deckTag.create({
    data: {
      tag_id: parseInt(tag_id),
      deck_id: parseInt(deck_id),
    },
  });

  res.status(201).json({
    message: "تم الوصل بنجاح",
    connection,
  });
};

const disconnectTag = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { tag_id, deck_id } = req.body;

  let connection;
  try {
    connection = await prisma.deckTag.findMany({
      where: {
        deck_id: parseInt(deck_id),
        tag_id: parseInt(tag_id),
      },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الربط, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (connection.length != 1) {
    return next(new NewError("المجموعة والتاغ ليسوا مربوطين", 400));
  }

  try {
    connection = await prisma.deckTag.deleteMany({
      where: {
        tag_id: parseInt(tag_id),
        deck_id: parseInt(deck_id),
      },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الربط, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.status(201).json({
    message: "تم الفصل بنجاح",
  });
};

exports.createTag = createTag;
exports.editTag = editTag;
exports.deleteTag = deleteTag;
exports.getAllTags = getAllTags;
exports.getOneTag = getOneTag;
exports.connectTag = connectTag;
exports.disconnectTag = disconnectTag;
