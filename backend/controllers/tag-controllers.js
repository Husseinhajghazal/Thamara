const NewError = require("../model/new-error");
const { validationResult } = require("express-validator");
const Tag = require("../model/tag");

const createTag = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name } = req.body;

  let foundedTag;
  try {
    foundedTag = await Tag.findOne({ where: { name } });
  } catch (e) {
    return next(
      new NewError("حصلت مشكلة أثناء الأنشاء, الرجاء المحاولة لاحقا", 500)
    );
  }

  if (foundedTag) {
    return next(new NewError("يوجد تاغ مسبق بهذا الأسم", 500));
  }

  const createdTag = await Tag.create({
    value: name,
  });

  res.status(201).json({
    message: "تم أنشاء التاغ بنجاح",
    createdTag,
  });
};

const getAllTags = async (req, res, next) => {
  let tags;
  try {
    tags = await Tag.findAll();
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
    decks,
  });
};

const editTag = async (req, res, next) => {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { name } = req.body;

  const id = req.params.id;

  let foundedTag;
  try {
    foundedTag = await Tag.findByPk(id);
  } catch (e) {
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  if (!foundedTag) {
    return next(new NewError("لا يوجد تاغ مسبق بهذا الأسم", 404));
  }

  await foundedTag.update({
    value: name,
  });

  res.status(201).json({
    message: "تم تعديل التاغ بنجاح",
    foundedTag,
  });
};

const deleteTag = async (req, res, next) => {
  const id = req.params.id;

  let foundedTag;
  try {
    foundedTag = await Tag.findByPk(id);
  } catch (e) {
    return next(new NewError("حصلت مشكلة, الرجاء المحاولة لاحقا", 500));
  }

  if (!foundedTag) {
    return next(new NewError("لا يوجد تاغ مسبق بهذا الأسم", 404));
  }

  try {
    await Tag.destroy({
      where: {
        id: id,
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
  const id = req.params.id;

  let tag;
  try {
    tag = await Tag.findByPk(id);
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
    message: "تم الحصول على التاغ بنجاح",
    tag,
  });
};

exports.createTag = createTag;
exports.editTag = editTag;
exports.deleteTag = deleteTag;
exports.getAllTags = getAllTags;
exports.getOneTag = getOneTag;
