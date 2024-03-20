const NewError = require("../model/new-error");
const { validationResult } = require("express-validator");
const Rate = require("../model/rate");
const Deck = require("../model/deck");

const postRate = async (req, res, next) => {
  const errors = validationResult(req);

  if (!errors.isEmpty()) {
    return next(new NewError(errors.array()[0].msg, 422));
  }

  const { deck_id, rate, user_id } = req.body;

  try {
    const foundedRate = await Rate.findOne({ where: { user_id: user_id } });
    if (foundedRate) {
      return next(
        new NewError("لقد قمت بالتقييم من قبل، لا يمكنك التقييم مرة أخرى", 400)
      );
    }

    const foundedDeck = await Deck.findByPk(deck_id);
    if (!foundedDeck) {
      return next(new NewError("لا يوجد مجموعة بهذا المعرف", 400));
    }

    const rates = await Rate.findAll({ where: { deck_id } });
    const sum = rates.reduce((total, rate) => total + rate.value, 0);

    const newRate = (sum + +rate) / (rates.length + 1);

    await foundedDeck.update({
      ratesCount: rates.length + 1,
      rate: newRate,
      version: foundedDeck.version + 1,
    });

    const createdRate = await Rate.create({ deck_id, user_id, value: +rate });

    res.status(201).json({
      message: "تم تقييم المجموعة بنجاح",
      ratesCount: rates.length + 1,
      rate: Math.round(newRate),
      version: foundedDeck.version + 1,
      createdRate,
    });
  } catch (error) {
    return next(new NewError("حصلت مشكلة، الرجاء المحاولة لاحقا", 500));
  }
};

exports.postRate = postRate;
