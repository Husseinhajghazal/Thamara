const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const createCollection = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name } = req.body;

  let collection;
  try {
  } catch (e) {
    collection = await prisma.collection.findUnique({ where: { name } });
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (collection) {
    return next(new NewError("يوجد رابطة بهذا الأسم!", 400));
  }

  let createdCollection;
  try {
    createdCollection = await prisma.collection.create({
      data: { name },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.status(201).json({
    message: "تم أنشاء الرابطة بنجاح",
    createdCollection,
  });
};

const editCollection = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name } = req.body;

  const id = parseInt(req.params.id);

  let updatedCollection;
  try {
    updatedCollection = await prisma.collection.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء التعديل, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!updatedCollection) {
    return next(new NewError("لا يوجد رابط بهذا المعرف", 400));
  }

  try {
    updatedCollection = await prisma.collection.update({
      where: { id },
      data: { name },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.status(201).json({
    message: "تم تعديل الرابطة بنجاح",
    updatedCollection,
  });
};

const deleteCollection = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let deletedCollection;
  try {
    deletedCollection = await prisma.collection.findUnique({ where: { id } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الحذف, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (!deletedCollection) {
    return next(new NewError("لا يوجد رابط بهذا المعرف", 400));
  }

  try {
    deletedCollection = await prisma.collection.delete({
      where: { id },
    });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  res.status(201).json({
    message: "تم حذف الرابطة بنجاح",
    deletedCollection,
  });
};

const getOneCollection = async (req, res, next) => {
  const id = parseInt(req.params.id);

  let collection;
  try {
    collection = await prisma.collection.findUnique({
      where: { id },
      include: { decks: true },
    });
  } catch (e) {
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  if (!collection) {
    return next(new NewError("لا يوجد رابط بهذا المعرف", 400));
  }

  res.status(201).json({
    message: "تم الحصول على الرابطة",
    collection,
  });
};

const getAllCollections = async (req, res, next) => {
  let collections;
  try {
    collections = await prisma.collection.findMany({
      include: {
        decks: true,
      },
    });
  } catch (e) {
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  res.status(201).json({
    message: "تم الحصول على الروابط",
    collections,
  });
};

exports.createCollection = createCollection;
exports.editCollection = editCollection;
exports.deleteCollection = deleteCollection;
exports.getOneCollection = getOneCollection;
exports.getAllCollections = getAllCollections;
