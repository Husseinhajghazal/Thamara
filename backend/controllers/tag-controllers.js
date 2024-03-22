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

const connectTag = async (req, res, next) => {};

const deleteConnectedTag = async (req, res, next) => {};

exports.createTag = createTag;
exports.editTag = editTag;
exports.deleteTag = deleteTag;
exports.getAllTags = getAllTags;
exports.getOneTag = getOneTag;
