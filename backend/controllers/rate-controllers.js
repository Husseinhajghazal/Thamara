const NewError = require("../models/new-error");
const { validationResult } = require("express-validator");
const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const postRate = async (req, res, next) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { deck_id, rate, user_id } = req.body;

  try {
    const foundedRate = await prisma.rate.findUnique({
      where: { user_id: user_id },
    });
    if (foundedRate) {
      return next(
        new NewError("لقد قمت بالتقييم من قبل، لا يمكنك التقييم مرة أخرى", 400)
      );
    }

    const foundedDeck = await prisma.deck.findUnique({
      where: { id: parseInt(deck_id) },
    });
    if (!foundedDeck) {
      return next(new NewError("لا يوجد مجموعة بهذا المعرف", 400));
    }

    const createdRate = await prisma.rate.create({
      data: {
        deck_id: parseInt(deck_id),
        user_id: user_id,
        value: parseInt(rate),
      },
    });

    let { id, name, image_url, color, level, version, rates } =
      await prisma.deck.update({
        where: { id: parseInt(deck_id) },
        data: {
          version: parseInt(foundedDeck.version) + 1,
        },
        include: {
          rates: true,
        },
      });

    const ratesCounts = rates.length;

    updatedDeck = {
      id,
      name,
      image_url,
      color,
      level,
      version,
      ratesCounts,
      rate:
        rates.reduce((total, num) => total + parseInt(num.value), 0) /
        ratesCounts,
    };

    res.status(201).json({
      message: "تم تقييم المجموعة بنجاح",
      createdRate,
      updatedDeck,
    });
  } catch (error) {
    return next(new NewError("حصلت مشكلة، الرجاء المحاولة لاحقا", 500));
  }
};

exports.postRate = postRate;
